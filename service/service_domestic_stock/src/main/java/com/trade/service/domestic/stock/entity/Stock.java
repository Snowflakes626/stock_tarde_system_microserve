package com.trade.service.domestic.stock.entity;


import lombok.Data;

import java.sql.Date;

@Data
public class Stock {
	private String tsCode;
	private String symbol;
	private String name;
	private String area;
	private String industry;
	private String market;
	private Date listDate;
}

