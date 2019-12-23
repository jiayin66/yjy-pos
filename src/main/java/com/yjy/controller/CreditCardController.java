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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
	
	@GetMapping("find")
	@ApiOperation("查询全部的信用卡，后期加上排序功能，额度计算功能")
	public List<Creditcard> find(){
		List<Creditcard> result =creditcardMapper.findAll();
		Date now = new Date();
		System.out.println("当前时间："+YJYDateUtils.formartUseDate(now));
		for(Creditcard creditcard:result) {
			Integer resultDate = 0;
			Integer billInt = creditcard.getBillDate();
			Integer spaceDate = creditcard.getSpaceDate();
			
			
			if(billInt==null||spaceDate==null) {
				continue;
			}
			//【1】拿到实际的账单日
			//，时分秒等跟now相同
			Date billDate = YJYDateUtils.getDateFormInteger(now,billInt);
			if(now.equals(billDate)||now.after(billDate)) {  //否则上期账单
				//下期账单，时分秒不变
				billDate=YJYDateUtils.getDateFormIntegerNext(now,billInt);
			}
			//统计周期结束日
			billDate=YJYDateUtils.getDatePreOne(billDate);
			creditcard.setBillDateString("=>"+YJYDateUtils.formartUseDate(billDate));
			
			//【2】拿到最后还款日
			Integer fixDate = creditcard.getFixDate();
			Date lastDate;
			if(fixDate==null) {
				lastDate=YJYDateUtils.getLastDate(billDate,spaceDate);
			}else {
				//取月份
				Date month=YJYDateUtils.getLastDate(billDate,spaceDate);
				//在设置天数
				lastDate=YJYDateUtils.getDateFormInteger(month,fixDate);
			}
			creditcard.setLastDate(YJYDateUtils.formartUseDate(lastDate));
			//【3】还款剩余天数
		
			Integer count=YJYDateUtils.getBetween(lastDate,now);
			creditcard.setCountDownCount(count);
			
		}
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
	
	@PostMapping("consumefind")
	@ApiOperation("查找交易记录")
	public List<Consume> consumefind(@RequestBody ConsumeReq consumeReq){
		return consumeMapper.consumefind(consumeReq);
	}
	
}
