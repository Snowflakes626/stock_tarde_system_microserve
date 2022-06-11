package com.trade.service.user.entity.vo;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;


@Data
public class RegisterVo implements Serializable {
	private static final long serialVersionUID = 1L;
	@NotEmpty(message = "昵称不能为空")
	private String username;
	private String phone;
	@Email(message = "邮箱格式不对")
	private String email;
	@NotEmpty(message = "密码不能为空")
	private String password;
	@NotEmpty(message = "验证码不能为空")
	private String code;
}
