package com.behaviosec.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.behaviosec.isdk.client.APICall;
import com.behaviosec.isdk.client.Client;
import com.behaviosec.isdk.client.ClientConfiguration;
import com.behaviosec.isdk.config.BehavioSecException;
import com.behaviosec.isdk.entities.Report;
import com.behaviosec.isdk.entities.Response;
import com.behaviosec.isdk.evaluators.BooleanEvaluator;
import com.behaviosec.isdk.evaluators.ScoreEvaluator;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.google.cloud.dialogflow.v2.DetectIntentResponse;
import com.google.cloud.dialogflow.v2.QueryInput;
import com.google.cloud.dialogflow.v2.QueryResult;
import com.google.cloud.dialogflow.v2.SessionName;
import com.google.cloud.dialogflow.v2.SessionsClient;
import com.google.cloud.dialogflow.v2.TextInput;
import com.google.cloud.dialogflow.v2.TextInput.Builder;
import com.google.common.collect.Maps;
import com.ibm.cloud.sdk.core.http.HttpConfigOptions;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.assistant.v2.Assistant;
import com.ibm.watson.assistant.v2.model.CreateSessionOptions;
import com.ibm.watson.assistant.v2.model.MessageInput;
import com.ibm.watson.assistant.v2.model.MessageOptions;
import com.ibm.watson.assistant.v2.model.MessageResponse;
import com.ibm.watson.assistant.v2.model.SessionResponse;

@RestController
public class DialogFlowController {
	@Autowired
	ApplicationArguments applicationArguments;
	private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass().getName());
	public static Message currentMessage = new Message();
	static int maxRisk = 200;
	private static boolean isDialogFlow = false;
    private String projectId = "banking-dggfcs";
    private String languageCode = "en";

	@SuppressWarnings("deprecation")
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public String proxyGet(HttpMethod method, HttpServletRequest request, HttpResponse response)
			throws URISyntaxException {
		URI thirdPartyApi = new URI("http", null, "localhost", 9090, request.getRequestURI(), request.getQueryString(),
				null);

		return proxyPost(method, request, response, thirdPartyApi);
	}

	private StringBuilder getBody(HttpServletRequest request) {
		StringBuilder body = new StringBuilder();
		try (Reader reader = new BufferedReader(
				new InputStreamReader(request.getInputStream(), Charset.forName(StandardCharsets.UTF_8.name())))) {
			int c = 0;
			while ((c = reader.read()) != -1) {
				body.append((char) c);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return body;
	}

	private Message parseMessage(String originalBody, Message message) {
		if (originalBody == null || originalBody.length() == 0) {
			return message;
		}
		int bdataIndex = originalBody.length() - 2;

		if (originalBody.indexOf(",\"bdata\"") > 10) {
			bdataIndex = originalBody.indexOf(",\"bdata\"");
		}
		String bdata = null;
		String newBody = originalBody.substring(0, bdataIndex) + "}}";
		log.debug("New body = " + newBody);

		if (originalBody.length() > bdataIndex + 30) {
			bdata = originalBody.substring(bdataIndex + ",\"bdata\":".length(), originalBody.length() - 2);
		}
		log.debug("bdata = " + bdata);

		JacksonJsonParser parse = new JacksonJsonParser();
		JsonFactory factory = new JsonFactory();
		JsonParser parser = null;
		try {
			parser = factory.createParser(originalBody.toString());
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
							log.debug("********** bdata is set" + bdata);
						}
					}
				}
			} catch (IOException e) {
			}
		}

		if (isDialogFlow) {
			int len = "{\"input\":{\"message_type\":\"text\",\"text\":\"".length();
			int tot = newBody.length();
			newBody = newBody.substring(len, tot - 3);
		}
		message.setMessage(newBody);
