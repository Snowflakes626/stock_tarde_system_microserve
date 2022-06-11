package com.trade.service.trade.entity;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class HoldStockChange {
	private String userId;
	private String stockCode;
	private int avlChangeNumber;
	private int totalChangeNumber;
	BigDecimal changePrice;

	public HoldStockChange() {
	}

	public HoldStockChange(String userId, String stockCode, int avlChangeNumber, int totalChangeNumber, BigDecimal changePrice) {
		this.userId = userId;
		this.stockCode = stockCode;
		this.avlChangeNumber = avlChangeNumber;
		this.totalChangeNumber = totalChangeNumber;
		this.changePrice = changePrice;
	}
}
