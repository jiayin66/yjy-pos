package com.yjy.mapper;

import com.yjy.entity.CreditLimit;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


public interface CreditLimitMapper  {

	CreditLimit findLimitById(String limitId);

	void saveAvailable(@Param("id")String id,@Param("count") BigDecimal count);
}