package com.trade.service.trade.feign.Impl;


import com.trade.result.R;
import com.trade.service.trade.entity.BalanceChange;
import com.trade.service.trade.entity.BalanceFromBank;
import com.trade.service.trade.entity.HoldStockChange;
import com.trade.service.trade.entity.HoldStockInfo;
import com.trade.service.trade.feign.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserInfoServiceImpl implements UserInfoService {
	@Override
	public R getUserBalance(String userId) {
		log.info("熔断保护");
		return R.error().message("调用超时");
	}

	@Override
	public R getHoldStockInfo(String id, String code) {
		log.info("熔断保护");
		return R.error().message("调用超时");
	}

	@Override
	public R addHoldStockInfo(HoldStockInfo holdStockInfo) {
		log.info("熔断保护");
		return R.error().message("调用超时");
	}

	@Override
	public R updateBalance(BalanceChange balanceChange) {
		log.info("熔断保护");
		return R.error().message("调用超时");
	}

	@Override
	public R updateHoldStockInfo(HoldStockChange holdStockChange) {
		log.info("熔断保护");
		return R.error().message("调用超时");
	}

	@Override
	public R deleteHoldStockInfo(String id, String code) {
		log.info("熔断保护");
		return R.error().message("调用超时");
	}

	@Override
	public R updateBalanceByBank(BalanceFromBank balance) {
		log.info("熔断保护");
		return R.error().message("调用超时");
	}

	@Override
	public int ifUserHoldThisStock(String id, String code) {
		log.info("熔断保护");
		return 0;
	}

	// @Override
	// public R getHoldStockInfo(String userId, String ts_code) {
	// 	log.info("熔断保护");
	// 	return R.error().message("调用超时");
	// }
}
