package com.trade.service.predict.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.trade.service.predict.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService extends IService<User> {
	boolean updateScore(Double score, String id);
}
