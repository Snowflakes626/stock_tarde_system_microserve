package com.trade.service.predict.controller;

import com.trade.exception.TradeException;
import com.trade.result.R;
import com.trade.result.ResultCodeEnum;
import com.trade.service.predict.entity.User;
import com.trade.service.predict.service.UserService;
import com.trade.utils.JwtInfo;
import com.trade.utils.JwtUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@CrossOrigin
@RestController
@RequestMapping("/api/predict/user")
public class UserController {
	@Resource
	private UserService userService;

	@PostMapping("/questionnaire")
	public R questionnaireUpadte(@RequestBody User user) {
		String id = "";
		try {
			JwtInfo jwtInfo = JwtUtils.getMemberIdByJwtToken(user.getToken());
			id = jwtInfo.getId();
			System.out.println("id = " + id);
		} catch (Exception e) {
			//log.error("解析用户信息失败，" + e.getMessage());
			throw new TradeException(ResultCodeEnum.FETCH_USERINFO_ERROR);
		}
		Double score = user.getScore();
		System.out.println(id+" "+score);
		if (userService.updateScore(score, id)) {
			return R.ok();
		}
		return R.error().message("提交失败");
	}
}
