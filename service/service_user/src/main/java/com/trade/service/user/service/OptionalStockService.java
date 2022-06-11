package com.trade.service.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.trade.service.user.entity.OptionalStock;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public interface OptionalStockService extends IService<OptionalStock> {
	boolean addOptionalStock(String code, String userId, String name);
	boolean deleteOptionalStock(String code, String userId);
	IPage<OptionalStock> selectPage(Long page, Long limit, String id);
}
