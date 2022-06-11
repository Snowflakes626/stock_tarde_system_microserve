package com.trade.service.domestic.stock.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trade.service.domestic.stock.entity.KlineData;
import com.trade.service.domestic.stock.entity.Stock;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface StockMapper extends BaseMapper<Stock> {

	@Select("select * from index_daily_qfq where ts_code = #{ts_code}")
	ArrayList<KlineData> getDailyIndex(@Param("ts_code")String ts_code);
	@Select("select ts_code,trade_date,close,open,high,low,pre_close,close_chg,convert((close-pre_close)*100/pre_close,decimal(10,2)) AS pct_chg," +
			"(vol/100) AS vol,(amount/1000) AS amount  from index_weekly_qfq where ts_code = #{ts_code}")
	ArrayList<KlineData> getWeeklyIndex(@Param("ts_code") String ts_code);

	@Select("select ts_code,trade_date,close,open,high,low,pre_close,close_chg,convert((close-pre_close)*100/pre_close,decimal(10,2)) AS pct_chg," +
			"(vol/100) AS vol,(amount/1000) AS amount  from index_monthly_qfq where ts_code = #{ts_code}")
	ArrayList<KlineData> getMonthlyIndex(@Param("ts_code") String ts_code);

	@Select("(select * from index_daily_qfq where ts_code = #{ts_code} ORDER BY trade_date DESC limit #{limitNum}) ORDER BY trade_date ASC")
	ArrayList<KlineData> getWeeklyIndexPlus(@Param("ts_code") String ts_code, @Param("limitNum") int limitNum);

	@Select("select * from index_daily_qfq where date_format(trade_date,'%Y-%m') = date_format(now(),'%Y-%m') and ts_code = #{ts_code}")
	ArrayList<KlineData> getMonthlyIndexPlus(@Param("ts_code") String ts_code);

	@Select("select ts_code,trade_date,close,open,high,low,pre_close,close_chg,pct_chg,vol,amount from stock_all_daily_wfq where ts_code = #{ts_code}")
	ArrayList<KlineData> getDailyStock(@Param("ts_code") String ts_code);

	@Select("select ts_code,trade_date,close,open,high,low,pre_close,close_chg,convert((close-pre_close)*100/pre_close,decimal(10,2)) AS pct_chg," +
			"(vol/100) AS vol,(amount/1000) AS amount from stock_all_weekly_wfq where ts_code = #{ts_code}")
	ArrayList<KlineData> getWeeklyStock(@Param("ts_code") String ts_code);

	@Select("select ts_code,trade_date,close,open,high,low,pre_close,close_chg,convert((close-pre_close)*100/pre_close,decimal(10,2)) AS pct_chg," +
			"(vol/100) AS vol,(amount/1000) AS amount from stock_all_monthly_wfq where ts_code = #{ts_code}")
	ArrayList<KlineData> getMonthlyStock(@Param("ts_code") String ts_code);

	@Select("(select ts_code,trade_date,close,open,high,low,pre_close,close_chg,pct_chg,vol,amount from stock_all_daily_wfq " +
			"where ts_code = #{ts_code} ORDER BY trade_date DESC limit #{limitNum}) ORDER BY trade_date ASC")
	ArrayList<KlineData> getWeeklyStockPlus(@Param("ts_code") String ts_code, @Param("limitNum") int limitNum);

	@Select("select ts_code,trade_date,close,open,high,low,pre_close,close_chg,pct_chg,vol,amount from stock_all_daily_wfq " +
			"where date_format(trade_date,'%Y-%m') = date_format(now(),'%Y-%m') and ts_code = #{ts_code}")
	ArrayList<KlineData> getMonthlyStockPlus(@Param("ts_code") String ts_code);

	@Select("select * from stock_basic where ts_code like concat('%',#{query},'%') OR name like concat('%',#{query},'%') ORDER BY ts_code ASC limit 100")
	ArrayList<Stock> getQueryStock(@Param("query") String query);

	@Select("select count(*) from stock_basic")
	int getStockTotalNum();

	@Select("select count(*) from stock_basic where ts_code like concat('%',#{keyword},'%') OR name like concat('%',#{keyword},'%')")
	int getStockTotalNumByKeyword(@Param("keyword") String keyword);

	@Select("select * from stock_basic where symbol = #{symbol}")
	Stock getStockBasicInfo(@Param("symbol") String symbol);

	@Select("select * from stock_basic where #{ts_code} = ts_code")
	Stock getDetailStock(@Param("ts_code") String ts_code);

	@Select("select * from index_daily where #{ts_code} = ts_code order by trade_date desc limit 50 ")
	ArrayList<KlineData> getIndexKlineInfo(@Param("ts_code")String ts_code);

	@Select("select * from stock_daily where #{ts_code} = ts_code order by trade_date desc limit 50")
	ArrayList<KlineData> getKlineInfo(@Param("ts_code")String ts_code);

	List<Stock> selectPage(
			Page<Stock> pageParam,
			@Param(Constants.WRAPPER) QueryWrapper<Stock> queryWrapper);

	@Select("select ts_code, name, market, area, list_date from stock_basic where ts_code like concat('%',#{keyword},'%') OR name like concat('%',#{keyword},'%') " +
			"ORDER BY ts_code ASC limit ${(page-1)*limit} , #{limit}")
	ArrayList<Stock> getStockListByKeyword(@Param("page") long page, @Param("limit") long limit, @Param("keyword") String keyword);


	@Select("select ts_code, name, market, area, list_date from stock_basic ORDER BY ts_code ASC limit ${(page-1)*limit} , #{limit}")
	ArrayList<Stock> getStockList(@Param("page") long page, @Param("limit") long limit);

}

