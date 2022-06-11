package com.trade.service.user.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BalanceChange {
	private String userId;
	private BigDecimal avlChangeBalance;
	private BigDecimal totalChangeBalance;
}
