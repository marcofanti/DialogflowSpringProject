package com.behaviosec.utils;

import java.io.IOException;

import org.springframework.boot.json.JacksonJsonParser;

import com.behaviosec.chat.DialogFlowController;
import com.behaviosec.chat.Message;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class ParseUtils {
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ParseUtils.class);

    private static final JacksonJsonParser parse = new JacksonJsonParser();
    private static final JsonFactory factory = new JsonFactory();

	public static Message parseMessage(String originalBody, Message message, boolean isDialogFlow) {
		log.debug("***************   originalBody\n\n" + originalBody  + "\n\n\n");

		if (originalBody == null || originalBody.length() == 0) {
			return message;
		}
		
		String bdata = null;
		String text = null;
		String newBody = null;
		
		
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
					if ("text".equals(fieldName)) {
						System.out.println(fieldName + parser.getValueAsString());
						text = parser.getValueAsString();
						if (text == null || text.equals("null")) {
							text = null;
							log.debug("text is null");
						} else {
							log.debug("********** text is set" + text);
						}
					}
				}
			} catch (IOException e) {
			}
		}

		if (isDialogFlow) {
			//int len = "{\"input\":{\"message_type\":\"text\",\"text\":\"".length();
			//int tot = newBody.length();
			//newBody = newBody.substring(len, tot - 3);
			newBody =  text; //"{\"input\":{\"message_type\":\"text\",\"text\":\"" + text + "\"}}";
		}
		message.setMessage(newBody);
