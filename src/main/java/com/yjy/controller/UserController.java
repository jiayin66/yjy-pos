package com.yjy.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.yjy.entity.UserEntity;
import com.yjy.mapper.PosMapper;
import com.yjy.mapper.UserMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/user/")
@Api(tags="用户管理")
public class UserController {
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private PosMapper posMapper;
	@GetMapping("findAllUser")
	@ApiOperation("【plus】查询用户，每个用户带自己携带的机器")
	public List<UserEntity> findAllUser(){
		List<UserEntity> result=userMapper.selectList(null);
		if(CollectionUtils.isEmpty(result)) {
			return result;
		}
		for(UserEntity userEntity:result) {
			String userId = userEntity.getId();
			if(userId!=null) {
				Map<String,Object> columnMap = new HashMap<>();
				columnMap.put("USER_ID", userId);
				userEntity.setMachineList(posMapper.selectByMap(columnMap));
			}
			
		}
		return result;
	}
	@GetMapping("findUser")
	@ApiOperation("【xml】查询用户，每个用户带自己携带的机器")
	public List<UserEntity> findUser(@ApiParam("跟pos类型一样1银钱包 ")@RequestParam(value="type",required=false) Integer type){
		return userMapper.findUser(type);
	}
	
	@PostMapping("insertUser")
	@ApiOperation("添加用户，自动生成插入时间")
	public void insert(@RequestBody UserEntity userEntity){
		if(userEntity.getAddTime()==null) {
			userEntity.setAddTime(new Date());
		}
	   userMapper.insert(userEntity);
	}
	@PostMapping("updateUser")
	@ApiOperation("修改用户")
	public void update(@RequestBody UserEntity userEntity){
		userMapper.updateById(userEntity);
	}
}
