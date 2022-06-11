package com.trade.service.trade.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.trade.result.R;
import com.trade.service.trade.entity.TradeOrder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public interface TradeService extends IService<TradeOrder> {
	R entrustOrder(String userId, String code, BigDecimal price, int stockNum, String type);
	IPage<TradeOrder> getUserHistoryTradeOrderList(Long page, Long limit, String userId);
	IPage<TradeOrder> getUserTodayTradeOrderList(Long page, Long limit, String userId);
	R cancelOrder(String userId, String orderId);
	R updateBalanceByBank(String userId, String updateValue);
	void dealingOrder();
}
