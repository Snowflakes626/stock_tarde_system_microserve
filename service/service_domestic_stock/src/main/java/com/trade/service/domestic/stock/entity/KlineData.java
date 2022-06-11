package com.trade.service.domestic.stock.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class KlineData {
 private String id;
 private String tsCode;
 private String tradeDate;
 private BigDecimal close;
 private BigDecimal open;
 private BigDecimal high;
 private BigDecimal low;
 private BigDecimal preClose;
 private BigDecimal closeChg;
 private BigDecimal pctChg;
 private BigDecimal vol;
 private BigDecimal amount;
}