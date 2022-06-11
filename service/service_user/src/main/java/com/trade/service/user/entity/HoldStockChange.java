package com.trade.service.user.entity;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class HoldStockChange {
	private String userId;
	private String stockCode;
	private int avlChangeNumber;
	private int totalChangeNumber;
	BigDecimal changePrice;
}
