package com.trade.service.trade.entity;

import lombok.Data;

@Data
public class BalanceFromBank {
	private String userId;
	private String updateValue;

	public BalanceFromBank(String userId, String updateValue) {
		this.userId = userId;
		this.updateValue = updateValue;
	}

	public BalanceFromBank() {
	}
}
