public final class Card {
  public final char rank;

  public Card(char rank) {
    this.rank = rank;
  }

  @Override public String toString() {
    return String.format("%s%s", rank, "");
  }
}