//		bdata = "[[\"m\",\"n\",{\"vendorSub\":\"\",\"productSub\":\"20030107\",\"vendor\":\"Google Inc.\",\"maxTouchPoints\":1,\"hardwareConcurrency\":16,\"cookieEnabled\":true,\"appCodeName\":\"Mozilla\",\"appName\":\"Netscape\",\"appVersion\":\"5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1\",\"platform\":\"MacIntel\",\"product\":\"Gecko\",\"userAgent\":\"Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1\",\"language\":\"en-US\",\"languages\":[\"en-US\",\"en\"],\"onLine\":true,\"doNotTrack\":null,\"geolocation\":{},\"mediaCapabilities\":{},\"connection\":{},\"webkitTemporaryStorage\":{},\"webkitPersistentStorage\":{},\"userActivation\":{},\"mediaSession\":{},\"permissions\":{},\"deviceMemory\":8,\"clipboard\":{},\"credentials\":{},\"keyboard\":{},\"locks\":{},\"mediaDevices\":{},\"serviceWorker\":{},\"storage\":{},\"presentation\":{},\"bluetooth\":{},\"usb\":{}}],[\"m\",\"s\",{\"availWidth\":375,\"availHeight\":812,\"width\":375,\"height\":812,\"colorDepth\":24,\"pixelDepth\":24,\"availLeft\":0,\"availTop\":0,\"orientation\":{}}],[\"m\",\"v\",261],[\"m\",\"e\",{\"ptype\":null,\"ptypes\":{\"touch\":2},\"k229\":0,\"kn\":0,\"tz\":480,\"pr\":3,\"u\":{\"7\":1,\"8\":10,\"9\":29,\"10\":25,\"11\":20,\"12\":12,\"13\":3},\"f\":[1,\"[object ServiceWorkerContainer]\",\"[object Geolocation]\",\"true\",\"undefined\",\"20030107\",\"undefined\",\"true\",\"[object WebGLRenderingContext]\",\"function HTMLCanvasElement() { [native code] }\",\"true\",\"false\",\"null\",\"MacIntel\",\"undefined\",\"true\",\"false\",\"true\",\"true\",\"{\\\"0\\\":\\\"Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1\\\",\\\"1\\\":\\\"en-US\\\",\\\"2\\\":\\\"true\\\",\\\"3\\\":\\\"812x375x24\\\",\\\"4\\\":\\\"24\\\",\\\"5\\\":\\\"\\\",\\\"6\\\":\\\"\\\",\\\"7\\\":\\\"\\\",\\\"8\\\":\\\"480\\\",\\\"9\\\":\\\"true\\\",\\\"10\\\":\\\"true\\\",\\\"11\\\":\\\"3\\\",\\\"12\\\":{}}\"]}],[\"m\",\"k\",{},[[\"Z\",49,1578952924553],[\"U\",2070,\"text#username\"],[\"r\",2070,\"\"],[\"v\",6428,\"\"],[\"n\",6428,\"text#username\"],[\"U\",8732,\"text#username\"],[\"r\",8732,\"\"],[\"v\",17219,\"\"],[\"n\",17219,\"text#username\"],[\"D\",17235,\"DIV##LI#\"],[\"U\",17235,\"text#username\"],[\"E\",17236,\"DIV##LI#\"]]],[\"c\",[[\"t\",\"DIV##LI#\",17230],[\"v\",375,812,17230],[\"md\",187.97265625,148.2734375,17230,0],[\"mu\",187.97265625,148.2734375,17234,0]],\"/\"],[\"w\",[{\"text#username\":0},{\"movement\":0}],\"/\"]]";
//		bdata = "[[\"m\",\"n\",{\"vendorSub\":\"\",\"productSub\":\"20030107\",\"vendor\":\"Google Inc.\",\"maxTouchPoints\":1,\"hardwareConcurrency\":16,\"cookieEnabled\":true,\"appCodeName\":\"Mozilla\",\"appName\":\"Netscape\",\"appVersion\":\"5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1\",\"platform\":\"MacIntel\",\"product\":\"Gecko\",\"userAgent\":\"Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1\",\"language\":\"en-US\",\"languages\":[\"en-US\",\"en\"],\"onLine\":true,\"doNotTrack\":null,\"geolocation\":{},\"mediaCapabilities\":{},\"connection\":{},\"webkitTemporaryStorage\":{},\"webkitPersistentStorage\":{},\"userActivation\":{},\"mediaSession\":{},\"permissions\":{},\"deviceMemory\":8,\"clipboard\":{},\"credentials\":{},\"keyboard\":{},\"locks\":{},\"mediaDevices\":{},\"serviceWorker\":{},\"storage\":{},\"presentation\":{},\"bluetooth\":{},\"usb\":{}}],[\"m\",\"s\",{\"availWidth\":375,\"availHeight\":812,\"width\":375,\"height\":812,\"colorDepth\":24,\"pixelDepth\":24,\"availLeft\":0,\"availTop\":0,\"orientation\":{}}],[\"m\",\"v\",261],[\"m\",\"e\",{\"ptype\":null,\"ptypes\":{\"touch\":8},\"k229\":0,\"kn\":0,\"tz\":480,\"pr\":3,\"u\":{\"8\":7,\"9\":16,\"10\":51,\"11\":20,\"12\":6},\"f\":[1,\"[object ServiceWorkerContainer]\",\"[object Geolocation]\",\"true\",\"undefined\",\"20030107\",\"undefined\",\"true\",\"[object WebGLRenderingContext]\",\"function HTMLCanvasElement() { [native code] }\",\"true\",\"false\",\"null\",\"MacIntel\",\"undefined\",\"true\",\"false\",\"true\",\"true\",\"{\\\"0\\\":\\\"Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1\\\",\\\"1\\\":\\\"en-US\\\",\\\"2\\\":\\\"true\\\",\\\"3\\\":\\\"812x375x24\\\",\\\"4\\\":\\\"24\\\",\\\"5\\\":\\\"\\\",\\\"6\\\":\\\"\\\",\\\"7\\\":\\\"\\\",\\\"8\\\":\\\"480\\\",\\\"9\\\":\\\"true\\\",\\\"10\\\":\\\"true\\\",\\\"11\\\":\\\"3\\\",\\\"12\\\":{}}\"]}],[\"m\",\"k\",{},[[\"Z\",12,1578958140759],[\"D\",3456,\"DIV##LI#\"],[\"U\",3456,\"text#username\"],[\"E\",3456,\"DIV##LI#\"],[\"D\",7476,\"DIV##LI#\"],[\"E\",7477,\"DIV##LI#\"],[\"D\",10346,\"DIV##LI#\"],[\"E\",10346,\"DIV##LI#\"],[\"D\",15687,\"text#username\"],[\"n\",15687,\"text#username\"],[\"E\",15688,\"text#username\"],[\"an\",18395,\"text#username\"],[\"sn\",18631,\"text#username\"]]],[\"c\",[[\"t\",\"DIV##LI#\",3448],[\"v\",375,812,3448],[\"md\",242.44921875,167.453125,3448,0],[\"mu\",242.44921875,167.453125,3454,0],[\"t\",\"DIV##LI#\",7469],[\"md\",157.63671875,187.4765625,7469,0],[\"mu\",157.63671875,187.4765625,7475,0],[\"t\",\"DIV##LI#\",10339],[\"md\",177.71484375,152.80859375,10339,0],[\"mu\",177.71484375,152.80859375,10345,0],[\"t\",\"INPUT#textInput#LABEL#\",15680],[\"md\",160.93359375,593.4765625,15680,0],[\"mu\",160.93359375,593.4765625,15686,0]],\"/\"],[\"w\",[{\"text#username\":21},{\"movement\":0}],\"/\"]]\r\n";
		message.setBdata(bdata);

		return message;
	}

	public static QueryResult detectIntentTexts(String projectId, String text, String sessionId,
			String languageCode)  {
		try (SessionsClient sessionsClient = SessionsClient.create()) {
			// Set the session name using the sessionId (UUID) and projectID (my-project-id)
			SessionName session = SessionName.of(projectId, sessionId);
			System.out.println("Session Path: " + session.toString());

			// Detect intents for each text input
			// Set the text (hello) and language code (en-US) for the query
			Builder textInput = TextInput.newBuilder().setText(text).setLanguageCode(languageCode);

			// Build the query with the TextInput
			QueryInput queryInput = QueryInput.newBuilder().setText(textInput).build();

			// Performs the detect intent request
			DetectIntentResponse response = sessionsClient.detectIntent(session, queryInput);

			// Display the query result
			QueryResult queryResult = response.getQueryResult();
			return queryResult;
		} catch (Exception e) {
			return null;
		}
	}

	private String validateUser(String body) {

		Message thisMessage = parseMessage(body.toString(), new Message());

		if (!"{\"input\":{\"message_type\":\"text\",\"text\":\"mfanti@behaviosec.com\"}}"
				.equals(thisMessage.getMessage())
				&& thisMessage.getMessage().length() != "{\"input\":{\"message_type\":\"text\",\"text\":\"123456\"}}"
						.length()) {
			return "{\"output\":{\"generic\":[{\"response_type\":\"text\",\"text\":\"Sorry Marco, I still cannot verify you. "
					+ "Please enter your google authenticator 6 digit number.\"}],\"intents\":[{\"intent\":\"General_Conversation-Greetings\",\"confidence\":0.96069655418396}],\"entities\":[]}}";
		}

		if (thisMessage.getBdata() != null && thisMessage.getBdata().length() > 0) {
			Report r = getReport(thisMessage.getBdata(), "mfanti@behaviosec.com",
					"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.74 Safari/537.36",
					"127.0.0.1", thisMessage.getSessionID());
			if (r == null || r.getScore() < 80 || r.getConfidence() < 50 || r.getRisk() > maxRisk || r.isTabAnomaly()) {
				return "{\"output\":{\"generic\":[{\"response_type\":\"text\",\"text\":\"Sorry Marco, I still cannot verify you. "
						+ "Please enter your google authenticator 6 digit number.\"}],\"intents\":[{\"intent\":\"General_Conversation-Greetings\",\"confidence\":0.96069655418396}],\"entities\":[]}}";
			} else {
				currentMessage.setReport(r);
				currentMessage.setAuthorizedBefore(0);
				return currentMessage.getResponse();
			}
		}

		return "{\"output\":{\"generic\":[{\"response_type\":\"text\",\"text\":\"Sorry Marco, I still cannot verify you. "
				+ "Please enter your google authenticator 6 digit number.\"}],\"intents\":[{\"intent\":\"General_Conversation-Greetings\",\"confidence\":0.96069655418396}],\"entities\":[]}}";
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public String proxyPost(HttpMethod method, HttpServletRequest request, HttpResponse response, URI thirdPartyApi)
			throws URISyntaxException {

		String[] applicationArgumentsList = applicationArguments.getSourceArgs();
		String chatType = applicationArgumentsList[0];
		log.debug("chatType " + chatType);
		if ("dialogflow".equals(chatType)) {
			isDialogFlow = true;
		}

		String requestURI = request.getRequestURI();
		log.debug("requestURI is " + requestURI);

		if (currentMessage.getAuthorizedBefore() == -1 && !requestURI.endsWith("/sessions")) {
			StringBuilder body = getBody(request);

			return validateUser(body.toString());
		}

		StringBuilder body = getBody(request);

		Message thisMessage = parseMessage(body.toString(), currentMessage);

		if (requestURI.startsWith("/getReport") || requestURI.endsWith("/message")) {

			if (thisMessage.getBdata() != null && thisMessage.getBdata().length() > 0) {
				if ("{\"input\":{\"message_type\":\"text\",\"text\":\"I want to make a credit card payment\"}}"
						.equals(currentMessage.getMessage())) {
					currentMessage.setReport(getReport(thisMessage.getBdata(), "mfanti@behaviosec.com",
							"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.117 Safari/537.36",
							"127.0.0.1", thisMessage.getSessionID()));
				}
			}
		} else if (requestURI.endsWith("/sessions")) {
			if (isDialogFlow) {
				UUID uuid = UUID.randomUUID();
				String sessionId = uuid.toString();
				currentMessage = new Message(sessionId);
				return "{\r\n" + "  \"session_id\": \"" + sessionId + "\"\r\n" + "}";
			}
			try {
				String assistantId = requestURI.substring("/assistant/api/v2/assistants/".length(),
						requestURI.length() - "/sessions".length());
				log.debug("assistantId is " + assistantId);

				CreateSessionOptions options = new CreateSessionOptions.Builder(assistantId).build();
				SessionResponse sessionResponse = HelloWorldApplication.assistant.createSession(options).execute()
						.getResult();

				log.info("returning " + sessionResponse.toString());
				int beginIndex = sessionResponse.toString().indexOf("session_id\":\"") + "session_id\":\"".length() + 7;
				currentMessage = new Message(
						sessionResponse.toString().substring(beginIndex, sessionResponse.toString().length() - 3));

				String result = currentMessage.getSessionID();
				log.debug("result is " + result);
				return sessionResponse.toString();
			} catch (Exception e) {
				return null;
			}
		}

		if (thirdPartyApi == null) {
			thirdPartyApi = new URI("http", null, "localhost", 9999, request.getRequestURI(), request.getQueryString(),
					null);
		}

		if (body.toString().startsWith("{\"session_id\":\"")) {
			currentMessage.setSessionID(body.toString().substring("{\"session_id\":\"".length()));
			log.error("********************************************** should not be here");
		}
		
		if (isDialogFlow) {
			String defaultString = "{\r\n" + 
					"  \"output\": {\r\n" + 
					"    \"generic\": [\r\n" + 
					"      {\r\n" + 
					"        \"response_type\": \"text\",\r\n" + 
					"        \"text\": \"Hello Marco, I am the ACME bank's Virtual Agent.\"\r\n" + 
					"      },\r\n" + 
					"      {\r\n" + 
					"        \"response_type\": \"option\",\r\n" + 
					"        \"title\": \"I can help you with a number of banking tasks:\",\r\n" + 
					"        \"options\": [\r\n" + 
					"          {\r\n" + 
					"            \"label\": \"Making a credit card payment\",\r\n" + 
					"            \"value\": {\r\n" + 
					"              \"input\": {\r\n" + 
					"                \"text\": \"I want to make a credit card payment\"\r\n" + 
					"              }\r\n" + 
					"            }\r\n" + 
					"          },\r\n" + 
					"          {\r\n" + 
					"            \"label\": \"Transfer money\",\r\n" + 
					"            \"value\": {\r\n" + 
					"              \"input\": {\r\n" + 
					"                \"text\": \"I want transfer money\"\r\n" + 
					"              }\r\n" + 
					"            }\r\n" + 
					"          },\r\n" + 
					"          {\r\n" + 
					"            \"label\": \"Check balance\",\r\n" + 
					"            \"value\": {\r\n" + 
					"              \"input\": {\r\n" + 
					"                \"text\": \"I want to check my balance\"\r\n" + 
					"              }\r\n" + 
					"            }\r\n" + 
					"          },\r\n" + 
					"          {\r\n" + 
					"            \"label\": \"Open an account\",\r\n" + 
					"            \"value\": {\r\n" + 
					"              \"input\": {\r\n" + 
					"                \"text\": \"I want to open an account\"\r\n" + 
					"              }\r\n" + 
					"            }\r\n" + 
					"          }\r\n" + 
					"        ]\r\n" + 
					"      }\r\n" + 
					"    ],\r\n" + 
					"    \"intents\": [],\r\n" + 
					"    \"entities\": []\r\n" + 
					"  }\r\n" + 
					"}\r\n";

			QueryResult queryResult = null;
			
			try {
				if (currentMessage.getMessage() == null || currentMessage.getMessage().trim().length()==0) {
					return defaultString;
				}
				queryResult = detectIntentTexts(projectId, currentMessage.getMessage(), currentMessage.getSessionID(), languageCode);
				log.debug("====================");
				log.debug("Query Text: '%s'\n", queryResult.getQueryText());
				log.debug("Detected Intent: %s (confidence: %f)\n", queryResult.getIntent().getDisplayName(),
						queryResult.getIntentDetectionConfidence());
				log.debug("Fulfillment Text: '%s'\n", queryResult.getFulfillmentText());
				log.debug(queryResult.toString());

				String fulfillmentText = queryResult.getFulfillmentText();
				String fulfillmentTextJson = "{\r\n" + 
						"  \"output\": {\r\n" + 
						"    \"generic\": [\r\n" + 
						"      {\r\n" + 
						"        \"response_type\": \"text\",\r\n" + 
						"        \"text\": \"" + fulfillmentText + "\"\r\n" + 
						"      }\r\n" + 
						"    ],\r\n" + 
						"    \"intents\": [\r\n" + 
						"      {\r\n" + 
						"        \"intent\": \"General_Conversation-Greetings\",\r\n" + 
						"        \"confidence\": 0.5048123836517334\r\n" + 
						"      }\r\n" + 
						"    ],\r\n" + 
						"    \"entities\": []\r\n" + 
						"  }\r\n" + 
						"}";
				log.debug("fulfillmentTextJson " + fulfillmentTextJson);
				return fulfillmentTextJson;
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return defaultString;
		}

		MessageInput input = new MessageInput.Builder().messageType("text").text(currentMessage.getMessage()).build();

		MessageOptions options = new MessageOptions.Builder("b7d8c8fe-0e39-4fd5-bfc0-e9b22868c1cb",
				currentMessage.getSessionID()).input(input).build();

		MessageResponse messageResponse = HelloWorldApplication.assistant.message(options).execute().getResult();

		System.out.println(messageResponse);

		String result = messageResponse.toString();

		if (messageResponse != null) {
			// return it as a String
			if (result.contains("\"Hello, I am the bank's Virtual Agent.")) {
				int first = 96;
				result = result.substring(0, first) + " Marco" + result.substring(first);
			} else if (result.contains("\"text\": \"I can help you with credit card payments.\"")) {
				Report r = currentMessage.getReport();
				if (r == null) {
					if (thisMessage.getBdata() != null && thisMessage.getBdata().length() > 0) {
						currentMessage.setReport(getReport(thisMessage.getBdata(), "mfanti@behaviosec.com",
								"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.117 Safari/537.36",
								"127.0.0.1", thisMessage.getSessionID()));
						r = currentMessage.getReport();
					}
				}
				if (r != null && r.toString().indexOf("diDesc:") > 0) {
					int st = r.toString().indexOf("diDesc:");
					String diDesc = r.toString().substring(st);
					log.debug(diDesc);
					if (r.toString().substring(st).contains(", 3") || r.toString().substring(st).indexOf("[3") > 0) {
						currentMessage.setAuthorizedBefore(-1);
						currentMessage.setResponse(result);
						return "{\"output\":{\"generic\":[{\"response_type\":\"text\",\"text\":\"Marco, I cannot verify you (cut and paste detected). "
								+ "Please enter your email address or username.\"}],\"intents\":[{\"intent\":\"General_Conversation-Greetings\",\"confidence\":0.96069655418396}],\"entities\":[]}}";
					}
					if (r.toString().substring(st).contains(", 0") || r.toString().substring(st).indexOf("[0") > 0) {
						currentMessage.setAuthorizedBefore(-1);
						currentMessage.setResponse(result);
						return "{\"output\":{\"generic\":[{\"response_type\":\"text\",\"text\":\"Marco, I cannot verify you (no input detected). "
								+ "Please enter your email address or username.\"}],\"intents\":[{\"intent\":\"General_Conversation-Greetings\",\"confidence\":0.96069655418396}],\"entities\":[]}}";
					}
				} else if (r == null || r.getScore() < 80 || r.getConfidence() < 50 || r.getRisk() > maxRisk
						|| currentMessage.getMessage().equals(
								"{\"input\":{\"message_type\":\"text\",\"text\":\"I want to make a credit card payment\"}}")) {
					currentMessage.setAuthorizedBefore(-1);
					currentMessage.setResponse(result);
					return "{\"output\":{\"generic\":[{\"response_type\":\"text\",\"text\":\"Marco, I cannot verify you. "
							+ "Please enter your email address or username.\"}],\"intents\":[{\"intent\":\"General_Conversation-Greetings\",\"confidence\":0.96069655418396}],\"entities\":[]}}";
				}
			} else {
				Report r = currentMessage.getReport();
				if (r != null) {
					if (r.getScore() > 80 && r.getConfidence() > 50 && r.getRisk() > maxRisk) {
						currentMessage.setAuthorizedBefore(currentMessage.getAuthorizedBefore() + 1);
					}
					log.debug("Score = " + r.getScore());
				}
			}
			log.debug(result);
			return result;
		}

		return null;

	}

	public Report getReport(String bdata, String username, String useragent, String ip, String uuid) {
		String url = "https://partner.behaviosec.com/";
		String tenantID = "THyek3Nd9qx6SbB2";

		ClientConfiguration cc = new ClientConfiguration(url);
		ClientConfiguration ccWithTenantId = new ClientConfiguration(url);

		Client client = new Client(cc);
		ccWithTenantId.setTenantId(tenantID);
		Client clientWithTenantId = new Client(ccWithTenantId);

		APICall callHealth = APICall.healthBuilder().build();
		APICall callReport = APICall.reportBuilder().username(username).userIP(ip).userAgent(useragent)
				.timingData(bdata).sessionId(uuid).build();

		try {
			Response h = client.makeCall(callHealth);
			if (h.hasReport()) {
				System.out.println(h.getReport().toString());
			}
			Response r = clientWithTenantId.makeCall(callReport);
//			System.out.println(r);
			if (r.hasReport()) {
				Report rep = r.getReport();
				if (rep != null) {
					log.debug(rep.toString());
					log.debug("Boolean evaluator: " + (new BooleanEvaluator()).evaluate(rep));
					log.debug("Score evaluator: " + (new ScoreEvaluator()).evaluate(rep));
					return rep;
				}
			} else {
				System.out.println("Error:" + r.getMessage());
			}
		} catch (BehavioSecException e) {
			e.printStackTrace();
		}
		return null;
	}
}
