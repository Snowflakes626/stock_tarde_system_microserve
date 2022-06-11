package com.trade.service.trade.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class TradeOrder {
	@TableId(type = IdType.ASSIGN_ID)
	private String orderId;

	private String userId;
	private String stockCode;
	private String stockName;
	private Timestamp orderTime;
	private BigDecimal entrustPrice;
	private BigDecimal donePrice;
	private int stockNum;
	private String type;
	private int station;
}
