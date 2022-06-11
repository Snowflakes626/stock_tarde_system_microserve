package com.trade.service.trade.service.Impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.trade.result.R;
import com.trade.service.trade.entity.*;
import com.trade.service.trade.feign.CurStockInfoService;
import com.trade.service.trade.feign.UserInfoService;
import com.trade.service.trade.mapper.TradeMapper;
import com.trade.service.trade.service.TradeService;
import com.trade.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.TimeUnit;


@Service
public class TradeServiceImpl extends ServiceImpl<TradeMapper, TradeOrder> implements TradeService {

	@Autowired
	private TradeService tradeService;

	@Autowired
	private TradeMapper tradeMapper;

	@Autowired
	private CurStockInfoService curStockInfoService;

	@Autowired
	UserInfoService userInfoService;

	@Autowired(required = false)
	private RedisTemplate<String, Object> redisTemplate;

	private final String REDIS_ALL_KEYS = "all-keys";

	private boolean listSet(String key, String value) {
		redisTemplate.opsForList().rightPush(key, value);
		redisTemplate.expire(key, 6*60*60, TimeUnit.SECONDS);
		return true;
	}

	private void sSetAndTime(String key, Object... values) {
		redisTemplate.opsForSet().add(key, values);
		redisTemplate.expire(key, 6*60*60, TimeUnit.SECONDS);
	}

	private boolean lFindValue(String key, Object value) {
		List<Object> list = redisTemplate.opsForList().range(key, 0, -1);
		for (Object o : list) {
			if (o.equals(value)) return true;
		}
		return false;
	}

	private boolean lRemove(String key, long count, Object value) {
		redisTemplate.opsForList().remove(key, count, value);
		return true;
	}

	private HoldStockInfo getHoldStockInfoFromJson(R r) {
		LinkedHashMap<String, Object> data = (LinkedHashMap<String, Object>) r.getData().get("data");
		String id = (String)data.get("id");
		String userId = (String)data.get("userId");
		String stockCode = (String)data.get("stockCode");
		double tempCostPrice = (double) data.get("costPrice");
		BigDecimal costPrice = new BigDecimal(tempCostPrice);
		int totalNumber = (int) data.get("totalNumber");
		int avlNumber = (int) data.get("avlNumber");
		return new HoldStockInfo(id, userId, stockCode, costPrice, totalNumber, avlNumber);
	}

	@Override
	public R entrustOrder(String userId, String code, BigDecimal price, int stockNum, String type) {
		long curTime = System.currentTimeMillis();
		String orderId = IdWorker.getIdStr();
		// String stockName = tradeService.getNameByCode();
		Timestamp entrustTime = new Timestamp(new Date().getTime());
		// Timestamp entrustTime = TimeUtils.getNowTimestamp();
		String redisKey = code + ":" + price + ":" + type;
		String redisValue = orderId;
		R balanceR = userInfoService.getUserBalance(userId);
		System.out.println();
		LinkedHashMap<String, Object> balanceData = (LinkedHashMap<String, Object>) balanceR.getData().get("data");
		double avlBalance = (double)balanceData.get("avlBalance");
		double totalBalance = (double)balanceData.get("totalBalance");
		UserBalance balance = new UserBalance(new BigDecimal(avlBalance) , new BigDecimal(totalBalance));
		// UserBalance balance = (UserBalance) balanceR.getData().get("data");
		R holdStockInfoR = userInfoService.getHoldStockInfo(userId, code);
		// LinkedHashMap<String, Object> holdStockData = (LinkedHashMap<String, Object>) holdStockInfoR.getData().get("data");
		HoldStockInfo holdStockInfo = getHoldStockInfoFromJson(holdStockInfoR);
		// HoldStockInfo holdStockInfo = (HoldStockInfo) holdStockInfoR.getData().get("data");
		if (balance == null || holdStockInfo == null)
			return R.error().message("信息获取错误");


		// 购买
		if (((type.equals(TradeType.ACTION_BUY)
				&& balance.getAvlBalance().compareTo(price.multiply(BigDecimal.valueOf(stockNum))) > -1)
			|| (type.equals(TradeType.ACTION_SELL)
				&& holdStockInfo.getAvlNumber() >= stockNum))
			&& tradeMapper.entrustOrder(orderId, userId, code, entrustTime, price, stockNum, type, TradeType.STATION_ENTRUST)
			&& this.listSet(redisKey, redisValue)) {


			if (type.equals(TradeType.ACTION_BUY)) {
				BalanceChange balanceChange = new BalanceChange(userId,
						price.multiply(BigDecimal.valueOf(stockNum)).negate(),
						BigDecimal.valueOf(0));
				userInfoService.updateBalance(balanceChange);
			} else {
				HoldStockChange holdStockChange = new HoldStockChange(userId,
						code, -stockNum, 0, BigDecimal.valueOf(0));
				userInfoService.updateHoldStockInfo(holdStockChange);
			}
			this.sSetAndTime("all-keys", redisKey);
		} else {
			return R.error().message("挂单失败");
		}
		return R.ok().message("挂单成功");
	}

