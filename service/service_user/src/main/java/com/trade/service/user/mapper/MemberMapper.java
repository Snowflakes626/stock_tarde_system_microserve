package com.trade.service.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.trade.service.user.entity.Member;
import com.trade.service.user.entity.UserBalance;
import com.trade.service.user.entity.vo.OptionalStockVo;
import com.trade.service.user.entity.vo.QueryVo;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.ArrayList;

@Mapper
public interface MemberMapper extends BaseMapper<Member> {
	Integer selectRegisterNumByDay(String day);
	QueryVo selectQueryVo(String id);
	UserBalance selectBalance(String id);
	boolean updateBalance(String id, BigDecimal avlChange, BigDecimal totalChange);
	boolean updateBalanceByBank(String id, BigDecimal updateValue);

	@Select("select code from user_optional where user_id = #{userID}")
	ArrayList<String> getUserAllOptionalCode(@Param("username") String userID);

	@Select("select count(*) from user_optional where user_id = #{userID}")
	int getUserOptionalTotalNum(@Param("username") String userID);

	@Insert("insert into user_optional(user_id,code,name) values(#{userID},#{code},#{name})")
	boolean addUserOptional(@Param("username") String userID, @Param("code") String code, @Param("name") String name);

	@Delete("delete from user_optional where user_id = #{userID} and code = #{code}")
	boolean deleteUserOptional(@Param("username") String userID, @Param("code") String code);

	@Select("select code,name from user_optional where user_id = #{userID} ORDER BY id ASC limit ${(page-1)*limit} , #{limit}")
	ArrayList<OptionalStockVo> getUserOptionalList(@Param("page") int page, @Param("limit") int limit, @Param("username") String userID);

	@Select("select count(*) from user_optional where user_id = #{userID} and code = #{code}")
	int ifExistStock(@Param("username") String userID, @Param("code") String code);
}