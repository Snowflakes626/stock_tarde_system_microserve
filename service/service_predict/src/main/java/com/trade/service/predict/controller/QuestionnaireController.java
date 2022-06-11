package com.trade.service.predict.controller;

import cn.hutool.core.collection.ListUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/predict/questionnaire")
public class QuestionnaireController {

	@GetMapping("/get")
	public List getQuestionnaire() throws IOException {
		JSONObject json;
		String jsonPath = "/questionnaire.json";
		InputStream config = getClass().getResourceAsStream(jsonPath);
		if (config == null) {
			throw new RuntimeException("读取文件失败");
		} else {
			 json = JSON.parseObject(config, JSONObject.class) ;
		}
		return ListUtil.toList(json.getJSONArray("data"));
	}
}
