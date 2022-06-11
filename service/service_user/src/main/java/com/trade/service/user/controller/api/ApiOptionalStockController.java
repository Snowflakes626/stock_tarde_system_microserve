package com.trade.service.user.controller.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.trade.exception.TradeException;
import com.trade.result.R;
import com.trade.result.ResultCodeEnum;
import com.trade.service.user.entity.OptionalStock;
import com.trade.service.user.service.OptionalStockService;
import com.trade.utils.JwtInfo;
import com.trade.utils.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@CrossOrigin
@Api(tags = "自选股管理")
@RestController
@RequestMapping("/api/ucenter/optional")
@Slf4j
public class ApiOptionalStockController {

	@Autowired
	OptionalStockService optionalStockService;

	@ApiOperation(value = "增加自选股")
	@PostMapping("add")
	public R addOptionalStock(HttpServletRequest request) {
		String id = "";
		try {
			JwtInfo jwtInfo = JwtUtils.getMemberIdByJwtToken(request);
			id = jwtInfo.getId();
			System.out.println("id = " + id);
		} catch (Exception e) {
			log.error("解析用户信息失败，" + e.getMessage());
			throw new TradeException(ResultCodeEnum.FETCH_USERINFO_ERROR);
		}
		boolean flag = optionalStockService.addOptionalStock(request.getParameter("code"), id, request.getParameter("name"));
		if (!flag) {
			return R.error().message("增加错误");
		}
		return R.ok().message("增加成功");
	}

	@ApiOperation(value = "删除自选股")
	@DeleteMapping("delete")
	public R deleteOptionalStock(
			HttpServletRequest request) {
		String id = "";
		try {
			JwtInfo jwtInfo = JwtUtils.getMemberIdByJwtToken(request);
			id = jwtInfo.getId();
			System.out.println("id = " + id);
		} catch (Exception e) {
			log.error("解析用户信息失败，" + e.getMessage());
			throw new TradeException(ResultCodeEnum.FETCH_USERINFO_ERROR);
		}
		String code = request.getParameter("code");
		boolean flag = optionalStockService.deleteOptionalStock(code, id);
		if (!flag) {
			return R.error().message("删除错误");
		}
		return R.ok().message("删除成功");
	}

	@ApiOperation("分页自选列表")
	@GetMapping("list/{page}/{limit}")
	public R index(
			@ApiParam(value = "当前页码", required = true)
			@PathVariable Long page,

			@ApiParam(value = "每页")
			@PathVariable Long limit,

			HttpServletRequest request) {
		String id = "";
		try {
			JwtInfo jwtInfo = JwtUtils.getMemberIdByJwtToken(request);
			id = jwtInfo.getId();
			System.out.println("id = " + id);
		} catch (Exception e) {
			log.error("解析用户信息失败，" + e.getMessage());
			throw new TradeException(ResultCodeEnum.FETCH_USERINFO_ERROR);
		}
		IPage<OptionalStock> pageModel = optionalStockService.selectPage(page, limit, id);
		long total = pageModel.getTotal();
		System.out.println("总页数total = " + total);
		List<OptionalStock> records = pageModel.getRecords();
		System.out.println("records = " + records);
		return R.ok().data("total", total).data("rows", records);
	}

}
