package com.trade.service.trade.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BalanceChange {
	private String userId;
	private BigDecimal avlChangeBalance;
	private BigDecimal totalChangeBalance;

	public BalanceChange(String userId, BigDecimal avlChangeBalance, BigDecimal totalChangeBalance) {
		this.userId = userId;
		this.avlChangeBalance = avlChangeBalance;
		this.totalChangeBalance = totalChangeBalance;
	}

	public BalanceChange() {
	}
}
