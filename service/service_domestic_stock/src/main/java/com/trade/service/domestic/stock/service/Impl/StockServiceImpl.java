package com.trade.service.domestic.stock.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.trade.result.R;
import com.trade.result.ResultCodeEnum;
import com.trade.service.domestic.stock.entity.KlineData;
import com.trade.service.domestic.stock.entity.Stock;
import com.trade.service.domestic.stock.mapper.StockMapper;
import com.trade.service.domestic.stock.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StockServiceImpl extends ServiceImpl<StockMapper, Stock> implements StockService {

	@Autowired(required = false)
	private RedisTemplate<String, String> redisTemplate;


	// @Override
	// public R getIndexKlineInfo(String ts_code, String type) {
	// 	// 得到当前时间，并进行时间规范化处理
	// 	int limitNum = TimeUtils.getCurrSunNum();
	// 	int hours = TimeUtils.getHour(new java.util.Date());
	// 	if (hours > 9 && hours < 18) {
	// 		limitNum = limitNum - 1;
	// 	}
	// 	ArrayList<KlineData> plusIndex;
	// 	ArrayList<KlineData> list;
	// 	// 判断将要描绘的k线的状态，并到数据库中进行查询
	// 	switch (type) {
	// 		case "Daily":
	// 			list = baseMapper.getDailyIndex(ts_code);
	// 			break;
	// 		case "Weekly":
	// 			list = baseMapper.getWeeklyIndex(ts_code);
	// 			if (limitNum > 1 && limitNum < 6) {
	// 				plusIndex = baseMapper.getWeeklyIndexPlus(ts_code, limitNum);
	// 				list.add(getRequireData(plusIndex, ts_code));
	// 			}
	// 			break;
	// 		case "Monthly":
	// 			list = baseMapper.getMonthlyIndex(ts_code);
	// 			plusIndex = baseMapper.getMonthlyIndexPlus(ts_code);
	// 			if (!plusIndex.isEmpty()) {
	// 				list.add(getRequireData(plusIndex, ts_code));
	// 			}
	// 			break;
	// 		default:
	// 			list = null;
	// 			break;
	// 	}
	// 	if (list == null) {
	// 		return R.setResult(ResultCodeEnum.GET_INDEX_KLINE_FAILED);
	// 	}
	// 	return R.ok().data("data", list);
	// }

	// @Override
	// public R getStockKlineInfo(String ts_code,String type){
	// 	String msg = "";
	// 	int limitNum = TimeUtils.getCurrSunNum();
	// 	int hours = TimeUtils.getHour(new java.util.Date());
	// 	if (hours > 9 && hours < 18) {
	// 		limitNum = limitNum - 1;
	// 	}
	//
	// 	ArrayList<KlineData> plusStock;
	// 	ArrayList<KlineData> list;
	// 	switch (type) {
	// 		case "Daily":
	// 			list = baseMapper.getDailyStock(ts_code);
	// 			break;
	// 		case "Weekly":
	// 			list = baseMapper.getWeeklyStock(ts_code);
	// 			if (limitNum > 1 && limitNum < 6) { //如果当前时间是星期二到星期五中的一天
	// 				plusStock = baseMapper.getWeeklyStockPlus(ts_code, limitNum);
	// 				list.add(getRequireData(plusStock, ts_code));
	// 			}
	// 			break;
	// 		case "Monthly":
	// 			list = baseMapper.getMonthlyStock(ts_code);
	// 			plusStock = baseMapper.getMonthlyStockPlus(ts_code);
	// 			if (!plusStock.isEmpty()) {
	// 				list.add(getRequireData(plusStock, ts_code));
	// 			}
	// 			break;
	// 		default:
	// 			list = null;
	// 			break;
	// 	}
	// 	if (list == null) {
	// 		return R.setResult(ResultCodeEnum.GET_KLINE_FAILED);
	// 	}
	// 	return R.ok().data("data", list);
	// }

	@Override
	public R getQueryStock(String query) {
		ArrayList<Stock> list = baseMapper.getQueryStock(query);
		if (list == null) {
			return R.setResult(ResultCodeEnum.GET_STOCK_INFO_FAILED);
		}
		return R.ok().data("data", list);
	}



	@Override
	public R getStockList(long page, long limit, String keyword) {
		ArrayList<Stock> list;
		int total;
		System.out.println("limit = " + limit);
		if (keyword == "") {
			list = baseMapper.getStockList(page, limit);
			total = baseMapper.getStockTotalNum();
			if (list == null) {
				return R.setResult(ResultCodeEnum.GET_STOCK_LIST_INFO_FAILED);
			}
		} else {
			list = baseMapper.getStockListByKeyword(page, limit, keyword);
			total = baseMapper.getStockTotalNumByKeyword(keyword);
			if (list == null) {
				return R.setResult(ResultCodeEnum.GET_STOCK_LIST_INFO_FAILED);
			}
		}
		return R.ok().data("total", total).data("rows", list);
	}

	@Override
	public R getStockBasicInfo(String symbol) {
		Stock stock = baseMapper.getStockBasicInfo(symbol);
		if (stock == null) {
			return R.setResult(ResultCodeEnum.GET_STOCK_INFO_FAILED);
		}
		return R.ok().data("data", stock);
	}

	// 根据日线的数据，计算周线、月线所需的当前周、当前月数据
	// private KlineData getRequireData(ArrayList<KlineData> plusData, String ts_code) {
	// 	BigDecimal high = null;
	// 	BigDecimal low = null;
	// 	BigDecimal vol = null;
	// 	BigDecimal amount = null;
	// 	KlineData plus = new KlineData();
	// 	for (int i = 0; i < plusData.size(); i++) {
	// 		if (i == 0) {
	// 			plus.setTs_code(ts_code);
	// 			plus.setOpen(plusData.get(i).getOpen());
	// 			plus.setPre_close(plusData.get(i).getPre_close());
	// 			high = plusData.get(i).getHigh();
	// 			low = plusData.get(i).getLow();
	// 			vol = plusData.get(i).getVol();
	// 			amount = plusData.get(i).getAmount();
	// 			continue;
	// 		}
	// 		if (i == plusData.size() - 1) {
	// 			plus.setId(plusData.get(i).getId());
	// 			plus.setClose(plusData.get(i).getClose());
	// 			plus.setTrade_date(plusData.get(i).getTrade_date());
	// 		}
	// 		if (high.compareTo(plusData.get(i).getHigh()) == -1) {
	// 			high = plusData.get(i).getHigh();
	// 		}
	// 		if (low.compareTo(plusData.get(i).getLow()) == 1) {
	// 			low = plusData.get(i).getLow();
	// 		}
	// 		vol = vol.add(plusData.get(i).getVol());
	// 		amount = amount.add(plusData.get(i).getAmount());
	// 	}
	// 	plus.setHigh(high);
	// 	plus.setLow(low);
	// 	plus.setVol(vol);
	// 	plus.setAmount(amount);
	// 	plus.setClose_chg(plus.getOpen().subtract(plus.getClose()));
	// 	plus.setPct_chg(plus.getOpen().divide(plus.getClose(), 2, RoundingMode.HALF_UP));
	//
	// 	return plus;
	// }

	@Override
	public IPage<Stock> selectPage(Long page, Long limit) {
		QueryWrapper<Stock> queryWrapper = new QueryWrapper<>();
		queryWrapper.orderByAsc("ts_code");
		Page<Stock> pageParam = new Page<>(page, limit);
		List<Stock> records = baseMapper.selectPage(pageParam, queryWrapper);
		pageParam.setRecords(records);
		return pageParam;
	}

	@Override
	public IPage<Stock> selectPageByK(Long page, Long limit, String keyword) {
		QueryWrapper<Stock> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("user_id",keyword);
		queryWrapper.orderByDesc("total_number");
		Page<Stock> pageParam = new Page<>(page, limit);
		List<Stock> records = baseMapper.selectPage(pageParam, queryWrapper);
		pageParam.setRecords(records);
		return pageParam;
	}

	@Override
	public R getDetailStock(String ts_code) {
		Stock result = baseMapper.getDetailStock(ts_code);
		if (result == null) return R.setResult(ResultCodeEnum.GET_STOCK_INFO_FAILED);
		return R.ok().data("info", result);
	}

	@Override
	public R getIndexKlineInfo(String ts_code) {
		ArrayList<KlineData> result = baseMapper.getIndexKlineInfo(ts_code);
		if (result == null) return R.setResult(ResultCodeEnum.GET_INDEX_KLINE_FAILED);
		return R.ok().data("kline", result);
	}

	@Override
	public R getKlineInfo(String ts_code) {
		ArrayList<KlineData> result = baseMapper.getKlineInfo(ts_code);
		if (result == null) return R.setResult(ResultCodeEnum.GET_KLINE_FAILED);
		return R.ok().data("kline", result);
	}

	@Override
	public boolean authUser(String jwtToken) {
		String result = redisTemplate.opsForValue().get(jwtToken);
		return result != null;
	}
}
