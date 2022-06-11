package com.trade.service.trade.entity;

import lombok.Data;

@Data
public class CancelOrder {
	private String userId;
	private String orderId;
}
