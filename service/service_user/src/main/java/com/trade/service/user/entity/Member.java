package com.trade.service.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user_info")
@ApiModel(value="Member对象", description="会员表")
public class Member implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "id")
	@TableId(value = "id", type = IdType.ASSIGN_ID)
	private String id;

	@ApiModelProperty(value = "手机号")
	private String phone;

	@ApiModelProperty(value = "密码")
	private String password;

	@ApiModelProperty(value = "邮箱")
	private String email;

	@ApiModelProperty(value = "昵称")
	private String username;

	@ApiModelProperty(value = "权限")
	private String roles;

	@ApiModelProperty(value = "性别 男，女")
	private String sex;

	@ApiModelProperty(value = "年龄")
	private Integer age;

	@ApiModelProperty(value = "用户头像")
	private String avatar;

	@ApiModelProperty(value = "用户签名")
	private String sign;

	@ApiModelProperty(value = "审核人员")
	private String checker;

	@ApiModelProperty(value = "可用金额")
	private Double avlBalance;

	@ApiModelProperty(value = "总金额")
	private Double totalBalance;

	@ApiModelProperty(value = "是否禁用 1（true）已禁用，  0（false）未禁用")
	@TableField("is_disabled")
	private Boolean disabled;

	@ApiModelProperty(value = "创建时间", example = "2020-01-01 8:00:00")
	@TableField(fill = FieldFill.INSERT)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime createTime;

	@ApiModelProperty(value = "更新时间", example = "2020-01-01 8:00:00")
	@TableField(fill = FieldFill.INSERT_UPDATE)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime updateTime;
}
