package com.yjy.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.yjy.entity.CreditLimit;
import com.yjy.entity.Creditcard;

@Repository
public interface CreditcardMapper {

	List<Creditcard> findAll();

	String findLimitId(String id);

	void updateRemark(@Param("id") String id,@Param("remark") String remark);

}