import java.io.IOException;

import org.springframework.boot.json.JacksonJsonParser;

import com.behaviosec.chat.Message;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class TestParseMessage {
	private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass().getName());
	
	static String originalBody = "{\"input\":{\"message_type\":\"text\",\"text\":\"mfanti@behaviosec.com\",\"bdata\":[[\"m\",\"n\",{\"vendorSub\":\"\",\"productSub\":\"20030107\",\"vendor\":\"Google Inc.\",\"maxTouchPoints\":1,\"hardwareConcurrency\":16,\"cookieEnabled\":true,\"appCodeName\":\"Mozilla\",\"appName\":\"Netscape\",\"appVersion\":\"5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1\",\"platform\":\"MacIntel\",\"product\":\"Gecko\",\"userAgent\":\"Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1\",\"language\":\"en-US\",\"languages\":[\"en-US\",\"en\"],\"onLine\":true,\"doNotTrack\":null,\"geolocation\":{},\"mediaCapabilities\":{},\"connection\":{},\"webkitTemporaryStorage\":{},\"webkitPersistentStorage\":{},\"userActivation\":{},\"mediaSession\":{},\"permissions\":{},\"deviceMemory\":8,\"clipboard\":{},\"credentials\":{},\"keyboard\":{},\"locks\":{},\"mediaDevices\":{},\"serviceWorker\":{},\"storage\":{},\"presentation\":{},\"bluetooth\":{},\"usb\":{}}],[\"m\",\"s\",{\"availWidth\":375,\"availHeight\":812,\"width\":375,\"height\":812,\"colorDepth\":24,\"pixelDepth\":24,\"availLeft\":0,\"availTop\":0,\"orientation\":{}}],[\"m\",\"v\",261],[\"m\",\"e\",{\"ptype\":null,\"ptypes\":{\"touch\":4},\"k229\":0,\"kn\":0,\"tz\":480,\"pr\":3,\"u\":{\"7\":1,\"8\":12,\"9\":16,\"10\":37,\"11\":23,\"12\":11},\"f\":[1,\"[object ServiceWorkerContainer]\",\"[object Geolocation]\",\"true\",\"undefined\",\"20030107\",\"undefined\",\"true\",\"[object WebGLRenderingContext]\",\"function HTMLCanvasElement() { [native code] }\",\"true\",\"false\",\"null\",\"MacIntel\",\"undefined\",\"true\",\"false\",\"true\",\"true\",\"{\\\"0\\\":\\\"Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1\\\",\\\"1\\\":\\\"en-US\\\",\\\"2\\\":\\\"true\\\",\\\"3\\\":\\\"812x375x24\\\",\\\"4\\\":\\\"24\\\",\\\"5\\\":\\\"\\\",\\\"6\\\":\\\"\\\",\\\"7\\\":\\\"\\\",\\\"8\\\":\\\"480\\\",\\\"9\\\":\\\"true\\\",\\\"10\\\":\\\"true\\\",\\\"11\\\":\\\"3\\\",\\\"12\\\":{}}\"]}],[\"m\",\"k\",{},[[\"Z\",22,1578950170548],[\"U\",199,\"text#username\"],[\"r\",199,\"\"],[\"v\",118139,\"\"],[\"n\",118139,\"text#username\"],[\"D\",121568,\"DIV##LI#\"],[\"U\",121568,\"text#username\"],[\"E\",121568,\"DIV##LI#\"],[\"r\",121604,\"\"],[\"v\",123001,\"\"],[\"r\",125804,\"\"],[\"v\",150713,\"\"],[\"D\",150720,\"text#username\"],[\"n\",150720,\"text#username\"],[\"E\",150720,\"text#username\"],[\"an\",153298,\"text#username\"],[\"sn\",153530,\"text#username\"]]],[\"c\",[[\"t\",\"DIV##LI#\",121561],[\"v\",375,812,121561],[\"md\",205.81640625,149.51171875,121561,0],[\"mu\",205.81640625,149.51171875,121567,0],[\"t\",\"INPUT#textInput#LABEL#\",150714],[\"md\",54.859375,592.6953125,150714,0],[\"mu\",54.859375,592.6953125,150719,0]],\"/\"],[\"w\",[{\"text#username\":21},{\"movement\":0}],\"/\"]]}}";

	public static void main(String[] args) {
		TestParseMessage testParseMessage = new TestParseMessage();
		testParseMessage.parseMessage(originalBody, new Message());
	}

	private Message parseMessage(String originalBody, Message message) {	
		if (originalBody == null || originalBody.length() == 0) {
			return message;
		}
		int bdataIndex = originalBody.indexOf(",\"bdata\"");
		String bdata = null;
		String newBody = originalBody.substring(0, bdataIndex) + "}}";
		log.debug("New body = " + newBody);
		
		if (originalBody.length() > bdataIndex + 30) {
			bdata = originalBody.substring(bdataIndex + ",\"bdata\":".length(), originalBody.length() - 2);
		}
		
		log.debug("bdata " + bdata);
/*
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

		while(!parser.isClosed()){
		    JsonToken jsonToken = null;
			try {
				jsonToken = parser.nextToken();
				log.debug(jsonToken.name() + " " + parser.getCurrentName() + " " + jsonToken.toString());
			} catch (IOException e) {
			}
		}
*/
		message.setMessage(newBody);
		message.setBdata(bdata);
		
		return message;
	}
	

}
