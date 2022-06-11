package com.trade.service.user.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trade.service.user.entity.OptionalStock;
import org.apache.ibatis.annotations.*;

import java.sql.Timestamp;
import java.util.List;

@Mapper
public interface OptionalStockMapper  extends BaseMapper<OptionalStock> {

	// @Select("select * from user_optional where user_id = #{userId} and code = #{code}")
	Integer existOptionalStock(String userId, String code);

	@Insert("insert into user_optional(id, user_id, code, name, insert_date)" +
			"values(#{id}, #{userId}, #{code}, #{name}, #{now})")
	boolean addOptionalStock(String id, String userId, String code, String name, Timestamp now);

	@Delete("delete from user_optional where user_id = #{userId} and code = #{code}")
	boolean deleteOptionalStock(String userId, String code);

	List<OptionalStock> selectPageByUserId(
			Page<OptionalStock> pageParam,
			@Param(Constants.WRAPPER) QueryWrapper<OptionalStock> queryWrapper);
}
