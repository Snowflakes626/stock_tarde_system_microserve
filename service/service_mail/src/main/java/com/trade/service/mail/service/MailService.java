package com.trade.service.mail.service;

public interface MailService {
	void sendCode(String email, String checkCode, String template) throws Exception;
}
