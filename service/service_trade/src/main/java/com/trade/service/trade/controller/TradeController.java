package com.trade.service.trade.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.trade.result.R;
import com.trade.service.trade.entity.BalanceFromBank;
import com.trade.service.trade.entity.CancelOrder;
import com.trade.service.trade.entity.EntrustOrder;
import com.trade.service.trade.entity.TradeOrder;
import com.trade.service.trade.feign.UserInfoService;
import com.trade.service.trade.service.TradeService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/trade")
@EnableScheduling
public class TradeController {

	@Autowired
	private TradeService tradeService;

	@Autowired
	UserInfoService userInfoService;

	@PostMapping("entrustOrder")
	@ApiOperation(value = "挂单")
	public R entrustOrder(@RequestBody EntrustOrder order) {
		String userId = order.getUserId();
		String code = order.getCode();
		BigDecimal price = order.getPrice();
		int stockNum = order.getStockNum();
		String type = order.getType();
		return tradeService.entrustOrder(userId, code, price, stockNum, type);
	}

	@PostMapping("cancelOrder")
	@ApiOperation(value = "撤单")
	public R cancelOrder(@RequestBody CancelOrder order) {
		String userId = order.getUserId();
		String orderId = order.getOrderId();
		return tradeService.cancelOrder(userId, orderId);
	}

	@GetMapping("/getHistoryTradeOrderList/{page}/{limit}")
	@ApiOperation(value = "查询用户历史交易单列表")
	public R getHistoryTradeOrderList(String userId,
	                                  @PathVariable Long page,
	                                  @PathVariable Long limit) {
		IPage<TradeOrder> pageModel = tradeService.getUserHistoryTradeOrderList(page, limit, userId);
		long total = pageModel.getTotal();
		List<TradeOrder> records = pageModel.getRecords();
		return R.ok().data("total", total).data("rows", records);
	}

	@GetMapping("/getTodayTradeOrderList/{page}/{limit}")
	@ApiOperation(value = "查询用户今日交易单列表")
	public R getTodayTradeOrderList(String userId,
	                                  @PathVariable Long page,
	                                  @PathVariable Long limit) {
		IPage<TradeOrder> pageModel = tradeService.getUserTodayTradeOrderList(page, limit, userId);
		long total = pageModel.getTotal();
		List<TradeOrder> records = pageModel.getRecords();
		return R.ok().data("total", total).data("rows", records);
	}

	@PostMapping("/UpdateBalanceFromBankFeign")
	@ApiOperation(value = "银行转账")
	public R updateBalanceFromBank(@RequestBody BalanceFromBank balance) {
		return userInfoService.updateBalanceByBank(balance);
	}
}
