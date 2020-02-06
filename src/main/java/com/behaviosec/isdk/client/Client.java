package com.behaviosec.isdk.client;

import com.behaviosec.isdk.config.BehavioSecConfigurationException;
import com.behaviosec.isdk.config.BehavioSecException;
import com.behaviosec.isdk.config.Constants;
import com.behaviosec.isdk.entities.Response;
import org.apache.http.HttpResponse;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Client {

    private static final String TAG = Client.class.getName();
    private static final Logger LOGGER = LoggerFactory.getLogger(TAG);
    private final ClientConfiguration requestConfig;
    private RESTClientImpl restClient;

    public Client(ClientConfiguration config){
        this.requestConfig = config;
        this.restClient = new RESTClientImpl(this.requestConfig.getEndPoint());
    }

    public Response makeCall(APICall call) throws BehavioSecException {
        if (this.requestConfig.isMultitenant()){
            call.requestObject.add(
                    new BasicNameValuePair( Constants.TENANT_ID, this.requestConfig.getTenantId() )
            );
        }
        try {
            long startTime = System.currentTimeMillis();
            HttpResponse reportResponse = restClient.executeRequest(call.requestObject, call.path);
            long elapsedTime = System.currentTimeMillis() - startTime;
            Response response = new Response();
            response.setResponseTime( elapsedTime);
            response.setPath(call.path);
            if (reportResponse.getStatusLine().getStatusCode() == 200) {
                response.setResponseCode(reportResponse.getStatusLine().getStatusCode());
                response.setResponseString(EntityUtils.toString(reportResponse.getEntity()));
                return response;
            } else {
                return handleAPIerror(reportResponse);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new BehavioSecConfigurationException("Error connecting to API server: " + e.getMessage());
        }
    }


    private Response handleAPIerror(HttpResponse reportResponse) throws IOException {
        Response response = new Response();
        int responseCode = reportResponse.getStatusLine().getStatusCode();
        String error = getResponseString(reportResponse);
        response.setResponseCode(responseCode);
        if (responseCode == 400) {
            response.addError(error);
            LOGGER.error(TAG + " response 400  " + error + ".");
        } else if (responseCode == 403) {
            LOGGER.error(TAG + " response 403  " + error + ".");
            response.setErrorID(403);
            response.setMessage("Unauthorized request: " + error + ".");
        } else if (responseCode == 405) {
            LOGGER.error(TAG + " response 403  " + error + ".");
            response.setErrorID(405);
            response.setMessage("Post is not supported for this URL.");
        }else if (responseCode == 500) {
            LOGGER.error(TAG + " response 500  " + error);
            response.setErrorID(500);
            response.setMessage("Internal API server error: " + error + ".");
        } else {
            LOGGER.error(TAG + " response code: " + responseCode + " DATA: " + error + ".");
            response.setErrorID(responseCode);
            response.setMessage("Response code " + responseCode + " message" + error + ".");
        }
        return response;
    }


    private String getResponseString(HttpResponse resp) throws IOException {
        return EntityUtils.toString(resp.getEntity(), "UTF-8");
    }


}
