package com.trade.service.trade.entity;

import lombok.Data;

import java.math.BigDecimal;


@Data
public class UserBalance {
	private BigDecimal avlBalance;
	private BigDecimal totalBalance;

	public UserBalance(BigDecimal avlBalance, BigDecimal totalBalance) {
		this.avlBalance = avlBalance;
		this.totalBalance = totalBalance;
	}

	public UserBalance() {
	}
}
