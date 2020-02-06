package com.behaviosec.isdk.entities;

import com.behaviosec.isdk.config.Constants;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ReportRequest {

    private List<NameValuePair> requestObject;

    public ReportRequest(String username){
        super();
        requestObject.add(new BasicNameValuePair(Constants.USER_ID, username));
    }

    public ReportRequest(){
        requestObject = new ArrayList<>(2);
        requestObject.add(new BasicNameValuePair(Constants.TIMESTAMP,
                Long.toString(Calendar.getInstance().getTimeInMillis())));
    }

    public void setUsername(String username){
        requestObject.add(new BasicNameValuePair(Constants.USER_ID, username));
    }

    public void userAgent(String userAgent){
        requestObject.add(new BasicNameValuePair(Constants.USER_AGENT, userAgent));
    }

    public void setUserIp(String ip){
        requestObject.add(new BasicNameValuePair(Constants.IP, ip));
    }

    public void setSessionId(String sessionId){
        requestObject.add(new BasicNameValuePair(Constants.SESSION_ID, sessionId));
    }

    public void setTimingData(String bData){
        requestObject.add(new BasicNameValuePair(Constants.TIMING_DATA, bData));
    }

    public void setReportFlags(int reportFlags){
        requestObject.add(new BasicNameValuePair(Constants.REPORT_FLAGS, String.valueOf(reportFlags)));
    }

    public void setOperatorFlags(int operatorFlags){
        requestObject.add(new BasicNameValuePair(Constants.OPERATOR_FLAGS, String.valueOf(operatorFlags)));
    }

    public void setNotes(String notes){
        requestObject.add(new BasicNameValuePair(Constants.TIMING_DATA, notes));
    }

    public List<NameValuePair> getNameValuePaiList() {
        return requestObject;
    }
}
