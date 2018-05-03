package distudios.at.carcassonne.engine.logic;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonObject
public class GameState implements Serializable {

    @JsonField
    public ArrayList<Card> cards;
    @JsonField
    public List<Object> peeps;
    @JsonField
    public ArrayList<Integer> stack;
    @JsonField
    public List<Integer> points;
    @JsonField
    public int maxPlayerCount;
    @JsonField
    public int currentPlayer;

    public GameState() {
        cards = new ArrayList<>();
        peeps = new ArrayList<>();
        stack = new ArrayList<>();
        points = new ArrayList<>(5);
        maxPlayerCount = 0;
        currentPlayer = 0;
    }

    public void setStack(ArrayList<Integer> stack) {
        this.stack = stack;
    }

    public ArrayList<Integer> getStack() {
        return stack;
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public ArrayList<Card> getCards() { return cards; }

    public void addPeep(Object peep) {
        peeps.add(peep);
    }

    public void removePeep(Object peep) {
        peeps.remove(peep);
    }

    public int getPoints(int player) {
        return points.get(player);
    }

    public void setPoints(int player, int newPoints) {
        points.set(player, newPoints);
    }

    /**
     * Get the player number for the next turn
     * @return
     */
    public int getNextPlayer() {
        return (currentPlayer++) % maxPlayerCount;
    }

    /**
     * Check if its my turn
     * @param playerNumber my player number
     * @return
     */
    public boolean myTurn(int playerNumber) {
        return currentPlayer == playerNumber;
    }

}
