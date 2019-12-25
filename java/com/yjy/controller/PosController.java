package com.yjy.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yjy.entity.PosEntity;
import com.yjy.entity.UserEntity;
import com.yjy.mapper.PosMapper;
import com.yjy.mapper.UserMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@RestController
@RequestMapping("/pos/")
@Api(tags="pos管理")
public class PosController {
	@Autowired
	private PosMapper posMapper;
	
	@GetMapping("findByUserId")
	@ApiOperation("根据用户id查询机器，all查全部")
	public List<PosEntity> findByUserId(@RequestParam(value="userId") String userId){
		if("all".equals(userId)) {
			return posMapper.selectList(null);
		}
		Map<String,Object> columnMap = new HashMap<>();
		columnMap.put("USER_ID", userId);
		return posMapper.selectByMap(columnMap);
	}
	@ApiOperation("【plus】查询没有绑定的机器，以及该用户绑定的机器，方便修改")
	@GetMapping("findByUserIdAndNull")
	public List<PosEntity> findByUserIdAndNull(@RequestParam(value="userId") String userId){
		Map<String,Object> columnMap1 = new HashMap<>();
		columnMap1.put("USER_ID", userId);
		List<PosEntity> selectByMap1 = posMapper.selectByMap(columnMap1);
		
		Map<String,Object> columnMap2 = new HashMap<>();
		//因为主键是long类型，所以原来的null也是0，但是0并不是null
		columnMap2.put("USER_ID", null);
		List<PosEntity> selectByMap2 = posMapper.selectByMap(columnMap2);
		selectByMap1.addAll(selectByMap2);
		
		return selectByMap1;
	}
	@ApiOperation("【xml】查询没有绑定的机器，以及该用户绑定的机器，方便修改")
	@GetMapping("findByUserIdHasUser")
	public List<PosEntity> findByUserIdUserXml(@RequestParam(value="userId",required=false) String userId){
		if("all".equals(userId)) {
			userId=null;
		}
		List<PosEntity> result=posMapper.findByUserIdHasUser(userId);
		return result;
	}
	
	@ApiOperation("保存机器")
	@PostMapping("insertPos")
	public void insert(@RequestBody PosEntity posEntity){
		posMapper.insert(posEntity);
	}
	
	@ApiOperation("修改机器")
	@PostMapping("updatePos")
	public void update(@RequestBody  PosEntity posEntity){
		posMapper.updateById(posEntity);
	}
	
	@ApiOperation("批量绑定用户和机器")
	@PostMapping("bindingUser")
	public void bindingUser(@RequestParam("userId")String userId,@RequestBody  List<String> ids){
		posMapper.clearUserId(userId);
		for(String id:ids) {
			PosEntity selectById = posMapper.selectById(id);
			posMapper.updateById(new PosEntity(id,userId));
		}
	}
	
}
