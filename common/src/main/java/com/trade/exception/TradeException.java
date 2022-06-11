package com.trade.exception;

import com.trade.result.ResultCodeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@Data
public class TradeException extends RuntimeException{
	//状态码
	private Integer code;

	/**
	 * 接受状态码和消息
	 * @param code
	 * @param message
	 */
	public TradeException(Integer code, String message) {
		super(message);
		this.setCode(code);
	}

	/**
	 * 接收枚举类型
	 * @param resultCodeEnum
	 */
	public TradeException(ResultCodeEnum resultCodeEnum) {
		super(resultCodeEnum.getMessage());
		this.setCode(resultCodeEnum.getCode());
	}

	@Override
	public String toString() {
		return "CollegeException{" +
				"code=" + code +
				", message=" + this.getMessage() +
				'}';
	}
}
