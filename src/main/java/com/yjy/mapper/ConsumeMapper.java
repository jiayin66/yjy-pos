package com.yjy.mapper;

import com.yjy.entity.Consume;
import com.yjy.entity.ConsumeReq;

import java.util.List;

import org.springframework.stereotype.Repository;

public interface ConsumeMapper  {

	void insertSelective(Consume consume);

	List<Consume> consumefind(ConsumeReq consumeReq);
}