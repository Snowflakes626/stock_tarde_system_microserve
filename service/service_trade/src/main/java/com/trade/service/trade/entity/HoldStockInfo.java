package com.trade.service.trade.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class HoldStockInfo {
	@TableId(value = "id", type = IdType.ASSIGN_ID)
	private String id;
	private String userId;
	private String stockCode;
	private String stockName;
	private BigDecimal costPrice;
	private int totalNumber;
	private int avlNumber;

	public HoldStockInfo() {
	}

	public HoldStockInfo(String userId, String stockCode, BigDecimal costPrice, int totalNumber, int avlNumber) {
		this.userId = userId;
		this.stockCode = stockCode;
		this.costPrice = costPrice;
		this.totalNumber = totalNumber;
		this.avlNumber = avlNumber;
	}

	public HoldStockInfo(String id, String userId, String stockCode, BigDecimal costPrice, int totalNumber, int avlNumber) {
		this.id = id;
		this.userId = userId;
		this.stockCode = stockCode;
		this.costPrice = costPrice;
		this.totalNumber = totalNumber;
		this.avlNumber = avlNumber;
	}
}
