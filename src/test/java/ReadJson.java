import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
//import com.squareup.moshi.recipes.models.BlackjackHand;

public final class ReadJson {
	public void run() throws Exception {
		String json = "" + "{\n" + "  \"hidden_card\": {\n" + "    \"rank\": \"6\",\n" + "    \"suit\": \"SPADES\"\n"
				+ "  },\n" + "  \"visible_cards\": [\n" + "    {\n" + "      \"rank\": \"4\",\n"
				+ "      \"suit\": \"CLUBS\"\n" + "    },\n" + "    {\n" + "      \"rank\": \"A\",\n"
				+ "      \"suit\": \"HEARTS\"\n" + "    }\n" + "  ]\n" + "}\n";

		Moshi moshi = new Moshi.Builder().build();
		JsonAdapter<JsonMessage> jsonAdapter = moshi.adapter(JsonMessage.class);

		JsonMessage blackjackHand = jsonAdapter.fromJson(json);
		System.out.println(blackjackHand);
	}

	public static void main(String[] args) throws Exception {
		new ReadJson().run();
	}	
}