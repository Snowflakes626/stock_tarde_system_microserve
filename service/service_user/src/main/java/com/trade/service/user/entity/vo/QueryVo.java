package com.trade.service.user.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class QueryVo implements Serializable {
	private static final long serialVersionID = 1L;

	@ApiModelProperty(value = "id")
	private String Id;

	@ApiModelProperty(value = "手机号")
	private String phone;

	@ApiModelProperty(value = "邮箱")
	private String email;

	@ApiModelProperty(value = "昵称")
	private String username;

	@ApiModelProperty(value = "性别 男，女")
	private String sex;

	@ApiModelProperty(value = "年龄")
	private Integer age;

	@ApiModelProperty(value = "用户头像")
	private String avatar;

	@ApiModelProperty(value = "用户签名")
	private String sign;

	@ApiModelProperty(value = "可用金额")
	private Double avlBalance;

	@ApiModelProperty(value = "总金额")
	private Double totalBalance;
}
