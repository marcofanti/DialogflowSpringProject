import java.util.List;


@SuppressWarnings("checkstyle:membername")
public final class JsonMessage {
	  public final Card hidden_card;
	  public final List<Card> visible_cards;

	  public JsonMessage(Card hiddenCard, List<Card> visibleCards) {
	    this.hidden_card = hiddenCard;
	    this.visible_cards = visibleCards;
	  }

	  @Override public String toString() {
	    return "hidden=" + hidden_card + ",visible=" + visible_cards;
	  }

}