//		bdata = "[[\"m\",\"n\",{\"vendorSub\":\"\",\"productSub\":\"20030107\",\"vendor\":\"Google Inc.\",\"maxTouchPoints\":1,\"hardwareConcurrency\":16,\"cookieEnabled\":true,\"appCodeName\":\"Mozilla\",\"appName\":\"Netscape\",\"appVersion\":\"5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1\",\"platform\":\"MacIntel\",\"product\":\"Gecko\",\"userAgent\":\"Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1\",\"language\":\"en-US\",\"languages\":[\"en-US\",\"en\"],\"onLine\":true,\"doNotTrack\":null,\"geolocation\":{},\"mediaCapabilities\":{},\"connection\":{},\"webkitTemporaryStorage\":{},\"webkitPersistentStorage\":{},\"userActivation\":{},\"mediaSession\":{},\"permissions\":{},\"deviceMemory\":8,\"clipboard\":{},\"credentials\":{},\"keyboard\":{},\"locks\":{},\"mediaDevices\":{},\"serviceWorker\":{},\"storage\":{},\"presentation\":{},\"bluetooth\":{},\"usb\":{}}],[\"m\",\"s\",{\"availWidth\":375,\"availHeight\":812,\"width\":375,\"height\":812,\"colorDepth\":24,\"pixelDepth\":24,\"availLeft\":0,\"availTop\":0,\"orientation\":{}}],[\"m\",\"v\",261],[\"m\",\"e\",{\"ptype\":null,\"ptypes\":{\"touch\":2},\"k229\":0,\"kn\":0,\"tz\":480,\"pr\":3,\"u\":{\"7\":1,\"8\":10,\"9\":29,\"10\":25,\"11\":20,\"12\":12,\"13\":3},\"f\":[1,\"[object ServiceWorkerContainer]\",\"[object Geolocation]\",\"true\",\"undefined\",\"20030107\",\"undefined\",\"true\",\"[object WebGLRenderingContext]\",\"function HTMLCanvasElement() { [native code] }\",\"true\",\"false\",\"null\",\"MacIntel\",\"undefined\",\"true\",\"false\",\"true\",\"true\",\"{\\\"0\\\":\\\"Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1\\\",\\\"1\\\":\\\"en-US\\\",\\\"2\\\":\\\"true\\\",\\\"3\\\":\\\"812x375x24\\\",\\\"4\\\":\\\"24\\\",\\\"5\\\":\\\"\\\",\\\"6\\\":\\\"\\\",\\\"7\\\":\\\"\\\",\\\"8\\\":\\\"480\\\",\\\"9\\\":\\\"true\\\",\\\"10\\\":\\\"true\\\",\\\"11\\\":\\\"3\\\",\\\"12\\\":{}}\"]}],[\"m\",\"k\",{},[[\"Z\",49,1578952924553],[\"U\",2070,\"text#username\"],[\"r\",2070,\"\"],[\"v\",6428,\"\"],[\"n\",6428,\"text#username\"],[\"U\",8732,\"text#username\"],[\"r\",8732,\"\"],[\"v\",17219,\"\"],[\"n\",17219,\"text#username\"],[\"D\",17235,\"DIV##LI#\"],[\"U\",17235,\"text#username\"],[\"E\",17236,\"DIV##LI#\"]]],[\"c\",[[\"t\",\"DIV##LI#\",17230],[\"v\",375,812,17230],[\"md\",187.97265625,148.2734375,17230,0],[\"mu\",187.97265625,148.2734375,17234,0]],\"/\"],[\"w\",[{\"text#username\":0},{\"movement\":0}],\"/\"]]";
//		bdata = "[[\"m\",\"n\",{\"vendorSub\":\"\",\"productSub\":\"20030107\",\"vendor\":\"Google Inc.\",\"maxTouchPoints\":1,\"hardwareConcurrency\":16,\"cookieEnabled\":true,\"appCodeName\":\"Mozilla\",\"appName\":\"Netscape\",\"appVersion\":\"5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1\",\"platform\":\"MacIntel\",\"product\":\"Gecko\",\"userAgent\":\"Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1\",\"language\":\"en-US\",\"languages\":[\"en-US\",\"en\"],\"onLine\":true,\"doNotTrack\":null,\"geolocation\":{},\"mediaCapabilities\":{},\"connection\":{},\"webkitTemporaryStorage\":{},\"webkitPersistentStorage\":{},\"userActivation\":{},\"mediaSession\":{},\"permissions\":{},\"deviceMemory\":8,\"clipboard\":{},\"credentials\":{},\"keyboard\":{},\"locks\":{},\"mediaDevices\":{},\"serviceWorker\":{},\"storage\":{},\"presentation\":{},\"bluetooth\":{},\"usb\":{}}],[\"m\",\"s\",{\"availWidth\":375,\"availHeight\":812,\"width\":375,\"height\":812,\"colorDepth\":24,\"pixelDepth\":24,\"availLeft\":0,\"availTop\":0,\"orientation\":{}}],[\"m\",\"v\",261],[\"m\",\"e\",{\"ptype\":null,\"ptypes\":{\"touch\":8},\"k229\":0,\"kn\":0,\"tz\":480,\"pr\":3,\"u\":{\"8\":7,\"9\":16,\"10\":51,\"11\":20,\"12\":6},\"f\":[1,\"[object ServiceWorkerContainer]\",\"[object Geolocation]\",\"true\",\"undefined\",\"20030107\",\"undefined\",\"true\",\"[object WebGLRenderingContext]\",\"function HTMLCanvasElement() { [native code] }\",\"true\",\"false\",\"null\",\"MacIntel\",\"undefined\",\"true\",\"false\",\"true\",\"true\",\"{\\\"0\\\":\\\"Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1\\\",\\\"1\\\":\\\"en-US\\\",\\\"2\\\":\\\"true\\\",\\\"3\\\":\\\"812x375x24\\\",\\\"4\\\":\\\"24\\\",\\\"5\\\":\\\"\\\",\\\"6\\\":\\\"\\\",\\\"7\\\":\\\"\\\",\\\"8\\\":\\\"480\\\",\\\"9\\\":\\\"true\\\",\\\"10\\\":\\\"true\\\",\\\"11\\\":\\\"3\\\",\\\"12\\\":{}}\"]}],[\"m\",\"k\",{},[[\"Z\",12,1578958140759],[\"D\",3456,\"DIV##LI#\"],[\"U\",3456,\"text#username\"],[\"E\",3456,\"DIV##LI#\"],[\"D\",7476,\"DIV##LI#\"],[\"E\",7477,\"DIV##LI#\"],[\"D\",10346,\"DIV##LI#\"],[\"E\",10346,\"DIV##LI#\"],[\"D\",15687,\"text#username\"],[\"n\",15687,\"text#username\"],[\"E\",15688,\"text#username\"],[\"an\",18395,\"text#username\"],[\"sn\",18631,\"text#username\"]]],[\"c\",[[\"t\",\"DIV##LI#\",3448],[\"v\",375,812,3448],[\"md\",242.44921875,167.453125,3448,0],[\"mu\",242.44921875,167.453125,3454,0],[\"t\",\"DIV##LI#\",7469],[\"md\",157.63671875,187.4765625,7469,0],[\"mu\",157.63671875,187.4765625,7475,0],[\"t\",\"DIV##LI#\",10339],[\"md\",177.71484375,152.80859375,10339,0],[\"mu\",177.71484375,152.80859375,10345,0],[\"t\",\"INPUT#textInput#LABEL#\",15680],[\"md\",160.93359375,593.4765625,15680,0],[\"mu\",160.93359375,593.4765625,15686,0]],\"/\"],[\"w\",[{\"text#username\":21},{\"movement\":0}],\"/\"]]\r\n";
		message.setBdata(bdata);

		return message;
	}

	public static Message parseResponse(String jsonText, Message message, boolean isDialogFlow) {
		log.debug("***************   originalBody\n\n" + jsonText  + "\n\n\n");

		if (jsonText == null || jsonText.length() == 0) {
			return message;
		}
		
		String bdata = null;
		String text = null;
		String newBody = null;
		
		
		JsonParser parser = null;
		try {
			parser = factory.createParser(jsonText.toString());
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
					if ("text".equals(fieldName)) {
						System.out.println(fieldName + parser.getValueAsString());
						text = parser.getValueAsString();
						if (text == null || text.equals("null")) {
							text = null;
							log.debug("text is null");
						} else {
							log.debug("********** text is set" + text);
						}
					}
				}
			} catch (IOException e) {
			}
		}

		if (isDialogFlow) {
			//int len = "{\"input\":{\"message_type\":\"text\",\"text\":\"".length();
			//int tot = newBody.length();
			//newBody = newBody.substring(len, tot - 3);
			newBody =  text; //"{\"input\":{\"message_type\":\"text\",\"text\":\"" + text + "\"}}";
		}
		message.setMessage(newBody);
