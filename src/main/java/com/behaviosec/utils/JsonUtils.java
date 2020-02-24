package com.behaviosec.utils;

import com.behaviosec.chat.DialogFlowController.risk;
import com.behaviosec.config.Constants;
import com.behaviosec.isdk.entities.Report;

public class JsonUtils {
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(JsonUtils.class);
	
	public static String createJsonResponse(String fulfillmentText, Report r, risk risk, String feedbackMode) {
		String score = "";
		
		// Check if we need to include the privilege
		if (feedbackMode.equalsIgnoreCase(Constants.FEEDBACK_MODE_BOTH) || feedbackMode.equalsIgnoreCase(Constants.FEEDBACK_MODE_PRIVILEGE)) {
			score = risk.toString() + ":";
		}
				
		if (feedbackMode.equalsIgnoreCase(Constants.FEEDBACK_MODE_BOTH) || feedbackMode.equalsIgnoreCase(Constants.FEEDBACK_MODE_SCORE)) {
			if (r!= null) {
				double scoreDouble = r.getScore();
				double riskDouble = r.getRisk();
				double confidenceDouble = r.getConfidence();
				String s = String.format("%.0f", scoreDouble);
				String r1 = String.format("%.0f", riskDouble);
				String c = String.format("%.0f", confidenceDouble);
				score += " (s)" + s + " (c)" + c + " (r)" + r1 + " : ";
			} else {
				score += " invalid credentials or no report - ";
			}
		}
		
		String fulfillmentTextJson = "{\r\n" + 
				"  \"output\": {\r\n" + 
				"    \"generic\": [\r\n" + 
				"      {\r\n" + 
				"        \"response_type\": \"text\",\r\n" + 
				"        \"text\": \"" + score + fulfillmentText + "\"\r\n" + 
				"      }\r\n" + 
				"    ],\r\n" + 
				"    \"intents\": [\r\n" + 
				"      {\r\n" + 
				"        \"intent\": \"General_Conversation-Greetings\",\r\n" + 
				"        \"confidence\": 0.5048123836517334\r\n" + 
				"      }\r\n" + 
				"    ],\r\n" + 
				"    \"entities\": []\r\n" + 
				"  }\r\n" + 
				"}";
		log.debug("fulfillmentTextJson " + fulfillmentTextJson.replaceAll("\r", "").replaceAll("\n", ""));
		return fulfillmentTextJson;
	}
}
