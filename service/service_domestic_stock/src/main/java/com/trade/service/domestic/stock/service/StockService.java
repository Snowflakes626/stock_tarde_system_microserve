package com.trade.service.domestic.stock.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.trade.result.R;
import com.trade.service.domestic.stock.entity.Stock;
import org.springframework.stereotype.Repository;

@Repository
public interface StockService {

	// R getStockKlineInfo(String ts_code, String type);

	IPage<Stock> selectPage(Long page, Long limit);
	IPage<Stock> selectPageByK(Long page, Long limit, String keyword);
	R getQueryStock(String query);
	R getStockList(long page,long limit,String keyword);
	R getStockBasicInfo(String symbol);

	R getDetailStock(String ts_code);
	R getIndexKlineInfo(String ts_code);
	R getKlineInfo(String ts_code);

	boolean authUser(String jwtToken);

}
