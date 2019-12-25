package com.yjy.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import com.yjy.model.ConsumModel;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ConsumeReq  {
   
    
    @ApiModelProperty("信用卡主键id")
    private String cardId;

	@ApiModelProperty("1套现2消费3分期")
    private Integer type;

    private Date startTime;
    private Date endTime;
   
    @ApiModelProperty("是否有积分（针对套现）1有积分，0 没积分")
    private Integer score;

	public ConsumeReq() {
		super();
	}

	
	
   
}