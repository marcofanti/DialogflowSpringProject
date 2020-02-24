package com.behaviosec.utils;

import java.io.IOException;

import org.springframework.boot.json.JacksonJsonParser;

import com.behaviosec.chat.Message;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class ParseUtils {
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ParseUtils.class);

	public static final JacksonJsonParser parse = new JacksonJsonParser();
	public static final JsonFactory factory = new JsonFactory();

	public static void parseEmail(String bdata, Message message) {
		String chatemail = null;
		String chatname = null;
		String feedbackMode = null;
		String trainingMode = null;

		JsonParser parser = null;
		try {
			parser = factory.createParser(bdata);
		} catch (JsonParseException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		while (!parser.isClosed()) {
			JsonToken jsonToken = null;
			try {
				jsonToken = parser.nextToken();
				if (JsonToken.FIELD_NAME.equals(jsonToken)) {
					String fieldName = parser.getCurrentName();
					System.out.println(fieldName + " " + parser.getValueAsString());

					jsonToken = parser.nextToken();

					if ("chatemail".equalsIgnoreCase(fieldName)) {
						chatemail = parser.getValueAsString();
						if (chatemail == null || chatemail.equals("null")) {
							chatemail = null;
							log.debug("chatemail is null");
						} else {
							log.debug("chatemail is set " + chatemail);
						}
					}
					if ("chatname".equalsIgnoreCase(fieldName)) {
						chatname = parser.getValueAsString();
						if (chatname == null || chatname.equals("null")) {
							chatname = null;
							log.debug("chatname is null");
						} else {
							log.debug("chatname is set " + chatname);
						}
					}
					if ("feedbackMode".equalsIgnoreCase(fieldName)) {
						feedbackMode = parser.getValueAsString();
						if (feedbackMode == null || feedbackMode.equals("null")) {
							feedbackMode = null;
							log.debug("feedbackMode is null");
						} else {
							log.debug("feedbackMode is set " + feedbackMode);
						}
					}
					if ("trainingMode".equalsIgnoreCase(fieldName)) {
						trainingMode = parser.getValueAsString();
						if (trainingMode == null || trainingMode.equals("null")) {
							trainingMode = null;
							log.debug("trainingMode is null");
						} else {
							log.debug("trainingMode is set " + trainingMode);
						}
					}
				}
			} catch (IOException e) {
			}
		}

		if (chatemail != null && chatname != null) {
			message.setEmail(chatemail);
			message.setName(chatname);
		}
		if (feedbackMode != null ) {
			message.setFeedbackMode(feedbackMode);
		}
		if (trainingMode != null ) {
			message.setTrainingMode(trainingMode);
		}
	}

	public static String parseTimingData(String bdata, String userAgent) {
		if (bdata == null || bdata.length() == 0) {
			return userAgent;
		}

		JsonParser parser = null;
		try {
			parser = factory.createParser(bdata);
		} catch (JsonParseException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		while (!parser.isClosed()) {
			JsonToken jsonToken = null;
			try {
				jsonToken = parser.nextToken();
				if (JsonToken.FIELD_NAME.equals(jsonToken)) {
					String fieldName = parser.getCurrentName();
//					log.debug("fieldName " + fieldName );
					jsonToken = parser.nextToken();
					if ("userAgent".equalsIgnoreCase(fieldName)) {
						userAgent = parser.getValueAsString();
						log.debug(fieldName + " = " + userAgent);
						break;
					}
				}
			} catch (IOException e) {
			}
		}
		return userAgent;
	}
}
