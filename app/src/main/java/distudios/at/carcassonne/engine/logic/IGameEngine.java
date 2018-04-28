package distudios.at.carcassonne.engine.logic;

import java.util.ArrayList;

public interface IGameEngine {

    void init(Orientation orientation);

    void placeCard(Card card);

    boolean checkPlaceable(Card card);

    void addScore(int point, int player);

    ArrayList<Integer> getScoreChanges(Card card);
}
