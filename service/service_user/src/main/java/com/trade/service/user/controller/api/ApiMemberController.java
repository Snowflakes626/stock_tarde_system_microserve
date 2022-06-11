package com.trade.service.user.controller.api;



import com.trade.exception.TradeException;
import com.trade.result.R;
import com.trade.result.ResultCodeEnum;
import com.trade.service.user.entity.BalanceChange;
import com.trade.service.user.entity.BalanceFromBank;
import com.trade.service.user.entity.Member;
import com.trade.service.user.entity.UserBalance;
import com.trade.service.user.entity.vo.LoginVo;
import com.trade.service.user.entity.vo.QueryVo;
import com.trade.service.user.entity.vo.RegisterVo;
import com.trade.service.user.service.MemberService;
import com.trade.utils.JwtInfo;
import com.trade.utils.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Objects;

@CrossOrigin
@Api(tags = "会员管理")
@RestController
@RequestMapping("/api/ucenter/member")
@Slf4j
public class ApiMemberController {

	@Autowired
	private MemberService memberService;

	@ApiOperation(value = "会员注册")
	@PostMapping("/register")
	public R register(@Validated @RequestBody RegisterVo registerVo) {
		memberService.register(registerVo);
		return R.ok();
	}

	@ApiOperation(value = "会员登录")
	@PostMapping("/login")
	public R login(@RequestBody LoginVo loginVo) {
		String token = memberService.login(loginVo);
		return R.ok().data("token", token);
	}

	@ApiOperation(value = "根据token获取信息")
	@GetMapping("/get-info")
	public R getLoginInfo(HttpServletRequest request) {
		try {
			JwtInfo jwtInfo = JwtUtils.getMemberIdByJwtToken(request);
			return R.ok().data("userInfo", jwtInfo);
		} catch (Exception e) {
			log.error("解析用户信息失败，" + e.getMessage());
			throw new TradeException(ResultCodeEnum.FETCH_USERINFO_ERROR);
		}
	}

	@ApiOperation(value = "展示信息")
	@GetMapping("/detail")
	public R getById(HttpServletRequest request) {
		String id = Objects.requireNonNull(JwtUtils.getMemberIdByJwtToken(request)).getId();
		System.out.println(id);
		QueryVo queryVo = memberService.infoQuery(id);
		// Member member = memberService.getById(id);
		if (queryVo != null) {
			return R.ok().data("item", queryVo);
		} else {
			return R.error().message("用户信息错误");
		}
	}

	@ApiOperation(value = "更新信息")
	@PutMapping("/update")
	public R updateById(@ApiParam(value = "用户对象", required = true)
	                    @RequestBody QueryVo queryVo) {
		Member member = memberService.getById(queryVo.getId());
		member.setId(queryVo.getId());
		member.setAge(queryVo.getAge());
		member.setAvatar(queryVo.getAvatar());
		member.setEmail(queryVo.getEmail());
		member.setPhone(queryVo.getPhone());
		member.setUsername(queryVo.getUsername());
		member.setSex(queryVo.getSex());
		member.setSign(queryVo.getSign());
		boolean result = memberService.updateById(member);
		if (result) {
			return R.ok().message("修改成功");
		} else {
			return R.error().message("修改失败");
		}
	}

	@ApiOperation(value = "登出")
	@PutMapping("/logout")
	public R logout(HttpServletRequest request) {
		String jwtToken = Objects.requireNonNull(JwtUtils.getMemberIdByJwtToken(request)).toString();
		boolean flag = memberService.logout(jwtToken);
		if (flag) return R.ok();
		else return R.setResult(ResultCodeEnum.LOGOUT_ERROR);
	}

	@ApiOperation(value = "获取余额")
	@GetMapping("/getBalance")
	public R getUserBalance(String id) {
		System.out.println(id);
		UserBalance balance = memberService.getUserBalance(id);
		if (balance == null) return R.error().message("查询出错");
		return R.ok().data("data", balance);
	}

	@ApiOperation(value = "更新余额")
	@PutMapping("/updateBalance")
	public R updateUserBalance(@RequestBody BalanceChange balanceChange) {
		return memberService.updateUserBalance(balanceChange);
	}

	@ApiOperation(value = "更新账号资金")
	@PostMapping("/updateBalanceByBank")
	public R updateBalanceByBank(@RequestBody BalanceFromBank balance) {
		String userId =balance.getUserId();
		String updateValue = balance.getUpdateValue();
		return memberService.updateBalanceByBank(userId, new BigDecimal(updateValue));
	}


}
