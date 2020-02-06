package com.behaviosec.isdk.entities;

import com.behaviosec.isdk.config.BehavioSecResponsetException;
import com.behaviosec.isdk.config.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class Response {
    private int responseCode;
    private Report report = null;
    private int errorID = 0;
    private String message = "OK";
    private String path;
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private String reponseString = "";
    private long responseTime = -1;
    public long getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(long responseTime) {
        this.responseTime = responseTime;
    }



    public Response() {
    }

    public boolean hasReport(){
        return this.report != null;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public int getErrorID() {
        return errorID;
    }

    public void setErrorID(int errorID) {
        this.errorID = errorID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public void addError(String error){
        String[] errorList = error.substring(1, error.length()-1).split(",");
        this.setErrorID(Integer.parseInt(errorList[Constants.ERROR_ID_POSITION]));
        this.setMessage(errorList[Constants.ERROR_MESSAGE_POSITION]);
    }

    @Override
    public String toString(){
        String reportString = "No report in response";
        if(report!=null) reportString=this.getReport().toString();
        return "[" + this.getResponseTime() + " ms]" +
                "[" + this.getResponseCode() + "]" +
                "[" + this.getErrorID() + "]" +
                " " + getMessage() + " " +reportString;
    }

    public String getResponseString() {
        return reponseString;
    }

    public void setResponseString(String responseString) throws BehavioSecResponsetException {

        this.reponseString = responseString;
        if(this.path == Constants.API_GET_REPORT){
            try {
                Report bhsReport = objectMapper.readValue(
                        reponseString,
                        Report.class
                );
                this.report = bhsReport;
            } catch (IOException e) {
                e.printStackTrace();
                throw new BehavioSecResponsetException("Error parsing report");
            }
        }
    }

    public void setPath(String path) {
        this.path=path;
    }
}
