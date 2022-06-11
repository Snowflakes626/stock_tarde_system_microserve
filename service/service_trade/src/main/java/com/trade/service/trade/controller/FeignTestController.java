package com.trade.service.trade.controller;

import com.trade.result.R;
import com.trade.service.trade.entity.BalanceChange;
import com.trade.service.trade.entity.BalanceFromBank;
import com.trade.service.trade.entity.HoldStockChange;
import com.trade.service.trade.entity.HoldStockInfo;
import com.trade.service.trade.feign.UserInfoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
public class FeignTestController {

	@Autowired
	UserInfoService userInfoService;

	@GetMapping("/testGetHoldStockInfoFeign")
	@ResponseBody
	@ApiOperation(value = "获取持仓信息")
	public R getHoldStockInfo(@RequestParam("id") String id, @RequestParam("code") String code){
		R result = userInfoService.getHoldStockInfo(id, code);
		if (result == null) return R.error();
		return result;
	}

	@PostMapping("/testAddHoldStockInfoFeign")
	@ApiOperation(value = "增加持仓信息")
	public R addHoldStockInfo(@RequestBody HoldStockInfo holdStockInfo) {
		return userInfoService.addHoldStockInfo(holdStockInfo);
	}

	@PutMapping("/testUpdateHoldStockInfoFeign")
	@ApiOperation(value = "更新持仓信息")
	public R updateHoldStockInfo(@RequestBody HoldStockChange holdStockChange) {
		return userInfoService.updateHoldStockInfo(holdStockChange);
	}

	@DeleteMapping("/testDeleteHoldStockInfoFeign")
	@ApiOperation(value = "删除持仓信息")
	public R updateHoldStockInfo(@RequestParam("id") String id, @RequestParam("code") String code) {
		return userInfoService.deleteHoldStockInfo(id, code);
	}

	@GetMapping("/testGetBalanceFeign")
	@ResponseBody
	@ApiOperation(value = "获取用户余额信息")
	public R getBalanceInfo(@RequestParam("id") String id){
		R result = userInfoService.getUserBalance(id);
		if (result == null) return R.error();
		return result;
	}

	@PutMapping("/testUpdateBalanceFeign")
	@ApiOperation(value = "更新余额")
	public R updateBalance(@RequestBody BalanceChange balanceChange) {
		return userInfoService.updateBalance(balanceChange);
	}

	@PostMapping("/testUpdateBalanceFromBankFeign")
	@ApiOperation(value = "银行转账")
	public R updateBalanceFromBank(@RequestBody BalanceFromBank balance) {
		return userInfoService.updateBalanceByBank(balance);
	}

	@GetMapping("/testIfUserHoldStock")
	@ApiOperation(value = "判断用户是否持有股票")
	public int ifUserHoldThisStock(@RequestParam("id")String id, @RequestParam("code")String code) {
		return userInfoService.ifUserHoldThisStock(id, code);
	}
}
