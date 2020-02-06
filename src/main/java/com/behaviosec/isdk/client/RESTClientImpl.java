package com.behaviosec.isdk.client;


import com.behaviosec.isdk.config.Constants;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;



/**
 * REST client implementation for connectivity with BehavioSec endpoint
 */
public class RESTClientImpl {

    private static final String TAG = RESTClientImpl.class.getName();
    private static final Logger LOGGER = LoggerFactory.getLogger(TAG);
    private String endPoint;
    private HttpClient httpClient;


    public RESTClientImpl(String endPoint) {
        this.endPoint =endPoint;
        LOGGER.info(TAG + " BehavioSecRESTClient: " + this.endPoint);
        httpClient = HttpClientBuilder.create().build();
    }


    /**
     * Submit behavior data and get evaluation
     * @param request List<NameValuePair>
     * @return server response
     * @throws IOException
     */
    public HttpResponse executeRequest(List<NameValuePair> request, String path) throws IOException {
        String uri = endPoint + path;
        LOGGER.info(TAG + " makePost " + uri);
        HttpPost postRequest = new HttpPost(uri);
        postRequest.setHeader("Accept", Constants.ACCEPT_HEADER);
        postRequest.setHeader("Content-type", Constants.CONTENT_TYPE);
        if (request != null ) {
            postRequest.setEntity(new UrlEncodedFormEntity(request));
        }
        return this.httpClient.execute(postRequest);
    }
}
