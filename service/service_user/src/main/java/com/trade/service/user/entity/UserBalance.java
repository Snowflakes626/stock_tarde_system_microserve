package com.trade.service.user.entity;

import lombok.Data;

import java.math.BigDecimal;


@Data
public class UserBalance {
	private BigDecimal avlBalance;
	private BigDecimal totalBalance;
}
