package com.behaviosec.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Value;
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
import com.behaviosec.utils.JsonUtils;
import com.behaviosec.utils.ParseUtils;
import com.google.cloud.dialogflow.v2.DetectIntentResponse;
import com.google.cloud.dialogflow.v2.QueryInput;
import com.google.cloud.dialogflow.v2.QueryResult;
import com.google.cloud.dialogflow.v2.SessionName;
import com.google.cloud.dialogflow.v2.SessionsClient;
import com.google.cloud.dialogflow.v2.TextInput;
import com.google.cloud.dialogflow.v2.TextInput.Builder;

@RestController
public class DialogFlowController {
	@Value( "${chat.user.useragent}" )
	public String userAgent;
	@Value( "${chat.projectId}" )
    private String projectId;
	@Value( "${chat.languageCode}" )
    private String languageCode;
	@Value( "${chat.behaviosec.url}" )
    private String url;
	@Value( "${chat.behaviosec.tenant.id}" )	
    private String tenantID;
	@Value( "${chat.training.mode}" )	
    private String trainingMode;
	@Value( "${chat.feedback.mode}" )	
	private String feedbackMode; //  none, score, privilege, both
	@Value( "${chat.type}" )	
    private String chatType;
	
	private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass().getName());
	static int maxRisk = 200;
    
    public static HashMap <String, Message> messages = new HashMap <String, Message>();
    
    public enum risk  
    { 
        low, medium, high; 
    } 
    
    public enum trainingMode  
    { 
        none, score, both, all; 
    } 
    
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public String proxyGet(HttpMethod method, HttpServletRequest request, HttpResponse response)
			throws URISyntaxException {
		String result = proxyPost(method, request, response);
		log.debug("Result = " + result);
		return result;
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public String proxyPost(HttpMethod method, HttpServletRequest request, HttpResponse response)
			throws URISyntaxException {

		String requestURI = request.getRequestURI();
		log.debug("requestURI is " + requestURI);
		
		String sessionId = "";
		// Only the first request creates sessions
		if (!requestURI.endsWith("/sessions")) {
			int sessionsIndex = requestURI.indexOf("/sessions/") + "/sessions/".length();
			int len = "afa30473-656c-4695-9e9c-42a290d6e7e2".length();
			sessionId = requestURI.substring(sessionsIndex, sessionsIndex + len);
		} else {
			UUID uuid = UUID.randomUUID();
			sessionId = uuid.toString();
			messages.put(sessionId, new Message(sessionId));
			String returnMessage = "{\r\n" + "  \"session_id\": \"" + sessionId + "\"\r\n" + "}";
			log.debug("Returning " + returnMessage);
			return returnMessage;
		}

		
		Message currentMessage = null;
		
		if (messages.containsKey(sessionId)) {
			currentMessage = messages.get(sessionId);
		} else {
			Set <String> keySet = messages.keySet();
			for (String key: keySet) {
				currentMessage = messages.get(key);
				log.debug("key " + key);
			}
		}
		
		if (currentMessage.getAuthorizedBefore() == -1) {
			StringBuilder body = getBody(request);
			String bodyString = body.toString();
			return validateUser(currentMessage, bodyString);
		}

		StringBuilder body = getBody(request);

		currentMessage.parseInput(body.toString());

		String defaultString = com.behaviosec.config.Constants.INITIAL_CHAT_RETURN_STRING_1 + currentMessage.getName() + 
				com.behaviosec.config.Constants.INITIAL_CHAT_RETURN_STRING_2;

		QueryResult queryResult = null;
		
		try {
			if (currentMessage.getMessage() == null || currentMessage.getMessage().trim().length() == 0) {
				log.debug("Returning defaultString\n" + defaultString.replaceAll("\\r\\n|\\r|\\n", " "));
				return defaultString;
			}
			
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
			
			risk intentRisk = risk.low;
			
			if (queryResult.getIntent().getDisplayName().startsWith("account.balance")) {
				currentMessage.setBdata(currentMessage.getBdata().replaceAll("text#username", "text#balance"));					
				if (queryResult.getIntent().getDisplayName().startsWith("account.balance") && 
						queryResult.getFulfillmentText().startsWith("Here's your latest balance")) {
					fulfillmentText = "Here's your latest balance: $12,435.87";
				} 
				intentRisk = risk.medium;
			} else if (queryResult.getIntent().getDisplayName().startsWith("account.open")) {
				currentMessage.setBdata(currentMessage.getBdata().replaceAll("text#username", "text#open"));
				currentMessage.setBdata(currentMessage.getBdata().replaceAll("CREDIT_INPUT", "OPEN_INPUT"));			
				intentRisk = risk.low;
			} else if (queryResult.getIntent().getDisplayName().startsWith("payment.due_date")) {
				currentMessage.setBdata(currentMessage.getBdata().replaceAll("text#username", "text#payment"));
				currentMessage.setBdata(currentMessage.getBdata().replaceAll("CREDIT_INPUT", "PAYMENT_INPUT"));			
				fulfillmentText = "The due date is: March 15, 2020";
				intentRisk = risk.low;
			} else if (queryResult.getIntent().getDisplayName().equals("")) {
				currentMessage.setBdata(currentMessage.getBdata().replaceAll("text#username", "text#generic"));
				currentMessage.setBdata(currentMessage.getBdata().replaceAll("CREDIT_INPUT", "GENERIC_INPUT"));			
				intentRisk = risk.low;
			} else if (queryResult.getIntent().getDisplayName().startsWith("transfer.money")) {
				currentMessage.setBdata(currentMessage.getBdata().replaceAll("text#username", "text#transfer"));
				currentMessage.setBdata(currentMessage.getBdata().replaceAll("CREDIT_INPUT", "TRANSFER_INPUT"));			
				if (queryResult.getFulfillmentText().startsWith("transfer.money") ||
//							queryResult.getFulfillmentText().startsWith("Sure. Transfer from which account") ||
						queryResult.getFulfillmentText().startsWith("To which account") ||
						queryResult.getFulfillmentText().startsWith("And, how much do you want to transfer") ||
						queryResult.getFulfillmentText().startsWith("All right. So, you're transferring") || 
						queryResult.getFulfillmentText().startsWith("Okay, I have processed your transfer. Your confirmation number is")) {
					if (queryResult.getFulfillmentText().startsWith("Okay, I have processed your transfer. Your confirmation number is")) {
						fulfillmentText = "Okay, I have processed your transfer. Your confirmation number is " + "Y1234Z";
					}
					currentMessage.setBdata(currentMessage.getBdata().replaceAll("text#transfer", "text#generic"));
					currentMessage.setBdata(currentMessage.getBdata().replaceAll("TRANSFER_INPUT", "GENERIC_INPUT"));			
					intentRisk = risk.low;
				} else {
					intentRisk = risk.high;
				}
			} 
			
			Report r = null; //currentMessage.getReport();
			if (r == null) {
				if (currentMessage.getBdata() != null && currentMessage.getBdata().length() > 0) {
					currentMessage.setReport(getReport(currentMessage.getBdata(), currentMessage.getEmail(),
							ParseUtils.parseTimingData(currentMessage.getBdata(), userAgent), "127.0.0.1", currentMessage.getSessionID()));
					r = currentMessage.getReport();
				}
			}
			log.debug("intent risk: " + intentRisk);
			log.debug("training mode: " + currentMessage.getTrainingMode());
			
			if (currentMessage.getTrainingMode().equals(com.behaviosec.config.Constants.TRAINING_MODE_FALSE)) {
				if ((intentRisk == risk.medium || intentRisk == risk.high) && (r != null && r.toString().indexOf("diDesc:") > 0)) {
					int st = r.toString().indexOf("diDesc:");
					String diDesc = r.toString().substring(st);
					log.debug(diDesc);
					if (r.toString().substring(st).contains(", 3") || r.toString().substring(st).indexOf("[3") > 0) {
						currentMessage.setAuthorizedBefore(-1);
						currentMessage.setRisk(intentRisk);
						currentMessage.setResponse(JsonUtils.createJsonResponse(fulfillmentText, r, intentRisk, currentMessage.getFeedbackMode()));
						String newResult = JsonUtils.createJsonResponse(currentMessage.getName() + com.behaviosec.config.Constants.VERIFY_MESSAGE_3, 
								r, currentMessage.getRisk(), currentMessage.getFeedbackMode());
						log.debug("returning " + newResult);
						return newResult;
					}
					if (r.toString().substring(st).contains(", 0") || r.toString().substring(st).indexOf("[0") > 0) {
						currentMessage.setAuthorizedBefore(-1);
						currentMessage.setRisk(intentRisk);
						currentMessage.setResponse(JsonUtils.createJsonResponse(fulfillmentText, r, intentRisk, currentMessage.getFeedbackMode()));
						String newResult = JsonUtils.createJsonResponse(currentMessage.getName() + com.behaviosec.config.Constants.VERIFY_MESSAGE_2, 
								r, currentMessage.getRisk(), currentMessage.getFeedbackMode());
						log.debug("returning " + newResult);
						return newResult;
					}
				} 
	
				if ((intentRisk == risk.medium || intentRisk == risk.high) && (r == null || r.getScore() < 80 || r.getConfidence() < 50 || r.getRisk() > maxRisk
						|| currentMessage.getMessage().equals(
								"{\"input\":{\"message_type\":\"text\",\"text\":\"I want to make a credit card payment\"}}"))) {
					currentMessage.setAuthorizedBefore(-1);
					currentMessage.setRisk(intentRisk);
					currentMessage.setResponse(JsonUtils.createJsonResponse(fulfillmentText, r, intentRisk, currentMessage.getFeedbackMode()));
					String newResult = JsonUtils.createJsonResponse(currentMessage.getName() + com.behaviosec.config.Constants.VERIFY_MESSAGE_1, 
							r, currentMessage.getRisk(), currentMessage.getFeedbackMode());
					log.debug("returning " + newResult);
					return newResult;
				}
			} else {
				log.debug("Training mode is true");
			}
			
			return JsonUtils.createJsonResponse(fulfillmentText, r, intentRisk, currentMessage.getFeedbackMode());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return defaultString;
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

	private String validateUser(Message currentMessage, String body) {
		Message thisMessage = new Message();

		thisMessage.parseInput(body);
		thisMessage.copy(currentMessage);

		if (thisMessage.getMessage() != null && thisMessage.getMessage().length() == 6) {			
			if (NumberUtils.isDigits(thisMessage.getMessage())) {
				return currentMessage.getMessage();
			}
		}
		
		if (thisMessage.getMessage() != null && (thisMessage.getMessage() == currentMessage.getEmail() || thisMessage.getMessage().startsWith("mfanti"))) {
			if (thisMessage.getBdata() != null && thisMessage.getBdata().length() > 0) {
				Report r = getReport(thisMessage.getBdata(), thisMessage.getEmail(),
						ParseUtils.parseTimingData(thisMessage.getBdata(), userAgent),
						"127.0.0.1", thisMessage.getSessionID());
				if (r == null || r.getScore() < 80 || r.getConfidence() < 50 || r.getRisk() > maxRisk || r.isTabAnomaly()) {
					String newResult = JsonUtils.createJsonResponse("Sorry " + currentMessage.getName() + com.behaviosec.config.Constants.VERIFY_MESSAGE_4, r, 
							currentMessage.getRisk(), currentMessage.getFeedbackMode());
					log.debug("returning " + newResult);
					return newResult;
				} else {
					currentMessage.setReport(r);
					currentMessage.setAuthorizedBefore(0);
					return JsonUtils.createJsonResponse(currentMessage.getResponse(), r, currentMessage.getRisk(), currentMessage.getFeedbackMode());
				}
			}
		}

		String newResult = JsonUtils.createJsonResponse("Sorry " + currentMessage.getName() + com.behaviosec.config.Constants.VERIFY_MESSAGE_4, 
				null, currentMessage.getRisk(), currentMessage.getFeedbackMode());
		log.debug("returning " + newResult);
		// Put the old message back
		currentMessage.setMessage(currentMessage.getOldMessage());
		return newResult;
	}


	public Report getReport(String bdata, String username, String useragent, String clientIp, String uuid) {

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
