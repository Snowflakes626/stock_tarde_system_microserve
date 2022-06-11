package com.trade.service.user.entity.vo;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
public class LoginVo implements Serializable {
	private static final long serialVersionUID = 1L;
	@NotEmpty(message = "手机号或邮箱不能为空")
	private String userInfo;
	private String password;
}
