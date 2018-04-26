package distudios.at.carcassonne.engine.logic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameState implements Serializable {

    private ArrayList<Card> cards;
    private List<Object> peeps;
    private ArrayList<Integer> stack;
    private List<Integer> points;

    public GameState() {
        cards = new ArrayList<>();
        peeps = new ArrayList<>();
        stack = new ArrayList<>();
        points = new ArrayList<>(5);
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

}
