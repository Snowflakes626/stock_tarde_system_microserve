package com.trade.service.trade.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trade.service.trade.entity.TradeOrder;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Mapper
public interface TradeMapper extends BaseMapper<TradeOrder> {
	@Insert("insert ignore into trade_order(order_id,user_id,stock_code,order_time,entrust_price,stock_num,type,station) " +
			"values(#{order_id},#{user_id},#{stock_code},#{entrust_time},#{entrust_price},#{stock_num},#{type},#{station})")
	boolean entrustOrder(@Param("order_id") String orderId,
	                     @Param("user_id") String userId,
	                     @Param("stock_code") String code,
	                     @Param("entrust_time") Timestamp entrust,
	                     @Param("entrust_price") BigDecimal entrustPrice,
	                     @Param("stock_num") int number,
	                     @Param("type") String type,
	                     @Param("station") int station);

	List<TradeOrder> selectHistoryPageByUserId(
			Page<TradeOrder> pageParam,
			@Param(Constants.WRAPPER)QueryWrapper<TradeOrder> queryWrapper);

	@Select("select * from trade_order where order_id = #{order_id}")
	TradeOrder getUserTradeOrderInfo(@Param("order_id") String orderId);


	@Update("update trade_order set order_time = #{order_time}, station = #{station} where order_id = #{order_id} and user_id = #{user_id}")
	boolean cancelOrder(@Param("order_id") String order_id,
	                    @Param("user_id") String userId,
	                    @Param("order_time") Timestamp cancelTime,
	                    @Param("station") int station);

	@Update("update trade_order set order_time = #{order_time}, station = #{station}, done_price = #{done_price}" +
			" where order_id = #{order_id} and user_id = #{user_id}")
	boolean doneOrder(@Param("order_id") String order_id,
	                  @Param("user_id") String userId,
	                  @Param("order_time") Timestamp doneTime,
					  @Param("done_price") BigDecimal donePrice,
	                  @Param("station") int station);

}
