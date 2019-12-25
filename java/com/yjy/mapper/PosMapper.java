package com.yjy.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yjy.entity.PosEntity;
import com.yjy.entity.UserEntity;

public interface PosMapper extends BaseMapper<PosEntity>{
	@Update("update t_pos set USER_ID=0 where USER_ID=#{userId}")
	void clearUserId(@Param("userId") String userId);
	
	public List<PosEntity> findByUserIdHasUser(@Param("userId") String userId);

	List<UserEntity> findUser(Integer type);
}
