package com.yjy.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import com.yjy.model.ConsumModel;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class Consume  {
    private String id;
    
    @ApiModelProperty("信用卡主键id")
    private String cardId;
    
    @ApiModelProperty("信用卡")
    private Creditcard creditcard;

	@ApiModelProperty("11修改1套现2消费3分期")
    private Integer type;

	@ApiModelProperty("消费金额")
	private BigDecimal money;
	
	@ApiModelProperty("费用")
	private BigDecimal cost;
	
	@ApiModelProperty("到账金额")
	private BigDecimal realMoney;
	
    private String remark;

    private Date createTime;
    
    @ApiModelProperty("是否有积分（针对套现）1有积分，0 没积分")
    private Integer score;

	public Consume() {
		super();
	}

	
	
   
}