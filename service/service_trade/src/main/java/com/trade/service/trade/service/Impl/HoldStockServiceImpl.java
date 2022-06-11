package com.trade.service.trade.service.Impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.trade.service.trade.entity.HoldStockInfo;
import com.trade.service.trade.mapper.HoldStockMapper;
import com.trade.service.trade.service.HoldStockService;

public class HoldStockServiceImpl extends ServiceImpl<HoldStockMapper, HoldStockInfo> implements HoldStockService {
	@Override
	public HoldStockInfo getHoldStockInfo(String userId, String ts_code) {
		return null;
	}
}
