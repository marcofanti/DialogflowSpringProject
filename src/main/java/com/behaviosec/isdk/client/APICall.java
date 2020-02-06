package com.behaviosec.isdk.client;

import com.behaviosec.isdk.config.Constants;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class APICall {

    public String path;
    public List<NameValuePair> requestObject;


    public APICall(BuilderBase<?, ?, ?> builder){
        this.path = builder.path;
        this.requestObject = builder.requestObject;
    }

    public static report reportBuilder() {
        return new report();
    }

    public static health healthBuilder() {
        return new health();
    }

    public String toString(){
        String ret = "";
        for(NameValuePair obj: this.requestObject){
            ret += obj.getName() + ":" + obj.getValue() + " ;";
        }
        return ret;
    }

    /**
     * Abstract builder class so that the main API call can use generic types
     * Implements all potential flags
     * Sublass should add checks if the mandatory values exists
     */
    public static abstract class BuilderBase<B extends BuilderBase<B, H, R>, H, R>{

        public String path;
//        protected abstract T self();
        private List<NameValuePair> requestObject = new ArrayList<>();

        abstract B self();

        abstract H apiCallHook();

        abstract R build();
        /**
         * Returns constructed list with name value pairs for request object
         * @return List<NameValuePair> getRequestObject
         */
        public List<NameValuePair> getRequestObject() { return this.requestObject; }

        public void add( BasicNameValuePair pair) {
            this.requestObject.add(pair);
        }

        public H username(String username){
            add(new BasicNameValuePair(Constants.USER_ID, username));
            return apiCallHook();
        }

        public H userAgent(String userAgent){
            add(new BasicNameValuePair(Constants.USER_AGENT, userAgent));
            return apiCallHook();

        }

        public H userIP(String ip){
            add(new BasicNameValuePair(Constants.IP, ip));
            return apiCallHook();

        }

        public H sessionId(String sessionId){
            add(new BasicNameValuePair(Constants.SESSION_ID, sessionId));
            return apiCallHook();

        }

        public H timingData(String bData){
            add(new BasicNameValuePair(Constants.TIMING_DATA, bData));
            return apiCallHook();


        }

        public H reportFlags(int reportFlags){
            add(new BasicNameValuePair(Constants.REPORT_FLAGS, String.valueOf(reportFlags)));
            return apiCallHook();
        }

        public H operatorFlags(int operatorFlags){
            add(new BasicNameValuePair(Constants.OPERATOR_FLAGS, String.valueOf(operatorFlags)));
            return apiCallHook();
        }

        public H setNotes(String notes){
            add(new BasicNameValuePair(Constants.NOTES, notes));
            return apiCallHook();
        }
    }



    public static class report extends BuilderBase<report, report, APICall> {

        public report(){
            super();
            super.path = Constants.API_GET_REPORT;
        }

        public APICall build() {
            return new APICall(this);
        }

        @Override
        report self() {
            return this;
        }

        @Override
        report apiCallHook() {
            return this;
        }



    }

    public static class health extends BuilderBase<health, health, APICall> {

        public health(){
            super();
            super.path = Constants.API_GET_HEALTH_CHECK;
        }

        public APICall build() {
            return new APICall(this);
        }

        @Override
        health self() {
            return this;
        }

        @Override
        health apiCallHook() {
            return this;
        }



    }


}
