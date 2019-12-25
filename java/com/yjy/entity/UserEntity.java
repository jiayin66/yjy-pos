package com.yjy.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
@Data
@TableName("t_user")
public class UserEntity {
	@TableId(type=IdType.UUID)
	private String id;
	
	@TableField("NAME")
	private String name;
	
	@TableField("DISCRIPTION")
	private String discription;
	
	@TableField("PHONE")
	private String phone;
	
	@TableField("ADDRESS")
	private String address;
	
	@TableField("REMARK")
	private String remark;
	
	@TableField("SORT")
	private Integer sort;
	
	@TableField("ADD_TIME")
	private Date addTime;
	
	@TableField(exist = false)
	private List<PosEntity> machineList;//=new ArrayList<PosEntity>();;
}
