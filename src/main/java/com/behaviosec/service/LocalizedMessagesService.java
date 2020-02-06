package com.behaviosec.service;

import java.util.ResourceBundle;

public class LocalizedMessagesService {
	public static String getMessage(String messageKey) {
		ResourceBundle bundle = ResourceBundle.getBundle("messages");
		String message = bundle.getString(messageKey);
		return message;
		
	}

}
