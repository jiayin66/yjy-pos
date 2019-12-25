package com.yjy.model;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ConsumModel {
	@ApiModelProperty("1套现2消费3分期")
	private Integer type;
	
	@ApiModelProperty("信用卡id")
	private String creditId;
	
	@ApiModelProperty("信用卡卡号")
	private String cardId;
	
	@ApiModelProperty("消费金额")
	private BigDecimal money;
	
	@ApiModelProperty("费用")
	private BigDecimal cost;
	
	@ApiModelProperty("备注")
	private String remark;
	
	@ApiModelProperty("是否有积分（针对套现）1有积分，0 没积分")
    private Integer score;
}
