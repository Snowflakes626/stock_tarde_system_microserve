package com.trade.service.domestic.stock.controller.api;

import ch.qos.logback.core.joran.conditional.ElseAction;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.trade.result.R;
import com.trade.result.ResultCodeEnum;
import com.trade.service.domestic.stock.entity.Stock;
import com.trade.service.domestic.stock.service.StockService;
import com.trade.utils.JwtInfo;
import com.trade.utils.JwtUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;


@CrossOrigin
@RestController
@RequestMapping("/api/stock")
public class ApiStockController {

	@Autowired
	private StockService stockService;

	// @GetMapping("/getIndexKlineInfo")
	// @ResponseBody
	// @ApiOperation(value = "获取指数K线数据")
	// public R getIndexKlineInfo(String ts_code,String type){
	// 	System.out.println(ts_code + "  " + type);
	// 	R result = stockService.getIndexKlineInfo(ts_code,type);
	// 	System.out.println("indexInfo ");
	// 	return result;
	// }

	// @GetMapping("/getStockKlineInfo")
	// @ResponseBody
	// @ApiOperation(value = "获取股票K线数据")
	// public R getStockKlineInfo(String ts_code,String type){
	// 	System.out.println(ts_code + "  " + type);
	// 	R result = stockService.getStockKlineInfo(ts_code,type);
	// 	System.out.println("getStockInfo");
	// 	return result;
	// }

	@GetMapping("/getStockList/{page}/{limit}/{keyword}")
	@ResponseBody
	@ApiOperation(value = "获取股票列表")
	public R getStockList(@ApiParam(value = "当前页码", required = true)
							  @PathVariable Long page,

						  @ApiParam(value = "每页")
							  @PathVariable Long limit,

						  @ApiParam(value = "关键字")
							  @PathVariable String keyword){
		if("all".equals(keyword))
			keyword = "";
		R result = stockService.getStockList(page,limit,keyword);
		System.out.println(result);
		return result;
	}

	@ApiOperation("分页持仓列表")
	@GetMapping("getStockList1/{page}/{limit}/{keyword}")
	public R index(
			@ApiParam(value = "当前页码", required = true)
			@PathVariable Long page,

			@ApiParam(value = "每页")
			@PathVariable Long limit,

			@ApiParam(value = "关键字")
			@PathVariable String keyword
	){
		IPage<Stock> pageModel;
		if(keyword == "") {
			pageModel = stockService.selectPage(page, limit);
		}
		else{
			pageModel = stockService.selectPageByK(page, limit, keyword);
		}
		long total = pageModel.getTotal();
		System.out.println("总页数total = " + total);
		List<Stock> records = pageModel.getRecords();
		System.out.println("records = " + records);
		return R.ok().data("total", total).data("rows", records);
	}

	@GetMapping("/getStockBasicInfo")
	@ResponseBody
	@ApiOperation(value = "获取股票的基础信息")
	public R getStockBasicInfo(String symbol){
		R result = stockService.getStockBasicInfo(symbol);
		System.out.println("getStockBasicInfo");
		return result;
	}

	@GetMapping("/getDetailStock/{ts_code}")
	@ResponseBody
	@ApiOperation(value = "获取查询股票信息")
	public R getQueryStock(@PathVariable String ts_code, HttpServletRequest request){
		String jwtToken = JwtUtils.getMemberIdByJwtToken(request).toString();
		boolean flag = stockService.authUser(jwtToken);
		if (!flag) {
			return R.setResult(ResultCodeEnum.AUTH_ERROR);
		}
		R result = stockService.getDetailStock(ts_code);
		System.out.println("getDetailStock");
		return result;
	}

	@GetMapping("/getIndexKLineInfo/{ts_code}")
	@ResponseBody
	@ApiOperation(value = "获取指数k线")
	public R getIndexKLineInfo(@PathVariable String ts_code, HttpServletRequest request) {
		String jwtToken = JwtUtils.getMemberIdByJwtToken(request).toString();
		boolean flag = stockService.authUser(jwtToken);
		if (!flag) {
			return R.setResult(ResultCodeEnum.AUTH_ERROR);
		}
		R result = stockService.getIndexKlineInfo(ts_code);
		System.out.println("getIndexKLineInfo");
		return result;
	}

	@GetMapping("/getKLineInfo/{ts_code}")
	@ResponseBody
	@ApiOperation(value = "获取指数k线")
	public R getKLineInfo(@PathVariable String ts_code, HttpServletRequest request) {
		String jwtToken = JwtUtils.getMemberIdByJwtToken(request).toString();
		boolean flag = stockService.authUser(jwtToken);
		if (!flag) {
			return R.setResult(ResultCodeEnum.AUTH_ERROR);
		}
		R result = stockService.getKlineInfo(ts_code);
		System.out.println("getIndexKLineInfo");
		return result;
	}
}
