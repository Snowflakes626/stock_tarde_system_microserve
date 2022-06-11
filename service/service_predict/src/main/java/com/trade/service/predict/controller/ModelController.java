package com.trade.service.predict.controller;

import com.trade.result.R;
import com.alibaba.fastjson.JSON;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/predict/feign")
public class ModelController {
	@Resource
	private RestTemplate restTemplate;

	@GetMapping("data")
	public R getForObject(String data, Integer predictDate) {
		Map<String, Object> params = new HashMap<>(2);
		params.put("data", data);
		params.put("predictDate", predictDate);
		String url = "http://127.0.0.1:5000/";
		String result = restTemplate.postForObject(url, params, String.class);
		return R.ok().data("values", JSON.parseObject(result));
	}
}
