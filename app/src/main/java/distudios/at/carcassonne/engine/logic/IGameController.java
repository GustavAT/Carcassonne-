package distudios.at.carcassonne.engine.logic;

public interface IGameController {

    void init();

    void action();

    boolean actionCardPlacement(Card card);

    void dataReceived(Object data, int type);

}
