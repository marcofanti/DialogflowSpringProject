package com.behaviosec.isdk.client;


import com.behaviosec.isdk.config.BehavioSecException;
import com.behaviosec.isdk.entities.ReportRequest;
import com.behaviosec.isdk.entities.Response;
import org.apache.http.NameValuePair;

import java.io.IOException;
import java.util.List;

interface API {


    /**
     * RESTFull call to submit timing data and receive user report
     * Documentation https://developer.behaviosec.com/docapi/5.1/#getreport
     *
     * @return Returns a JSON formatted report (a collection of reports) that contains the comparison of a timing behavior string to the given user ID.
     * @param request
     */

//    HttpResponse getReport(List<NameValuePair> report) throws IOException;
    Response getReport(ReportRequest request) throws IOException, BehavioSecException;

    /**
     * Finalizes the session and marks it ready for training/evaluation.
     *
     * sessionId	String		The session ID to finalize.
     * tenantId	String (optional)	default_tenant	Only applicable in a multi-tenant setup. Refer to the multi-tenancy section for further information.
     * removeData	Boolean (optional)	false	Session will be finalized without training and session data removed.
     * waitForCompletion	Boolean (optional)	false	If true, the endpoint will be synchronous with the session finalized when returned.
     * @param report
     * @return
     * @throws BehavioSecException
     */
    Response finalizeSession(String sessionID, boolean removeData, boolean waitForcompletion) throws BehavioSecException;

    Response forceTraining(List<NameValuePair> report) throws BehavioSecException;
    boolean getHealthCheck(List<NameValuePair> report) throws BehavioSecException;
    Response getInvestigationReport(List<NameValuePair> report) throws BehavioSecException;
    Response getObfuscatedJavaScript(List<NameValuePair> report) throws BehavioSecException;
    Response getReportAndInvestigate(List<NameValuePair> report) throws BehavioSecException;
    Response getSession(List<NameValuePair> report) throws BehavioSecException;
    Response getUser(List<NameValuePair> report) throws BehavioSecException;
    Response getVersion(List<NameValuePair> report) throws BehavioSecException;
    Response removeUser(List<NameValuePair> report) throws BehavioSecException;
    Response resetProfile(List<NameValuePair> report) throws BehavioSecException;
    Response setFraudStatus(List<NameValuePair> report) throws BehavioSecException;
    Response setUser(List<NameValuePair> report) throws BehavioSecException;
    Response whitelistUser(List<NameValuePair> report) throws BehavioSecException;
}
