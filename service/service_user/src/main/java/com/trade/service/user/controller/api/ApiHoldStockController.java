package com.trade.service.user.controller.api;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.trade.exception.TradeException;
import com.trade.result.R;
import com.trade.result.ResultCodeEnum;
import com.trade.service.user.entity.HoldStockChange;
import com.trade.service.user.entity.HoldStockInfo;
import com.trade.service.user.entity.OptionalStock;
import com.trade.service.user.service.HoldStockService;
import com.trade.utils.JwtInfo;
import com.trade.utils.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import jdk.nashorn.internal.ir.IdentNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@CrossOrigin
@Api(tags = "持仓管理")
@RestController
@RequestMapping("/api/ucenter/hold-stock")
@Slf4j
public class ApiHoldStockController {

	@Autowired
	HoldStockService holdStockService;

	@ApiOperation("分页持仓列表")
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
		IPage<HoldStockInfo> pageModel = holdStockService.selectPage(page, limit, id);
		long total = pageModel.getTotal();
		System.out.println("总页数total = " + total);
		List<HoldStockInfo> records = pageModel.getRecords();
		System.out.println("records = " + records);
		return R.ok().data("total", total).data("rows", records);
	}
	@ApiOperation(value = "获取持仓")
	@GetMapping("getAllHold")
	public R getAllHold(@ApiParam(value = "用户id", required = true)String id){
		HoldStockInfo info = holdStockService.getAllHold(id);
		if(info == null) return R.error().message("获取信息错误");
		return R.ok().data("data", info);
	}
	@ApiOperation(value = "获取持有股票信息")
	@GetMapping("getHoldInfo")
	public R getHoldInfo(@ApiParam(value = "用户id", required = true)String id,
	                     @ApiParam(value = "股票代码", required = true)String code) {
		HoldStockInfo info = holdStockService.getHoldStockInfo(id, code);
		if (info == null) return R.error().message("获取信息错误");
		return R.ok().data("data", info);
	}

	@ApiOperation(value = "增加持仓信息")
	@PostMapping("addHoldInfo")
	public R addHoldInfo(
			@ApiParam(value = "持仓信息", required = true)
			@RequestBody HoldStockInfo stockInfo) {
		boolean flag = holdStockService.addHoldStockInfo(stockInfo);
		if (!flag) {
			return R.error().message("增加错误");
		}
		return R.ok().message("增加成功");
	}

	@ApiOperation(value = "更新持仓信息")
	@PutMapping("updateHoldInfo")
	public R updateHoldInfo(
			@ApiParam(value = "更新的持仓信息", required = true)
			@RequestBody HoldStockChange stockInfo) {
		boolean flag = holdStockService.updateHoldStockInfo(stockInfo);
		if (!flag) {
			return R.error().message("更新错误");
		}
		return R.ok().message("更新成功");
	}

	@ApiOperation(value = "删除持仓信息")
	@DeleteMapping("deleteHoldInfo")
	public R deleteHoldInfo(@ApiParam(value = "用户id", required = true)String id,
	                        @ApiParam(value = "股票代码", required = true)String code) {
		boolean flag = holdStockService.deleteHoldStockInfo(id, code);
		if (!flag) {
			return R.error().message("删除失败");
		}
		return R.ok().message("删除成功");
	}

	@ApiOperation(value = "判断是否有持仓")
	@GetMapping("ifUserHoldThisStock")
	public int ifUserHoldThisStock(@ApiParam(value = "用户id", required = true)String id,
	                        @ApiParam(value = "股票代码", required = true)String code) {
		return holdStockService.ifUserHoldThisStock(id, code);
	}
}
