package com.trade.service.user.service.Impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.trade.service.user.entity.HoldStockChange;
import com.trade.service.user.entity.HoldStockInfo;
import com.trade.service.user.entity.OptionalStock;
import com.trade.service.user.mapper.HoldStockMapper;
import com.trade.service.user.service.HoldStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class HoldStockServiceImpl extends ServiceImpl<HoldStockMapper, HoldStockInfo> implements HoldStockService {

	@Autowired
	HoldStockMapper holdStockMapper;

	@Override
	public HoldStockInfo getAllHold(String userId){
		return baseMapper.getAllHold(userId);
	}

	@Override
	public HoldStockInfo getHoldStockInfo(String userId, String ts_code) {
		return baseMapper.getHoldStockInfo(userId, ts_code);
	}

	@Override
	public boolean addHoldStockInfo(HoldStockInfo holdStockInfo) {
		String id = IdWorker.getIdStr();
		String userId = holdStockInfo.getUserId();
		String stockCode = holdStockInfo.getStockCode();
		BigDecimal costPrice = holdStockInfo.getCostPrice();
		int totalNumber = holdStockInfo.getTotalNumber();
		int avlNumber = holdStockInfo.getAvlNumber();
		if (baseMapper.getHoldStockInfo(userId, stockCode) != null) return false;
		return holdStockMapper.addHoldStockInfo(id, userId, stockCode, costPrice, totalNumber, avlNumber);
	}

	@Override
	public boolean updateHoldStockInfo(HoldStockChange holdStockChange) {
		String id = IdWorker.getIdStr();
		String userId = holdStockChange.getUserId();
		String stockCode = holdStockChange.getStockCode();
		BigDecimal changePrice = holdStockChange.getChangePrice();
		int totalChangeNumber = holdStockChange.getTotalChangeNumber();
		int avlChangeNumber = holdStockChange.getAvlChangeNumber();
		return holdStockMapper.updateHoldStockInfo(id, userId, stockCode, changePrice, totalChangeNumber, avlChangeNumber);
	}

	@Override
	public boolean deleteHoldStockInfo(String id, String code) {
		return holdStockMapper.deleteHoldStockInfo(id, code);
	}

	@Override
	public int ifUserHoldThisStock(String id, String code) {
		return holdStockMapper.ifUserHoldThisStock(id, code);
	}


	@Override
	public IPage<HoldStockInfo> selectPage(Long page, Long limit, String userId) {
		QueryWrapper<HoldStockInfo> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("user_id",userId);
		queryWrapper.orderByDesc("total_number");
		Page<HoldStockInfo> pageParam = new Page<>(page, limit);
		List<HoldStockInfo> records = baseMapper.selectPageByUserId(pageParam, queryWrapper);
		pageParam.setRecords(records);
		return pageParam;
	}
}