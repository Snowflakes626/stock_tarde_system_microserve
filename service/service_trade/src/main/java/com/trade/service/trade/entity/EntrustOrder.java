package com.trade.service.trade.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class EntrustOrder {
	private String userId;
	private String code;
	private BigDecimal price;
	private int stockNum;
	private String type;

	public EntrustOrder(String userId, String code, BigDecimal price, int stockNum, String type) {
		this.userId = userId;
		this.code = code;
		this.price = price;
		this.stockNum = stockNum;
		this.type = type;
	}

	public EntrustOrder() {
	}
}
