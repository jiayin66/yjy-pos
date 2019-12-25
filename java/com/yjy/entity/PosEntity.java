package com.yjy.entity;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
@Data
@TableName("t_pos")
public class PosEntity {
	public PosEntity(String id, String userId) {
		this.id=id;
		this.userId=userId;
	}

	public PosEntity() {
		super();
		// TODO Auto-generated constructor stub
	}

	@TableId(type=IdType.UUID)
	private String id;
	
	@TableField("TYPE")
	@ApiModelProperty("1银钱包 2新中付 3瑞通宝")
	private Integer type;
	
	@TableField("MACHINE_ID")
	private String machineId;
	


	@TableField("USE_TIME")
	private Date useTime;
	
	@TableField("USER_ID")
	private String userId;
	
	@TableField(exist = false)
	private UserEntity userEntityList;
	
	@TableField("REMARK")
	private String remark;
	
}