//		bdata = "[[\"m\",\"n\",{\"vendorSub\":\"\",\"productSub\":\"20030107\",\"vendor\":\"Google Inc.\",\"maxTouchPoints\":1,\"hardwareConcurrency\":16,\"cookieEnabled\":true,\"appCodeName\":\"Mozilla\",\"appName\":\"Netscape\",\"appVersion\":\"5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1\",\"platform\":\"MacIntel\",\"product\":\"Gecko\",\"userAgent\":\"Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1\",\"language\":\"en-US\",\"languages\":[\"en-US\",\"en\"],\"onLine\":true,\"doNotTrack\":null,\"geolocation\":{},\"mediaCapabilities\":{},\"connection\":{},\"webkitTemporaryStorage\":{},\"webkitPersistentStorage\":{},\"userActivation\":{},\"mediaSession\":{},\"permissions\":{},\"deviceMemory\":8,\"clipboard\":{},\"credentials\":{},\"keyboard\":{},\"locks\":{},\"mediaDevices\":{},\"serviceWorker\":{},\"storage\":{},\"presentation\":{},\"bluetooth\":{},\"usb\":{}}],[\"m\",\"s\",{\"availWidth\":375,\"availHeight\":812,\"width\":375,\"height\":812,\"colorDepth\":24,\"pixelDepth\":24,\"availLeft\":0,\"availTop\":0,\"orientation\":{}}],[\"m\",\"v\",261],[\"m\",\"e\",{\"ptype\":null,\"ptypes\":{\"touch\":2},\"k229\":0,\"kn\":0,\"tz\":480,\"pr\":3,\"u\":{\"7\":1,\"8\":10,\"9\":29,\"10\":25,\"11\":20,\"12\":12,\"13\":3},\"f\":[1,\"[object ServiceWorkerContainer]\",\"[object Geolocation]\",\"true\",\"undefined\",\"20030107\",\"undefined\",\"true\",\"[object WebGLRenderingContext]\",\"function HTMLCanvasElement() { [native code] }\",\"true\",\"false\",\"null\",\"MacIntel\",\"undefined\",\"true\",\"false\",\"true\",\"true\",\"{\\\"0\\\":\\\"Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1\\\",\\\"1\\\":\\\"en-US\\\",\\\"2\\\":\\\"true\\\",\\\"3\\\":\\\"812x375x24\\\",\\\"4\\\":\\\"24\\\",\\\"5\\\":\\\"\\\",\\\"6\\\":\\\"\\\",\\\"7\\\":\\\"\\\",\\\"8\\\":\\\"480\\\",\\\"9\\\":\\\"true\\\",\\\"10\\\":\\\"true\\\",\\\"11\\\":\\\"3\\\",\\\"12\\\":{}}\"]}],[\"m\",\"k\",{},[[\"Z\",49,1578952924553],[\"U\",2070,\"text#username\"],[\"r\",2070,\"\"],[\"v\",6428,\"\"],[\"n\",6428,\"text#username\"],[\"U\",8732,\"text#username\"],[\"r\",8732,\"\"],[\"v\",17219,\"\"],[\"n\",17219,\"text#username\"],[\"D\",17235,\"DIV##LI#\"],[\"U\",17235,\"text#username\"],[\"E\",17236,\"DIV##LI#\"]]],[\"c\",[[\"t\",\"DIV##LI#\",17230],[\"v\",375,812,17230],[\"md\",187.97265625,148.2734375,17230,0],[\"mu\",187.97265625,148.2734375,17234,0]],\"/\"],[\"w\",[{\"text#username\":0},{\"movement\":0}],\"/\"]]";
//		bdata = "[[\"m\",\"n\",{\"vendorSub\":\"\",\"productSub\":\"20030107\",\"vendor\":\"Google Inc.\",\"maxTouchPoints\":1,\"hardwareConcurrency\":16,\"cookieEnabled\":true,\"appCodeName\":\"Mozilla\",\"appName\":\"Netscape\",\"appVersion\":\"5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1\",\"platform\":\"MacIntel\",\"product\":\"Gecko\",\"userAgent\":\"Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1\",\"language\":\"en-US\",\"languages\":[\"en-US\",\"en\"],\"onLine\":true,\"doNotTrack\":null,\"geolocation\":{},\"mediaCapabilities\":{},\"connection\":{},\"webkitTemporaryStorage\":{},\"webkitPersistentStorage\":{},\"userActivation\":{},\"mediaSession\":{},\"permissions\":{},\"deviceMemory\":8,\"clipboard\":{},\"credentials\":{},\"keyboard\":{},\"locks\":{},\"mediaDevices\":{},\"serviceWorker\":{},\"storage\":{},\"presentation\":{},\"bluetooth\":{},\"usb\":{}}],[\"m\",\"s\",{\"availWidth\":375,\"availHeight\":812,\"width\":375,\"height\":812,\"colorDepth\":24,\"pixelDepth\":24,\"availLeft\":0,\"availTop\":0,\"orientation\":{}}],[\"m\",\"v\",261],[\"m\",\"e\",{\"ptype\":null,\"ptypes\":{\"touch\":8},\"k229\":0,\"kn\":0,\"tz\":480,\"pr\":3,\"u\":{\"8\":7,\"9\":16,\"10\":51,\"11\":20,\"12\":6},\"f\":[1,\"[object ServiceWorkerContainer]\",\"[object Geolocation]\",\"true\",\"undefined\",\"20030107\",\"undefined\",\"true\",\"[object WebGLRenderingContext]\",\"function HTMLCanvasElement() { [native code] }\",\"true\",\"false\",\"null\",\"MacIntel\",\"undefined\",\"true\",\"false\",\"true\",\"true\",\"{\\\"0\\\":\\\"Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1\\\",\\\"1\\\":\\\"en-US\\\",\\\"2\\\":\\\"true\\\",\\\"3\\\":\\\"812x375x24\\\",\\\"4\\\":\\\"24\\\",\\\"5\\\":\\\"\\\",\\\"6\\\":\\\"\\\",\\\"7\\\":\\\"\\\",\\\"8\\\":\\\"480\\\",\\\"9\\\":\\\"true\\\",\\\"10\\\":\\\"true\\\",\\\"11\\\":\\\"3\\\",\\\"12\\\":{}}\"]}],[\"m\",\"k\",{},[[\"Z\",12,1578958140759],[\"D\",3456,\"DIV##LI#\"],[\"U\",3456,\"text#username\"],[\"E\",3456,\"DIV##LI#\"],[\"D\",7476,\"DIV##LI#\"],[\"E\",7477,\"DIV##LI#\"],[\"D\",10346,\"DIV##LI#\"],[\"E\",10346,\"DIV##LI#\"],[\"D\",15687,\"text#username\"],[\"n\",15687,\"text#username\"],[\"E\",15688,\"text#username\"],[\"an\",18395,\"text#username\"],[\"sn\",18631,\"text#username\"]]],[\"c\",[[\"t\",\"DIV##LI#\",3448],[\"v\",375,812,3448],[\"md\",242.44921875,167.453125,3448,0],[\"mu\",242.44921875,167.453125,3454,0],[\"t\",\"DIV##LI#\",7469],[\"md\",157.63671875,187.4765625,7469,0],[\"mu\",157.63671875,187.4765625,7475,0],[\"t\",\"DIV##LI#\",10339],[\"md\",177.71484375,152.80859375,10339,0],[\"mu\",177.71484375,152.80859375,10345,0],[\"t\",\"INPUT#textInput#LABEL#\",15680],[\"md\",160.93359375,593.4765625,15680,0],[\"mu\",160.93359375,593.4765625,15686,0]],\"/\"],[\"w\",[{\"text#username\":21},{\"movement\":0}],\"/\"]]\r\n";
		message.setBdata(bdata);

		return message;
	}

	public static String parseTimingData(String bdata) {
		if (bdata == null || bdata.length() == 0) {
			return DialogFlowController.userAgent;
		}
		
		String userAgent = DialogFlowController.userAgent;
		
		
		JsonParser parser = null;
		try {
			parser = factory.createParser(bdata);
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
					jsonToken = parser.nextToken();
					if ("userAgent".equals(fieldName)) {
						userAgent = parser.getValueAsString();
						log.debug(fieldName + " = " + userAgent);
						break;
					}
				}
			} catch (IOException e) {
			}
		}
		return userAgent;
	}
}
