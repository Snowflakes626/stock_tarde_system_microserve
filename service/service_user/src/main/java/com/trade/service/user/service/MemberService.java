package com.trade.service.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.trade.result.R;
import com.trade.service.user.entity.BalanceChange;
import com.trade.service.user.entity.Member;
import com.trade.service.user.entity.UserBalance;
import com.trade.service.user.entity.vo.LoginVo;
import com.trade.service.user.entity.vo.QueryVo;
import com.trade.service.user.entity.vo.RegisterVo;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Service
public interface MemberService extends IService<Member> {
	void register(RegisterVo registerVo);

	String login(LoginVo loginVo);

	// 查询注册数
	Integer countRegisterNum(String day);

	QueryVo infoQuery(String id);

	boolean logout(String jwtToken);

	UserBalance getUserBalance(String userId);

	R updateUserBalance(BalanceChange balanceChange);

	R updateBalanceByBank(String userId, BigDecimal updateValue);
}