	@Override
	public IPage<TradeOrder> getUserHistoryTradeOrderList(Long page, Long limit, String userId) {
		QueryWrapper<TradeOrder> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("user_id", userId);
		queryWrapper.orderByDesc("order_time");
		Page<TradeOrder> pageParam = new Page<>(page, limit);
		List<TradeOrder> records = baseMapper.selectHistoryPageByUserId(pageParam, queryWrapper);
		pageParam.setRecords(records);
		return pageParam;
	}

	@Override
	public IPage<TradeOrder> getUserTodayTradeOrderList(Long page, Long limit, String userId) {
		QueryWrapper<TradeOrder> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("user_id", userId);
		Timestamp nowTime = new Timestamp(new Date().getTime());
		queryWrapper.apply("TO_DAYS(order_time) = TO_DAYS("+nowTime+")");
		queryWrapper.orderByDesc("order_time");
		Page<TradeOrder> pageParam = new Page<>(page, limit);
		List<TradeOrder> records = baseMapper.selectHistoryPageByUserId(pageParam, queryWrapper);
		pageParam.setRecords(records);
		return pageParam;
	}

	@Override
	public R cancelOrder(String userId, String orderId) {
		Timestamp nowTime = new Timestamp(new Date().getTime());
		TradeOrder tradeOrder = tradeMapper.getUserTradeOrderInfo(orderId);
		String key = tradeOrder.getStockCode()+":"+tradeOrder.getEntrustPrice()+":"+tradeOrder.getType();
		if (this.lFindValue(key, orderId)) {
			// 如果redis中存在key，则删除订单
			if (tradeMapper.cancelOrder(orderId, userId, nowTime, TradeType.STATION_CANCEL)
					&& lRemove(key, 1, orderId)) {
				if (tradeOrder.getType().equals(TradeType.ACTION_BUY)) {
					BigDecimal price = tradeOrder.getEntrustPrice();
					BigDecimal number = BigDecimal.valueOf(tradeOrder.getStockNum());
					BigDecimal totalChangeBalance = BigDecimal.valueOf(0);
					BalanceChange balanceChange = new BalanceChange(userId, price.multiply(number), totalChangeBalance);
					userInfoService.updateBalance(balanceChange);
				} else if((tradeOrder.getType().equals(TradeType.ACTION_SELL))) {
					HoldStockChange holdStockChange = new HoldStockChange(userId, tradeOrder.getStockCode(),
							tradeOrder.getStockNum(), 0, BigDecimal.valueOf(0));
					userInfoService.updateHoldStockInfo(holdStockChange);
				} else {
					return R.error().message("错误状态");
				}
				if (redisTemplate.opsForList().size(key) == 1) {
					redisTemplate.opsForSet().remove("all-keys", key);
				}
			} else {
				return R.error().message("删除的订单不存在");
			}
		} else {
			return R.error().message("撤单失败，确认该委托单是否已成交");
		}
		return R.ok().message("撤单成功");
	}

