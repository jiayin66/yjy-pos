package com.yjy.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

@Data
public class CreditLimit  {
    private String id;

    private BigDecimal availableCredit;

    private BigDecimal limitCredit;

    private BigDecimal temporaryCredit;

    private String creditRemark;

   
}