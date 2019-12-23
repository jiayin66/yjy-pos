package com.yjy.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yjy.entity.UserEntity;

public interface UserMapper extends BaseMapper<UserEntity>{

	List<UserEntity> findUser(Integer type);
	

}
