package com.trade.service.trade.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(url = "http://sqt.gtimg.cn", name = "curInfoUrl")
public interface CurStockInfoService {

	@GetMapping(value = "/", consumes = "application/x-www-form-urlencoded;charset=UTF-8")
	String getStocksInfo(@RequestParam("q") String code);
}
