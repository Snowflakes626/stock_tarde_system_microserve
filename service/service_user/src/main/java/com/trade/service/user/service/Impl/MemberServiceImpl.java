package com.trade.service.user.service.Impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.trade.exception.TradeException;
import com.trade.result.R;
import com.trade.result.ResultCodeEnum;
import com.trade.service.user.entity.BalanceChange;
import com.trade.service.user.entity.Member;
import com.trade.service.user.entity.UserBalance;
import com.trade.service.user.entity.vo.LoginVo;
import com.trade.service.user.entity.vo.QueryVo;
import com.trade.service.user.entity.vo.RegisterVo;
import com.trade.service.user.mapper.MemberMapper;
import com.trade.service.user.service.MemberService;
import com.trade.utils.FormUtils;
import com.trade.utils.JwtInfo;
import com.trade.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService {

	@Autowired(required = false)
	private RedisTemplate<String, String> redisTemplate;

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void register(RegisterVo registerVo) {
		String username = registerVo.getUsername();
		String mobile = registerVo.getPhone();
		String email = registerVo.getEmail();
		String password = registerVo.getPassword();
		String code = registerVo.getCode();
		String roles = "user";

		//手机号和邮箱不能同时为空
		if (StringUtils.isEmpty(mobile) && StringUtils.isEmpty(email)) {
			throw new TradeException(ResultCodeEnum.PARAM_ERROR);
		}

		//优先校验手机，没有手机则校验邮箱
		//注意: 这里需要使用 '|' 要两边都能检测到
		if (!StringUtils.isEmpty(mobile) | FormUtils.isMobile(mobile)) {
			//校验验证码
			String checkCode = redisTemplate.opsForValue().get(mobile);
			if (!code.equals(checkCode)) {
				throw new TradeException(ResultCodeEnum.CODE_ERROR);
			}
		} else if (!StringUtils.isEmpty(email) | FormUtils.isMobile(email)) {
			//校验验证码
			String checkCode = redisTemplate.opsForValue().get(email);
			System.out.println(email);
			System.out.println(checkCode+"--");
			if (!code.equals(checkCode)) {
				throw new TradeException(ResultCodeEnum.CODE_ERROR);
			}
		}

		// 校验成功后删除验证码
		redisTemplate.delete(email);

		//是否被注册的条件
		QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
		if (!StringUtils.isEmpty(email)){
			queryWrapper.lambda().or().eq(Member::getEmail,email);
		}
		Integer count = baseMapper.selectCount(queryWrapper);
		if (count > 0) {
			throw new TradeException(ResultCodeEnum.REGISTER_USERINFO_ERROR);
		}

		// 注册
		Member member = new Member();
		member.setUsername(username);
		member.setPhone(mobile);
		member.setEmail(email);
		member.setRoles(roles);
		// 每次相同的password都会得到不同的加密结果
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		System.out.println(encoder);
		member.setPassword(encoder.encode(password));
		//member.setPassword(MD5.encrypt(password));
		member.setDisabled(false);
		//默认头像
		member.setAvatar("https://edu-college.oss-cn-shenzhen.aliyuncs.com/avatar/2020/07/27/20200727205056.jpg");
		baseMapper.insert(member);
	}

	@Override
	public String login(LoginVo loginVo) {
		String userInfo = loginVo.getUserInfo();
		String password = loginVo.getPassword();

		System.out.println(userInfo+" "+password);
		QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(Member::getPhone, userInfo).or().eq(Member::getEmail, userInfo);
		Member member = baseMapper.selectOne(queryWrapper);

		if (member == null) {
			throw new TradeException(ResultCodeEnum.LOGIN_ERROR);
		}
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		System.out.println(member.getPassword());
		if (!encoder.matches(password, member.getPassword())) {
			throw new TradeException(ResultCodeEnum.LOGIN_ERROR);
		}

		//检验用户是否被禁用
		if (member.getDisabled()) {
			throw new TradeException(ResultCodeEnum.LOGIN_DISABLED_ERROR);
		}

		JwtInfo jwtInfo = new JwtInfo();
		jwtInfo.setId(member.getId());
		jwtInfo.setUsername(member.getUsername());
		jwtInfo.setAvatar(member.getAvatar());

		redisTemplate.opsForValue().set(jwtInfo.toString(), "online", 30, TimeUnit.MINUTES);

		return JwtUtils.getJwtToken(jwtInfo, 1800);
	}

	@Override
	public Integer countRegisterNum(String day) {
		return baseMapper.selectRegisterNumByDay(day);
	}

	@Override
	public QueryVo infoQuery(String id) {
		System.out.println("id = " + id);
		return baseMapper.selectQueryVo(id);
	}

	@Override
	public boolean logout(String jwtToken) {
		return Boolean.TRUE.equals(redisTemplate.delete(jwtToken));
	}

	@Override
	public UserBalance getUserBalance(String id) {
		return baseMapper.selectBalance(id);
	}

	@Override
	public R updateUserBalance(BalanceChange balanceChange) {
		String userId = balanceChange.getUserId();
		BigDecimal avlChange = balanceChange.getAvlChangeBalance();
		BigDecimal totalChange = balanceChange.getTotalChangeBalance();
		boolean result = baseMapper.updateBalance(userId, avlChange, totalChange);
		if (!result) {
			return R.error().message("更新余额错误");
		}
		return R.ok().message("更新余额成功");
	}

	@Override
	public R updateBalanceByBank(String userId, BigDecimal updateValue) {
		if (baseMapper.updateBalanceByBank(userId, updateValue)) {
			return R.ok().message("银行转账成功");
		}
		return R.error().message("银行转账失败");
	}
}
