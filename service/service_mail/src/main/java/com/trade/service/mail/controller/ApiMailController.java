package com.trade.service.mail.controller;


import com.trade.exception.TradeException;
import com.trade.result.R;
import com.trade.result.ResultCodeEnum;
import com.trade.service.mail.service.MailService;
import com.trade.utils.FormUtils;
import com.trade.utils.RandomUtils;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/mail")
@Api(tags = "邮箱验证码")
@Slf4j
public class ApiMailController {
	@Autowired
	private MailService mailService;

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@GetMapping("send/{email}")
	public R getCode(@PathVariable String email) {
		System.out.println(email);
		// 防刷验证
		String redisCode = redisTemplate.opsForValue().get(email);
		System.out.println(redisCode);
		// System.out.println(redisCode.length());
		// 如果不为空，则返回错误信息
		if (null != redisCode && redisCode.length() > 0) {

			return R.error()
					.code(ResultCodeEnum.MMS_SEND_ERROR_BUSINESS_LIMIT_CONTROL.getCode())
					.message(ResultCodeEnum.MMS_SEND_ERROR_BUSINESS_LIMIT_CONTROL.getMessage());

		}
		// 校验邮箱是否合法
		if (StringUtils.isEmpty(email) || !FormUtils.isEmail(email)) {
			return R.error().message("请输入正确的邮箱");
		}
		// 生成验证码
		String checkCode = RandomUtils.getFourBitRandom();
		System.out.println(checkCode);
		// 发送验证码
		try {
			mailService.sendCode(email, checkCode, "code.html");
		} catch (Exception e) {
			throw new TradeException(ResultCodeEnum.MAIL_ERROR);
		}
		//将验证码存入redis缓存
		redisTemplate.opsForValue().set(email, checkCode, 5, TimeUnit.MINUTES);

		return R.ok().message("邮件发送成功");
	}
}
