package Games;

import Games.Card;
import Games.Color;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Transient;

import java.util.*;

@Entity
public class DeckManager {

    Stack<Card> deck = new Stack<>();
    @Id
    private String id = new ObjectId().toString();
    public DeckManager() {
        initialize();
    }

    public void initialize() {
        if (!deck.isEmpty()) deck.clear();
        for (Number num : Number.values()) {
            deck.push(new Card(Color.black, num, Suit.clubs));
            deck.push(new Card(Color.black, num, Suit.spades));
            deck.push(new Card(Color.red, num, Suit.diamonds));
            deck.push(new Card(Color.red, num, Suit.hearts));
        }
        shuffleDeck();
    }

    public void shuffleDeck() {
        long seed = System.nanoTime();
        Collections.shuffle(deck, new Random(seed));
    }

    public Card dealCard() {
        return deck.pop();
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (Card d : deck) {
            s.append(d.number + " of " + d.suit + " \n");
        }
        return s.toString();
    }

    public int howManyCards() {
        return deck.size();
    }


}

