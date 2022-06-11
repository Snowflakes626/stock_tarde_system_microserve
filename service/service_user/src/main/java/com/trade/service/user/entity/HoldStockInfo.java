package com.trade.service.user.entity;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class HoldStockInfo {
	private String id;
	private String userId;
	private String stockCode;
	private String stockName;
	private BigDecimal costPrice;
	private int totalNumber;
	private int avlNumber;
}
