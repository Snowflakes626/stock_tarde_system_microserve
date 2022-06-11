package com.trade.service.user.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trade.service.user.entity.HoldStockInfo;
import com.trade.service.user.entity.OptionalStock;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface HoldStockMapper extends BaseMapper<HoldStockInfo> {
	HoldStockInfo getAllHold(String userId);
	HoldStockInfo getHoldStockInfo(String userId, String code);


	@Insert("insert into hold_shares(id, user_id, stock_code, cost_price, total_number, avl_number)" +
			"values(#{id}, #{user_id}, #{stock_code}, #{cost_price}, #{total_number}, #{avl_number})")
	boolean addHoldStockInfo(@Param("id") String id,
							 @Param("user_id") String userId,
	                         @Param("stock_code") String stockCode,
	                         @Param("cost_price") BigDecimal costPrice,
	                         @Param("total_number") int totalNumber,
	                         @Param("avl_number") int avlNumber);

	@Update("update hold_shares set total_number = total_number + #{total_change_number}, avl_number = avl_number + #{avl_change_number}, " +
			"cost_price = cost_price + #{change_price} " +
			"where user_id = #{user_id} and stock_code = #{stock_code}")
	boolean updateHoldStockInfo(@Param("id") String id,
	                            @Param("user_id") String userId,
	                            @Param("stock_code") String stockCode,
	                            @Param("change_price") BigDecimal costPrice,
	                            @Param("total_change_number") int totalNumber,
	                            @Param("avl_change_number") int avlNumber);

	@Delete("delete from hold_shares where user_id = #{user_id} and stock_code = #{stock_code}")
	boolean deleteHoldStockInfo(@Param("user_id") String userId, @Param("stock_code") String stockCode);

	@Select("select count(*) from hold_shares where user_id = #{user_id} and stock_code = #{code}")
	int ifUserHoldThisStock(@Param("user_id")String userId, @Param("code")String stockCode);
	List<HoldStockInfo> selectPageByUserId(
			Page<HoldStockInfo> pageParam,
			@Param(Constants.WRAPPER) QueryWrapper<HoldStockInfo> queryWrapper);
}
