package com.trade.service.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.trade.service.user.entity.HoldStockChange;
import com.trade.service.user.entity.HoldStockInfo;
import com.trade.service.user.entity.OptionalStock;
import org.springframework.stereotype.Service;

@Service
public interface HoldStockService extends IService<HoldStockInfo>  {
	HoldStockInfo getAllHold(String id);
	HoldStockInfo getHoldStockInfo(String userId, String ts_code);
	boolean addHoldStockInfo(HoldStockInfo holdStockInfo);
	boolean updateHoldStockInfo(HoldStockChange holdStockChange);
	boolean deleteHoldStockInfo(String id, String code);
	int ifUserHoldThisStock(String id, String code);
	IPage<HoldStockInfo> selectPage(Long page, Long limit, String id);

}
