package com.trade.service.trade.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.trade.service.trade.entity.HoldStockInfo;

public interface HoldStockService extends IService<HoldStockInfo>  {
	HoldStockInfo getHoldStockInfo(String userId, String ts_code);
}