	@Override
	public R updateBalanceByBank(String userId, String updateValue) {
		BalanceFromBank balance = new BalanceFromBank(userId, updateValue);
		return userInfoService.updateBalanceByBank(balance);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void dealingOrder() {
		List<String> stockCodes = new ArrayList<>();
		List<BigDecimal> stockPrice = new ArrayList<>();
		List<String> stockType = new ArrayList<>();
		List<String> keys = new ArrayList<>();
		String[] temp;              // temp 分割code price type
		String orderId;
		String userId;
		String stockCode;
		int number;
		int totalNumber;            //持仓总数
		BigDecimal entrustPrice;    //买入或卖出价
		BigDecimal costPrice;       //成本价
		BigDecimal changePrice;     //成本价的变化价格部分，买单为正、增加成本，卖单为负、减少成本
		BigDecimal nowPrice;        //股票现价
		BigDecimal donePrice;       //成交价格
		BigDecimal fixAvlBalance;   //如果出现成交价格不等于委托价的情况，则要对用户的可用资金进行加减修补
		Timestamp nowTime;
		List<Object> orderIdList;   //某个股票某个价位的所有订单ID编号
		TradeOrder tradeOrder;
		HoldStockInfo holdStockInfo;  //用户个股的持仓信息

		// 从redis中得到所有的key(code:price:type)
		Set<Object> allStocks = redisTemplate.opsForSet().members("all-keys");
		if (!allStocks.isEmpty()) {
			// 遍历key来得到所有订单所包含的code,price,type
			for (Object obj : allStocks) {
				temp = obj.toString().split(":");
				keys.add(obj.toString());
				stockCodes.add(temp[0]);
				stockPrice.add(new BigDecimal(temp[1]));
				stockType.add(temp[2]);
			}
			// 通过腾讯股票接口api获得当时的成交价格
			String msg = curStockInfoService.getStocksInfo(String.join(",", stockCodes));
			String[] stockArr = msg.split(";");
			for (int i = 0; i < stockArr.length - 1; i++) {
				temp = stockArr[i].split("~");
				// 当前的价格
				nowPrice = new BigDecimal(temp[3]);
				// 委托价格
				entrustPrice = stockPrice.get(i);

				//如果该挂单是买单，并且买入价大于或等于现价，即买入单成交
				if (stockType.get(i).equals(TradeType.ACTION_BUY) && entrustPrice.compareTo(nowPrice) >= 0) {
					orderIdList = redisTemplate.opsForList().range(keys.get(i), 0, -1);
					nowTime = new Timestamp(new Date().getTime());

					//交易有两种情况，下单立即成交和挂着委托单等待,这里如果股票现价小于买入价则使用现价成交，不然使用买入价成交
					if (entrustPrice.compareTo(nowPrice) > 0) {
						donePrice = nowPrice;
					} else {
						donePrice = entrustPrice;
					}

					//遍历该股票所有委托单，由orderID去数据库中查询，得到该价位的委托单
					assert orderIdList != null;
					for (Object obj : orderIdList) {
						orderId = obj.toString();
						tradeOrder = tradeMapper.getUserTradeOrderInfo(orderId);
						userId = tradeOrder.getUserId();
						stockCode = tradeOrder.getStockCode();
						number = tradeOrder.getStockNum();
						fixAvlBalance = entrustPrice.subtract(donePrice).multiply(BigDecimal.valueOf(number));

						//更新委托单状态为已成交
						tradeMapper.doneOrder(orderId, tradeOrder.getUserId(), nowTime, donePrice, TradeType.STATION_DONE);

						//更新用户总资金，减去购买股票的资金,并且根据情况修补用户可用资金
						BalanceChange balanceChange = new BalanceChange(userId, fixAvlBalance, donePrice.multiply(BigDecimal.valueOf(number)).negate());
						userInfoService.updateBalance(balanceChange);

						//如果该用户已持有该股票，则更新用户持仓总数量和成本价，若没有则插入用户持仓信息
						System.out.println("buy: userId: " + userId + "  orderId:" + orderId);
						System.out.println(userInfoService.ifUserHoldThisStock(userId, orderId));
						if (userInfoService.ifUserHoldThisStock(userId, orderId) != 0) {
							R result = userInfoService.getHoldStockInfo(userId, stockCode);
							// holdStockInfo = (HoldStockInfo) result.getData().get("data");
							holdStockInfo = getHoldStockInfoFromJson(result);
							costPrice = holdStockInfo.getCostPrice();
							totalNumber = holdStockInfo.getTotalNumber();

							//  计算成本价的变化部分
							//  新成本 - 旧成本
							//  (成本价*总数量 + 成交价*成交数量)/(总数量 + 买入数量) - 成本价
							changePrice = ((costPrice.multiply(BigDecimal.valueOf(totalNumber)).add(donePrice.multiply(BigDecimal.valueOf(number))))
									.divide(BigDecimal.valueOf(totalNumber + number), 2, BigDecimal.ROUND_HALF_UP)).subtract(costPrice);

							HoldStockChange holdStockChange = new HoldStockChange(userId, stockCode, 0, number, changePrice);
							userInfoService.updateHoldStockInfo(holdStockChange);
						} else {
							HoldStockInfo tempHoldStockInfo = new HoldStockInfo(userId, stockCode, donePrice, number, 0);
							userInfoService.addHoldStockInfo(tempHoldStockInfo);
						}

						//删去redis相应的委托单缓存
						redisTemplate.opsForList().remove(keys.get(i), 1, orderId);
					}

					//删去redis缓存中allKeys关于该股票该价格的缓存
					redisTemplate.opsForSet().remove(REDIS_ALL_KEYS, keys.get(i));
				} else if (stockType.get(i).equals(TradeType.ACTION_SELL) && entrustPrice.compareTo(nowPrice) <= 0) {
					//如果该挂单是卖单，并且卖出价小于现价，即卖出单成交

					orderIdList = redisTemplate.opsForList().range(keys.get(i), 0, -1);
					nowTime = TimeUtils.getNowTimestamp();

					//交易有两种情况，下单立即成交和挂着委托单等待,这里如果股票现价大于卖出价则使用现价成交，不然使用卖出价成交
					if (entrustPrice.compareTo(nowPrice) < 0) {
						donePrice = nowPrice;
					} else {
						donePrice = entrustPrice;
					}

					//遍历该股票该价位的所有委托单
					assert orderIdList != null;
					for (Object obj : orderIdList) {
						orderId = obj.toString();
						tradeOrder = tradeMapper.getUserTradeOrderInfo(orderId);
						userId = tradeOrder.getUserId();
						stockCode = tradeOrder.getStockCode();
						number = tradeOrder.getStockNum();

						R result = userInfoService.getHoldStockInfo(userId, stockCode);
						// holdStockInfo = (HoldStockInfo) result.getData().get("data");
						holdStockInfo = getHoldStockInfoFromJson(result);
						costPrice = holdStockInfo.getCostPrice();
						totalNumber = holdStockInfo.getTotalNumber();

						tradeMapper.doneOrder(orderId, tradeOrder.getUserId(), nowTime, donePrice, TradeType.STATION_DONE);

						//更新用户总资金，加上卖出股票的资金
						BalanceChange balanceChange = new BalanceChange(userId, BigDecimal.valueOf(0), donePrice.multiply(BigDecimal.valueOf(number)));
						userInfoService.updateBalance(balanceChange);

						System.out.println("sell: userId: " + userId + "  orderId:" + orderId);
						System.out.println(userInfoService.ifUserHoldThisStock(userId, orderId));
						//如果该用户持有的该股票总数量不等于卖出的数量，则保留仓位信息并更新股票总数量和成本价，若等于则说明该股票已清仓，应直接清空该股票的仓位信息
						if (holdStockInfo.getTotalNumber() != number) {

							//  计算成本价的变化部分
							//  新成本 - 旧成本
							//  (成本价*总数量 - 卖出价*卖出数量)/(总数量 - 卖出数量) - 成本价
							changePrice = ((costPrice.multiply(BigDecimal.valueOf(totalNumber)).subtract(donePrice.multiply(BigDecimal.valueOf(number))))
									.divide(BigDecimal.valueOf(totalNumber - number), 2, BigDecimal.ROUND_HALF_UP)).subtract(costPrice);

							HoldStockChange holdStockChange = new HoldStockChange(userId, stockCode, 0, -number, changePrice);
							userInfoService.updateHoldStockInfo(holdStockChange);
						} else {
							userInfoService.deleteHoldStockInfo(userId, stockCode);
						}

						//删去redis相应的委托单缓存
						redisTemplate.opsForList().remove(keys.get(i), 1, orderId);
					}

					//删去redis缓存中allKeys关于该股票该价格的缓存
					redisTemplate.opsForSet().remove(REDIS_ALL_KEYS, keys.get(i));
				} else {
					//TODO 出现既不是买单也不是卖单的情况，虽说委托单经过基本不可能出现该错误，不过可以留着等功能拓展
				}
			}
		}
	}
}
