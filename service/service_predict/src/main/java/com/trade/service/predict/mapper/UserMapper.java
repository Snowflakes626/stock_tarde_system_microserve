package com.trade.service.predict.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.trade.service.predict.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper extends BaseMapper<User> {

	@Insert("insert into score(user_id, score) values(#{user_id}, #{score})")
	boolean insertInfo(@Param("user_id") String user_id, @Param("score") Double score);
}
