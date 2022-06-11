package com.trade.service.predict.service.Impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.trade.service.predict.entity.User;
import com.trade.service.predict.mapper.UserMapper;
import com.trade.service.predict.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
	@Override
	public boolean updateScore(Double score, String id) {
		return baseMapper.insertInfo(id, score);
	}

}

