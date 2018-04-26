package distudios.at.carcassonne.engine.logic;

public interface IGameEngine {

    void init(Orientation orientation);

    void placeCard(Card card);

    boolean checkPlaceable(Card card);
}
