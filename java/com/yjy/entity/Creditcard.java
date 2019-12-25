package com.yjy.entity;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class Creditcard  {
    private String id;

    private Integer billDate;
    private Integer fixDate;
    
    @ApiModelProperty("统计开始日")
    private String billDateString;

    private Integer spaceDate;

    private String cardId;

    private String cardName;
    private String remark;

    private String creditLimitId;
    
    private CreditLimit creditLimit;
    
    @ApiModelProperty("距离最后还款日天数")
    private Integer countDownCount;
    @ApiModelProperty("最后还款日")
    private String lastDate;
  
}