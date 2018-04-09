package distudios.at.carcassonne;

import android.app.Application;

import distudios.at.carcassonne.engine.graphics.IGraphicsController;
import distudios.at.carcassonne.engine.logic.IGameController;
import distudios.at.carcassonne.gui.ILobbyController;
import distudios.at.carcassonne.networking.INetworkController;
import distudios.at.carcassonne.networking.NetworkController;

/**
 * Hold global game states here
 */
public class CarcassonneApp extends Application{

    private static IGraphicsController graphicsController;
    private static INetworkController networkController;
    private static ILobbyController lobbyController;
    private static IGameController gameController;

    @Override
    public void onCreate() {
        super.onCreate();

        networkController = new NetworkController();
    }

    public static void setGraphicsController(IGraphicsController controller) {
        graphicsController = controller;
    }

    public static IGraphicsController getGraphicsController() {
        return graphicsController;
    }

    public static void setNetworkController(INetworkController controller) {
        networkController = controller;
    }

    public static INetworkController getNetworkController() {
        return networkController;
    }

    public static void setLobbyController(ILobbyController controller) {
        lobbyController = controller;
    }

    public static ILobbyController getLobbyController() {
        return lobbyController;
    }

    public static void setGameController(IGameController controller) {
        gameController = controller;
    }

    public static IGameController getGameController() {
        return gameController;
    }
}
