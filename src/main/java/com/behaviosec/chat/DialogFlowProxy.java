package com.behaviosec.chat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.ibm.cloud.sdk.core.http.HttpConfigOptions;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.assistant.v2.Assistant;

@SpringBootApplication
public class DialogFlowProxy {
	public static Assistant assistant = null;

	public static void main(String[] args) {
		IamAuthenticator authenticator = new IamAuthenticator("vBmsgWv2Sz6UB8j94T_ii8sRilf5WiXDBsjgKRhLGEuz");
		assistant = new Assistant("2019-02-28", authenticator);
//		assistant.setServiceUrl("http://localhost:9999/assistant/api");
		assistant.setServiceUrl("https://gateway.watsonplatform.net/assistant/api");
 
		HttpConfigOptions configOptions = new HttpConfigOptions.Builder()
		  .disableSslVerification(true)
		  .build();
		assistant.configureClient(configOptions);

		SpringApplication.run(DialogFlowProxy.class, args);
	}
}
