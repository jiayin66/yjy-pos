package com.yjy.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yjy.Utils.YJYDateUtils;
import com.yjy.entity.Consume;
import com.yjy.entity.ConsumeReq;
import com.yjy.entity.CreditLimit;
import com.yjy.entity.Creditcard;
import com.yjy.entity.UserEntity;
import com.yjy.mapper.ConsumeMapper;
import com.yjy.mapper.CreditLimitMapper;
import com.yjy.mapper.CreditcardMapper;
import com.yjy.mapper.PosMapper;
import com.yjy.mapper.UserMapper;
import com.yjy.model.ConsumModel;
import com.yjy.model.PageUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/creditcard/")
@Api(tags="信用卡管理")
public class CreditCardController {
	@Autowired
	private CreditcardMapper creditcardMapper;
	
	@Autowired
	private ConsumeMapper consumeMapper;
	
	@Autowired
	private CreditLimitMapper creditLimitMapper;
	
	@PostMapping("consumefind")
	@ApiOperation("【1】查找交易记录")
	public List<Consume> consumefind(@RequestBody ConsumeReq consumeReq){
		return consumeMapper.consumefind(consumeReq);
	}
	@PostMapping("consumefind/{pageIndex}/{pagesize}")
	@ApiOperation("【1】查找交易记录")
	public PageUtils<Consume> consumefindByPage(@PathVariable("pageIndex")Integer pageIndex,@PathVariable("pagesize")Integer pagesize,
	@RequestBody ConsumeReq consumeReq){
		Page<Object> startPage = PageHelper.startPage(pageIndex, pagesize);
		List<Consume> consumefind = consumeMapper.consumefind(consumeReq);
		return new PageUtils(consumefind,startPage.getTotal());
	}
	
	@GetMapping("find")
	@ApiOperation("【2】查询全部的信用卡，后期加上排序功能，额度计算功能")
	public List<Creditcard> find(){
		//拿到每张卡的信息
		List<Creditcard> result =creditcardMapper.findAll();
		Date now = new Date();
		System.out.println("当前时间："+YJYDateUtils.formartUseDate(now));
		
		//核心逻辑，计算最后还款日的天数
		for(Creditcard creditcard:result) {
			//【1】根据配置的账单统计不带月份的“结束日”，找到本次刷卡归属的结束年月日。（账单结束日是固定值）
			Date billDate=getBillEndDateByEndDay(creditcard.getEndDate(),now);
			creditcard.setBillDateString("=>"+YJYDateUtils.formartUseDate(billDate));
			
			//【2】根据我们计算的账单结束年月日，加上配置的免息日期。
			Date lastDate=getFinalDateByEndDate(billDate,creditcard.getSpaceDate(),creditcard.getFixDate());
			
			creditcard.setLastDate(YJYDateUtils.formartUseDate(lastDate));
			//【3】还款剩余天数
		
			Integer count=YJYDateUtils.getBetween(lastDate,now);
			creditcard.setCountDownCount(count);
			
		}
		
		//最后对所有数据排序
		result.sort(new Comparator<Creditcard>() {

			@Override
			public int compare(Creditcard o1, Creditcard o2) {
				Integer a = o1.getCountDownCount();
				Integer b = o2.getCountDownCount();
				a = (a==null)?0 : a;
				b = (b==null)?0 : b;
				return b-a;
			}
			
		});
		return result;
	}
	/**
	 * 根据账单周期最后一天，免息期，固定还款日综合计算最后还款日
	 * @param billDate
	 * @param spaceDate
	 * @return
	 */
	private Date getFinalDateByEndDate(Date billDate, Integer spaceDate,Integer fixDay) {
		Date lastDate=YJYDateUtils.getLastDate(billDate,spaceDate);
		if(fixDay!=null) {
			//配置了固定的最后还款日，就以这个为准。
			Date fixDate=YJYDateUtils.getDateFormInteger(lastDate,fixDay);
			if(!dvalueIsAllow(lastDate,fixDate)) {
				throw new RuntimeException("计算最后还款日出现差错billDate："+billDate);
			}
		}
		return lastDate;
	}

	/**
	 * 是否允许两个时间的差值范围。一般只差距一天。逻辑是大于3天不合理
	 * @param lastDate
	 * @param fixDate
	 * @return
	 */
	private boolean dvalueIsAllow(Date lastDate, Date fixDate) {
		Long d=lastDate.getTime()-fixDate.getTime();
		if(d==0) {
			return true;
		}
		if(d<0) {
			d=d*(-1);
		}
		if(d/1000/60/60/24>3) {
			return false;
		}
		return true;
	}

	/**
	 * 根据配置的结束day，拿到当前刷卡的账单归属是的结束年月日
	 * @param billInt
	 * @return
	 */
	private Date getBillEndDateByEndDay(Integer billInt,Date now ) {
		//时分秒等跟now相同
		Date billDate = YJYDateUtils.getDateFormInteger(now,billInt);
		if(now.after(billDate)) {  //否则上期账单
			//下期账单，时分秒不变
			billDate=YJYDateUtils.getDateFormIntegerNext(now,billInt);
		}
		return billDate;
	}

	@GetMapping("repayment")
	@ApiOperation("还款，传入信用卡id")
	public boolean repayment(@RequestParam("id") String id,@RequestParam("money") BigDecimal money){
		String limitId=creditcardMapper.findLimitId(id);
		CreditLimit creditLimit=creditLimitMapper.findLimitById(limitId);
		BigDecimal count=creditLimit.getAvailableCredit().add(money);
		creditLimitMapper.saveAvailable(limitId,count);
		return true;
	}
	@PostMapping("consume")
	@ApiOperation("消费，传入信用卡id")
	public boolean consume(@RequestBody Consume consume){
		System.out.println("入参"+consume.toString());
		Integer type = consume.getType();
		//这个是修改备注和余额
		if(type!=null && type==11) {
			creditcardMapper.updateRemark(consume.getCardId(),consume.getRemark());
			String limitId=creditcardMapper.findLimitId(consume.getCardId());
			creditLimitMapper.saveAvailable(limitId,consume.getMoney());
			return true;
		}
		
		//1.额度减少
		String limitId=creditcardMapper.findLimitId(consume.getCardId());
		CreditLimit creditLimit=creditLimitMapper.findLimitById(limitId);
		BigDecimal count=creditLimit.getAvailableCredit().subtract(consume.getMoney());
		creditLimitMapper.saveAvailable(limitId,count);
		//2.记录一笔消费
		consume.setId(UUID.randomUUID().toString());
		consume.setCreateTime(new Date());
		consumeMapper.insertSelective(consume);
		return true;
	}
	

	
}
