package com.behaviosec.chat;

import java.io.IOException;

import com.behaviosec.isdk.entities.Report;
import com.behaviosec.utils.ParseUtils;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class Message {
	private String sessionID;
	private String message;
	private String oldMessage;
	private String bdata;
	private String authorization;
	private Report report;
	private String response;
	private String name = "MarcoiOS";
	private String email = "mfantichatios@behaviosec.com";
	private int authorizedBefore = 0;
	private String feedbackMode = com.behaviosec.config.Constants.FEEDBACK_MODE_BOTH;
	private String trainingMode = com.behaviosec.config.Constants.TRAINING_MODE_FALSE;
	private DialogFlowController.risk risk = DialogFlowController.risk.low;
	
	private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass().getName());
	
	
	public Message() {
		this.sessionID = "";
		this.message = "";
		this.authorizedBefore = 0;
		log.info("Setting sessionID to \"\" and authorizedBefore to 0");
	}
	
	public Message(String sessionID) {
		super();
		this.sessionID = sessionID;
		this.message = "";
		this.authorizedBefore = 0;
		log.info("Setting sessionID to " + sessionID + " and authorizedBefore to 0");
	}
	
	public String getSessionID() {
		return sessionID;
	}
	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getBdata() {
		return bdata;
	}
	public void setBdata(String bdata) {
		this.bdata = bdata;
	}
	public String getAuthorization() {
		return authorization;
	}
	public void setAuthorization(String authorization) {
		this.authorization = authorization;
	}
	public Report getReport() {
		return report;
	}
	public void setReport(Report report) {
		this.report = report;
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	public int getAuthorizedBefore() {
		return authorizedBefore;
	}
	public void setAuthorizedBefore(int authorizedBefore) {
		this.authorizedBefore = authorizedBefore;
	}
	
	public DialogFlowController.risk getRisk() {
		return risk;
	}

	public void setRisk(DialogFlowController.risk risk) {
		this.risk = risk;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	// Set the email and display name based on info from app
	public void setEmail() {
		if (bdata != null && bdata.length() > 0) {
			ParseUtils.parseEmail(bdata, this);
		} 
	}	
	
	public String getOldMessage() {
		return oldMessage;
	}

	public void setOldMessage(String oldMessage) {
		this.oldMessage = oldMessage;
	}

	public String getFeedbackMode() {
		return feedbackMode;
	}

	public void setFeedbackMode(String feedbackMode) {
		this.feedbackMode = feedbackMode;
	}

	public String getTrainingMode() {
		return trainingMode;
	}

	public void setTrainingMode(String trainingMode) {
		this.trainingMode = trainingMode;
	}
	
	public void copy(Message anotherMessage) {
		email = anotherMessage.getEmail();
		name = anotherMessage.getName();
		risk = anotherMessage.getRisk();
		feedbackMode = anotherMessage.getFeedbackMode();
		trainingMode = anotherMessage.getTrainingMode();
		report = anotherMessage.getReport();
		sessionID = anotherMessage.getSessionID();
	}

	public void parseInput(String originalBody) {
		log.debug("input = " + originalBody);
		if (originalBody == null || originalBody.length() == 0) {
			return;
		}
		
		JsonParser parser = null;
		try {
			parser = ParseUtils.factory.createParser(originalBody.toString());
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
					System.out.println(fieldName + parser.getValueAsString());

					jsonToken = parser.nextToken();

					if ("bdata".equals(fieldName)) {
						System.out.println(fieldName + parser.getValueAsString());
						bdata = parser.getValueAsString();
						if (bdata == null || bdata.equals("null")) {
							bdata = null;
							log.debug("bdata is null");
						} else {
							log.debug("bdata is set: " + bdata.replaceAll("\\r\\n|\\r|\\n", " "));
							if (bdata.length() > 0) {
								if (bdata.startsWith("{")) {
									ParseUtils.parseEmail(bdata, this);
								}
							}
						}
					}
					if ("text".equals(fieldName)) {
						System.out.println(fieldName + parser.getValueAsString());
						String text = parser.getValueAsString();
						if (text == null || text.equals("null")) {
							text = null;
							log.debug("text is null");
						} else {
							log.debug("text is set: " + text);
						}
						message = text;
					}
				}
			} catch (IOException e) {
				log.error(e.getMessage());
			}
		}
	}

	@Override
	public String toString() {
		return "Message [sessionID=" + sessionID + ", message=" + message + ", oldMessage=" + oldMessage + ", bdata="
				+ bdata + ", authorization=" + authorization + ", report=" + report + ", response=" + response
				+ ", name=" + name + ", email=" + email + ", authorizedBefore=" + authorizedBefore + ", feedbackMode="
				+ feedbackMode + ", trainingMode=" + trainingMode + ", risk=" + risk + "]";
	}
}
