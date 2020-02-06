package com.behaviosec.chat;

import com.behaviosec.isdk.entities.Report;

public class Message {
	private String sessionID;
	private String message;
	private String bdata;
	private String authorization;
	private Report report;
	private String response;
	private int authorizedBefore = 0;
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
	@Override
	public String toString() {
		return "Message [sessionID=" + sessionID + ", message=" + message + ", bdata=" + bdata + ", authorization="
				+ authorization + ", report=" + report + ", response=" + response + ", authorizedBefore="
				+ authorizedBefore + "]";
	}
}
