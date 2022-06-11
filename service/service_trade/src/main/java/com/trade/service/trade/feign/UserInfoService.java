package com.trade.service.trade.feign;


import com.trade.result.R;
import com.trade.service.trade.entity.BalanceChange;
import com.trade.service.trade.entity.BalanceFromBank;
import com.trade.service.trade.entity.HoldStockChange;
import com.trade.service.trade.entity.HoldStockInfo;
import com.trade.service.trade.feign.Impl.UserInfoServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@Service
@FeignClient(value = "service-user", fallback = UserInfoServiceImpl.class)
//
public interface UserInfoService {
	@GetMapping("/api/ucenter/member/getBalance")
	R getUserBalance(@RequestParam("id") String id);

	@PutMapping("/api/ucenter/member/updateBalance")
	R updateBalance(@RequestBody BalanceChange balanceChange);

	@PostMapping("/api/ucenter/member/updateBalanceByBank")
	R updateBalanceByBank(@RequestBody BalanceFromBank balance);

	@GetMapping("/api/ucenter/hold-stock/getHoldInfo")
	R getHoldStockInfo(@RequestParam("id")String id, @RequestParam("code")String code);

	@PostMapping("/api/ucenter/hold-stock/addHoldInfo")
	R addHoldStockInfo(@RequestBody HoldStockInfo holdStockInfo);

	@PutMapping("/api/ucenter/hold-stock/updateHoldInfo")
	R updateHoldStockInfo(@RequestBody HoldStockChange holdStockChange);

	@DeleteMapping("/api/ucenter/hold-stock/deleteHoldInfo")
	R deleteHoldStockInfo(@RequestParam("id")String id, @RequestParam("code")String code);

	@GetMapping("/api/ucenter/hold-stock/ifUserHoldThisStock")
	int ifUserHoldThisStock(@RequestParam("id")String id, @RequestParam("code")String code);
}
