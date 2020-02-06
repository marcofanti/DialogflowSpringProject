/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.behaviosec.chat;

// Imports the Google Cloud client library

import com.google.cloud.dialogflow.v2.DetectIntentResponse;
import com.google.cloud.dialogflow.v2.QueryInput;
import com.google.cloud.dialogflow.v2.QueryResult;
import com.google.cloud.dialogflow.v2.SessionName;
import com.google.cloud.dialogflow.v2.SessionsClient;
import com.google.cloud.dialogflow.v2.TextInput;
import com.google.cloud.dialogflow.v2.TextInput.Builder;
import com.google.common.collect.Maps;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * DialogFlow API Detect Intent sample with text inputs.
 */
public class DetectIntentTexts {
	public static void main(String[] args) {
		DetectIntentTexts detectIntentTexts = new DetectIntentTexts();
		UUID uuid = UUID.randomUUID();
        String sessionId = uuid.toString();
        String projectId = "banking-dggfcs";
        String languageCode = "en";
        List<String> texts = Arrays.asList(new String[]{"How smart are you?", "Who are you?"});
        String text = new String("Hi");
        
        try {
//			Map<String, QueryResult> result = detectIntentTexts(projectId, texts, sessionId, languageCode);
//			System.out.println(result.toString());
			Map<String, QueryResult> resultSingle = detectIntentTexts(projectId, text, sessionId, languageCode);
			System.out.println(resultSingle.keySet() + "\n\n\n");
			for (String key: resultSingle.keySet()) {
				System.out.println(resultSingle.get(key));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static Map<String, QueryResult> detectIntentTexts(String projectId, String text, String sessionId,
			String languageCode) throws Exception {
		Map<String, QueryResult> queryResults = Maps.newHashMap();
		// Instantiates a client
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

			System.out.println("====================");
			System.out.format("Query Text: '%s'\n", queryResult.getQueryText());
			System.out.format("Detected Intent: %s (confidence: %f)\n", queryResult.getIntent().getDisplayName(),
					queryResult.getIntentDetectionConfidence());
			System.out.format("Fulfillment Text: '%s'\n", queryResult.getFulfillmentText());

			queryResults.put(text, queryResult);
		}
		return queryResults;
	}

	/**
	 * Returns the result of detect intent with texts as inputs.
	 *
	 * Using the same `session_id` between requests allows continuation of the
	 * conversation.
	 *
	 * @param projectId    Project/Agent Id.
	 * @param texts        The text intents to be detected based on what a user
	 *                     says.
	 * @param sessionId    Identifier of the DetectIntent session.
	 * @param languageCode Language code of the query.
	 * @return The QueryResult for each input text.
	 */
	public static Map<String, QueryResult> detectIntentTexts(String projectId, List<String> texts, String sessionId,
			String languageCode) throws Exception {
		Map<String, QueryResult> queryResults = Maps.newHashMap();
		// Instantiates a client
		try (SessionsClient sessionsClient = SessionsClient.create()) {
			// Set the session name using the sessionId (UUID) and projectID (my-project-id)
			SessionName session = SessionName.of(projectId, sessionId);
			System.out.println("Session Path: " + session.toString());

			// Detect intents for each text input
			for (String text : texts) {
				// Set the text (hello) and language code (en-US) for the query
				Builder textInput = TextInput.newBuilder().setText(text).setLanguageCode(languageCode);

				// Build the query with the TextInput
				QueryInput queryInput = QueryInput.newBuilder().setText(textInput).build();

				// Performs the detect intent request
				DetectIntentResponse response = sessionsClient.detectIntent(session, queryInput);

				// Display the query result
				QueryResult queryResult = response.getQueryResult();

				System.out.println("====================");
				System.out.format("Query Text: '%s'\n", queryResult.getQueryText());
				System.out.format("Detected Intent: %s (confidence: %f)\n", queryResult.getIntent().getDisplayName(),
						queryResult.getIntentDetectionConfidence());
				System.out.format("Fulfillment Text: '%s'\n", queryResult.getFulfillmentText());

				queryResults.put(text, queryResult);
			}
		}
		return queryResults;
	}
	// [END dialogflow_detect_intent_text]
}