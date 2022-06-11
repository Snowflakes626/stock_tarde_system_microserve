package com.trade.service.user.service.Impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.trade.service.user.entity.OptionalStock;
import com.trade.service.user.mapper.OptionalStockMapper;
import com.trade.service.user.service.OptionalStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class OptionalStockServiceImpl extends ServiceImpl<OptionalStockMapper, OptionalStock> implements OptionalStockService {

	@Autowired
	OptionalStockMapper mapper;

	private boolean existOptionalStock(String userId, String code) {
		return mapper.existOptionalStock(userId, code) == 0;
	}

	@Override
	public boolean addOptionalStock(String code, String userId, String name) {
		String id = IdWorker.getIdStr();
		Timestamp nowTime = new Timestamp(new Date().getTime());
		// if (!existOptionalStock(userId, stockCode)) return false;
		return mapper.addOptionalStock(id, userId, code, name, nowTime);
	}

	@Override
	public boolean deleteOptionalStock(String stockCode, String userId) {
		return mapper.deleteOptionalStock(userId, stockCode);
	}

	@Override
	public IPage<OptionalStock> selectPage(Long page, Long limit, String userId) {
		QueryWrapper<OptionalStock> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("user_id",userId);
		System.out.println("user_id ===== " + userId);
		queryWrapper.orderByDesc("insert_date");
		Page<OptionalStock> pageParam = new Page<>(page, limit);
		List<OptionalStock> records = baseMapper.selectPageByUserId(pageParam, queryWrapper);
		pageParam.setRecords(records);
		return pageParam;
	}
}
