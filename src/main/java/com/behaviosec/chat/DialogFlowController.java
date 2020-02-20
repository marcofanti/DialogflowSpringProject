package com.behaviosec.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.behaviosec.isdk.client.APICall;
import com.behaviosec.isdk.client.Client;
import com.behaviosec.isdk.client.ClientConfiguration;
import com.behaviosec.isdk.config.BehavioSecException;
import com.behaviosec.isdk.config.Constants;
import com.behaviosec.isdk.entities.Report;
import com.behaviosec.isdk.entities.Response;
import com.behaviosec.isdk.evaluators.BooleanEvaluator;
import com.behaviosec.isdk.evaluators.ScoreEvaluator;
import com.behaviosec.utils.ParseUtils;
import com.google.cloud.dialogflow.v2.DetectIntentResponse;
import com.google.cloud.dialogflow.v2.QueryInput;
import com.google.cloud.dialogflow.v2.QueryResult;
import com.google.cloud.dialogflow.v2.SessionName;
import com.google.cloud.dialogflow.v2.SessionsClient;
import com.google.cloud.dialogflow.v2.TextInput;
import com.google.cloud.dialogflow.v2.TextInput.Builder;
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
    
    private static final String THIRD_PARTY_APY_PROTOCOL = "http";
    private static final String THIRD_PARTY_APY_HOST = "localhost";
    private static final int THIRD_PARTY_APY_PORT = 9090;
    private static final String IP = "192.168.7.165";
    private static final String user = "marcofanti3@behaviosec.com";
    public static final String userAgent = 
    		"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.87 Safari/537.36";
	@SuppressWarnings("deprecation")
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public String proxyGet(HttpMethod method, HttpServletRequest request, HttpResponse response)
			throws URISyntaxException {
		URI thirdPartyApi = new URI(THIRD_PARTY_APY_PROTOCOL, null, THIRD_PARTY_APY_HOST, THIRD_PARTY_APY_PORT, request.getRequestURI(), request.getQueryString(),
				null);

		log.debug("Get URI = " + request.getRequestURI());
		String result = proxyPost(method, request, response, thirdPartyApi);
		log.debug("Result = " + result);
		return result;
//		return proxyPost(method, request, response, thirdPartyApi);
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

		Message thisMessage = ParseUtils.parseMessage(body.toString(), new Message(), isDialogFlow);

		String thisMessageStringMessage = thisMessage.getMessage();
		
		if (!user.equals(thisMessageStringMessage)) {
			
			log.debug(thisMessageStringMessage +  "!= ");
			log.debug(user);
			if (thisMessageStringMessage.length() != "123456".length()) {
				
				log.debug(thisMessageStringMessage.length() +  "!= ");
				log.debug("123456".length() + "");

			return "{\"output\":{\"generic\":[{\"response_type\":\"text\",\"text\":\"Sorry Marco, I still cannot verify you. "
					+ "Please enter your google authenticator 6 digit number.\"}],\"intents\":[{\"intent\":\"General_Conversation-Greetings\",\"confidence\":0.96069655418396}],\"entities\":[]}}";
		
			}
		}
		
		if (thisMessage.getBdata() != null && thisMessage.getBdata().length() > 0) {
			Report r = getReport(thisMessage.getBdata(), user,
					ParseUtils.parseTimingData(thisMessage.getBdata()),
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
			String bodyString = body.toString();
			return validateUser(bodyString);
		}

		StringBuilder body = getBody(request);

		Message thisMessage = ParseUtils.parseMessage(body.toString(), currentMessage, isDialogFlow);

		if (requestURI.startsWith("/getReport") || requestURI.endsWith("/message")) {

			if (thisMessage.getBdata() != null && thisMessage.getBdata().length() > 0) {
				if ("{\"input\":{\"message_type\":\"text\",\"text\":\"I want to make a credit card payment\"}}"
						.equals(currentMessage.getMessage())) {
					currentMessage.setReport(getReport(thisMessage.getBdata(), user,
							ParseUtils.parseTimingData(thisMessage.getBdata()), "127.0.0.1", thisMessage.getSessionID()));
				}
			}
		} else if (requestURI.endsWith("/sessions")) {
			if (isDialogFlow) {
				UUID uuid = UUID.randomUUID();
				String sessionId = uuid.toString();
				currentMessage = new Message(sessionId);
				String returnMessage = "{\r\n" + "  \"session_id\": \"" + sessionId + "\"\r\n" + "}";
				log.debug("Returning " + returnMessage);
				return returnMessage;
			}
			try {
				String assistantId = requestURI.substring("/assistant/api/v2/assistants/".length(),
						requestURI.length() - "/sessions".length());
				log.debug("assistantId is " + assistantId);

				CreateSessionOptions options = new CreateSessionOptions.Builder(assistantId).build();
				SessionResponse sessionResponse = DialogFlowProxy.assistant.createSession(options).execute()
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
				if (currentMessage.getMessage() == null || currentMessage.getMessage().trim().length() == 0) {
					log.debug("Returning defaultString\n" + defaultString);
					return defaultString;
				}
				
				ParseUtils.parseMessage(body.toString(), currentMessage, isDialogFlow);
				
				queryResult = detectIntentTexts(projectId, currentMessage.getMessage(), currentMessage.getSessionID(), languageCode);
				log.debug("====================");
				log.debug("\nQuery Text: {}\ngetIntent().getDisplayName(): {}:\ngetIntentDetectionConfidence(): {}:\ngetFulfillmentText(): {}:\nqueryResult.toString(): {}", 
						queryResult.getQueryText(), 
						queryResult.getIntent().getDisplayName(),
						queryResult.getIntentDetectionConfidence(), 
						queryResult.getFulfillmentText(), 
						queryResult.toString());
				
				log.debug("queryResult.getIntent() " + queryResult.getIntent());

				String fulfillmentText = queryResult.getFulfillmentText();
				
				boolean protectedIntent = false;
				
				if (queryResult.getIntent().getDisplayName().startsWith("account.balance")) {
					thisMessage.setBdata(thisMessage.getBdata().replaceAll("text#username", "text#balance"));					
					if (queryResult.getIntent().getDisplayName().startsWith("account.balance") && 
							queryResult.getFulfillmentText().startsWith("Here's your latest balance")) {
						fulfillmentText = "Here's your latest balance: $12,435.87";
					} 
					protectedIntent = true;
				} else if (queryResult.getIntent().getDisplayName().startsWith("account.open")) {
					thisMessage.setBdata(thisMessage.getBdata().replaceAll("text#username", "text#open"));
					thisMessage.setBdata(thisMessage.getBdata().replaceAll("CREDIT_INPUT", "OPEN_INPUT"));			
					protectedIntent = false;
				} else if (queryResult.getIntent().getDisplayName().startsWith("payment.due_date")) {
					thisMessage.setBdata(thisMessage.getBdata().replaceAll("text#username", "text#payment"));
					thisMessage.setBdata(thisMessage.getBdata().replaceAll("CREDIT_INPUT", "PAYMENT_INPUT"));			
					fulfillmentText = "The due date is: March 15, 2020";
					protectedIntent = false;
				} else if (queryResult.getIntent().getDisplayName().equals("")) {
					thisMessage.setBdata(thisMessage.getBdata().replaceAll("text#username", "text#generic"));
					thisMessage.setBdata(thisMessage.getBdata().replaceAll("CREDIT_INPUT", "GENERIC_INPUT"));			
					protectedIntent = false;
				} else if (queryResult.getIntent().getDisplayName().startsWith("transfer.money")) {
					thisMessage.setBdata(thisMessage.getBdata().replaceAll("text#username", "text#transfer"));
					thisMessage.setBdata(thisMessage.getBdata().replaceAll("CREDIT_INPUT", "TRANSFER_INPUT"));			
					if (queryResult.getFulfillmentText().startsWith("transfer.money") ||
//							queryResult.getFulfillmentText().startsWith("Sure. Transfer from which account") ||
							queryResult.getFulfillmentText().startsWith("To which account") ||
							queryResult.getFulfillmentText().startsWith("And, how much do you want to transfer") ||
							queryResult.getFulfillmentText().startsWith("All right. So, you're transferring") || 
							queryResult.getFulfillmentText().startsWith("Okay, I have processed your transfer. Your confirmation number is")) {
						if (queryResult.getFulfillmentText().startsWith("Okay, I have processed your transfer. Your confirmation number is")) {
							fulfillmentText = "Okay, I have processed your transfer. Your confirmation number is " + "Y1234Z";
						}
						thisMessage.setBdata(thisMessage.getBdata().replaceAll("text#transfer", "text#generic"));
						thisMessage.setBdata(thisMessage.getBdata().replaceAll("TRANSFER_INPUT", "GENERIC_INPUT"));			
						protectedIntent = false;
					} else {
						protectedIntent = true;
					}
				} 
				
				Report r = null; //currentMessage.getReport();
				if (r == null) {
					if (thisMessage.getBdata() != null && thisMessage.getBdata().length() > 0) {
						currentMessage.setReport(getReport(thisMessage.getBdata(), user,
								ParseUtils.parseTimingData(thisMessage.getBdata()), "127.0.0.1", thisMessage.getSessionID()));
						r = currentMessage.getReport();
					}
				}
				log.debug("Protected intent " + protectedIntent);
				
				if (protectedIntent && (r != null && r.toString().indexOf("diDesc:") > 0)) {
					int st = r.toString().indexOf("diDesc:");
					String diDesc = r.toString().substring(st);
					log.debug(diDesc);
					if (r.toString().substring(st).contains(", 3") || r.toString().substring(st).indexOf("[3") > 0) {
						currentMessage.setAuthorizedBefore(-1);
						currentMessage.setResponse(createJsonResponse(fulfillmentText, r));
						return "{\"output\":{\"generic\":[{\"response_type\":\"text\",\"text\":\"Marco, I cannot verify you (cut and paste detected). "
								+ "Please enter your email address or username.\"}],\"intents\":[{\"intent\":\"General_Conversation-Greetings\",\"confidence\":0.96069655418396}],\"entities\":[]}}";
					}
					if (r.toString().substring(st).contains(", 0") || r.toString().substring(st).indexOf("[0") > 0) {
						currentMessage.setAuthorizedBefore(-1);
						currentMessage.setResponse(createJsonResponse(fulfillmentText, r));
						return "{\"output\":{\"generic\":[{\"response_type\":\"text\",\"text\":\"Marco, I cannot verify you (no input detected). "
								+ "Please enter your email address or username.\"}],\"intents\":[{\"intent\":\"General_Conversation-Greetings\",\"confidence\":0.96069655418396}],\"entities\":[]}}";
					}
				} 

				if (protectedIntent && (r == null || r.getScore() < 80 || r.getConfidence() < 50 || r.getRisk() > maxRisk
						|| currentMessage.getMessage().equals(
								"{\"input\":{\"message_type\":\"text\",\"text\":\"I want to make a credit card payment\"}}"))) {
					currentMessage.setAuthorizedBefore(-1);
					currentMessage.setResponse(createJsonResponse(fulfillmentText, r));
					return "{\"output\":{\"generic\":[{\"response_type\":\"text\",\"text\":\"Marco, I cannot verify you. "
							+ "Please enter your email address or username.\"}],\"intents\":[{\"intent\":\"General_Conversation-Greetings\",\"confidence\":0.96069655418396}],\"entities\":[]}}";
				}
				
				return createJsonResponse(fulfillmentText, r);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return defaultString;
		}

		MessageInput input = new MessageInput.Builder().messageType("text").text(currentMessage.getMessage()).build();

		MessageOptions options = new MessageOptions.Builder("b7d8c8fe-0e39-4fd5-bfc0-e9b22868c1cb",
				currentMessage.getSessionID()).input(input).build();

		MessageResponse messageResponse = DialogFlowProxy.assistant.message(options).execute().getResult();

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
						currentMessage.setReport(getReport(thisMessage.getBdata(), user,
								ParseUtils.parseTimingData(thisMessage.getBdata()),
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
	
	private String createJsonResponse(String fulfillmentText, Report r) {
		String score = "";
		if (r!= null) {
			score += "(s)" + String.format("%.0f", r.getScore()) + " (r)" + String.format("%.0f", r.getRisk()) + " (c)" + String.format("%.0f", r.getConfidence());
		}
		String fulfillmentTextJson = "{\r\n" + 
				"  \"output\": {\r\n" + 
				"    \"generic\": [\r\n" + 
				"      {\r\n" + 
				"        \"response_type\": \"text\",\r\n" + 
				"        \"text\": \"" + score + ": " + fulfillmentText + "\"\r\n" + 
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
	}

	public Report getReport(String bdata, String username, String useragent, String clientIp, String uuid) {
		String url = "https://partner.behaviosec.com/";
		String tenantID = "THyek3Nd9qx6SbB2";

		ClientConfiguration cc = new ClientConfiguration(url);
		ClientConfiguration ccWithTenantId = new ClientConfiguration(url);

		Client client = new Client(cc);
		ccWithTenantId.setTenantId(tenantID);
		Client clientWithTenantId = new Client(ccWithTenantId);

		APICall callHealth = APICall.healthBuilder().build();
		
        APICall callReport = APICall.reportBuilder()
//                .tenantId(tenantID)
                .username(username)
                .userIP(clientIp)
                .userAgent(useragent)
                .timingData(bdata)
                .sessionId(uuid)
                .operatorFlags(Constants.FINALIZE_DIRECTLY)
                .build();

//		APICall callReport = APICall.reportBuilder().username(username).userIP(ip).userAgent(useragent)
//				.timingData(bdata).sessionId(uuid).build();

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
