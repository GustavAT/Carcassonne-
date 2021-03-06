package distudios.at.carcassonne.engine.logic;

import android.util.Log;
import android.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import distudios.at.carcassonne.CarcassonneApp;
import distudios.at.carcassonne.networking.INetworkController;
import distudios.at.carcassonne.networking.connection.CarcassonneMessage;
import distudios.at.carcassonne.networking.connection.PlayerInfo;

public class GameController implements IGameController {

    private Map<Integer, Player> playerHashMap;
    private IGameEngine gameEngine;
    /**
     * True if the player has placed his card in this turn
     */
    private boolean cardPlaced = false;
    private boolean isDebug = false;

    /**
     * Game state
     *
     * If its not this players turn: WAITING (do nothing)
     * else:
     * - DRAW_CARD (waiting for card drawing)
     * - PLACE_CARD (waiting for card placement)
     * - PLACE_FIGURE (waiting for figure placement)
     * - END_TURN (waiting for player to end turn)
     */
    private CState cState;
    private Card currentCard;



    private boolean isCheating = false;

    public boolean isCheating() {
        return isCheating;
    }
    public void setCheating(boolean cheating) {
        isCheating = cheating;
    }

    public GameController() {
        this.init();
    }

    private void init() {
        gameEngine = new GameEngine();
        gameEngine.init(Orientation.NORTH);
        cState = CState.WAITING;
    }

    @Override
    public void removeFromStack(Card c) {
        getGameState().removeFromStack(c);
    }

    @Override
    public Card drawCard() {
        if (cState == CState.DRAW_CARD) {
            return getGameState().drawCard();
        }
        return null;
    }

    @Override
    public List<Card> drawCards() {
        if (cState == CState.DRAW_CARD) {
            isCheating = true;
            return getGameState().drawCards();
        }
        return new ArrayList<>();
    }

    @Override
    public boolean placeCard(Card card) {
        if (cState != CState.PLACE_CARD) return false;

        if(gameEngine.checkPlaceable(card)){
            gameEngine.placeCard(card);
            removeFromStack(card);
            cState = CState.PLACE_FIGURE;
            return true;
        }
        return false;
    }

    @Override
    public List<PeepPosition> showPossibleFigurePos(Card card) {
        return gameEngine.getALLFigurePos(card);
    }

    @Override
    public List<Peep> getPlacedPeeps(Card c) {
        List<Peep> peeps = new ArrayList<>();

        for (Peep p : getGameState().peeps) {
            int cardId = p.getCardId();
            if (cardId == c.getId()) {
                peeps.add(p);
            }
        }

        return peeps;
    }

    @Override
    public boolean canPlacePeep() {
        return peepsLeft() < 10;
    }

    @Override
    public int peepsLeft() {
        return gameEngine.getPlayerPeeps(CarcassonneApp.getNetworkController().getDevicePlayerNumber());
    }

    @Override
    public boolean placeFigure(Card card, PeepPosition position) {
        if (cState != CState.PLACE_FIGURE) return false;

        int playerId = CarcassonneApp.getNetworkController().getDevicePlayerNumber();
        if (gameEngine.placePeep(card, position, playerId)) {
            cState = CState.END_TURN;
            return true;
        }
        return false;
    }


    @Override
    public void endTurn() {
        if ((cState != CState.END_TURN && cState != CState.PLACE_FIGURE) || !isMyTurn()) return;

        gameEngine.markAllCards();

        // ugly: does not solve the problem but the symptom
        try {
            checkPoints(currentCard);
        } catch (Exception e) {
            Log.e("SCORE", e.getMessage());
        }

        CarcassonneMessage message = new CarcassonneMessage(CarcassonneMessage.END_TURN);
        GameState state = getGameState();
        state.currentPlayer = state.getNextPlayer();
        message.state = state;
        Log.d("CARDS", "send: " + state.cards.size());

        cState = CState.WAITING;
        currentCard = null;
        CarcassonneApp.getNetworkController().sendMessage(message);
    }

    @Override
    public void initMyTurn() {
        if (cState != CState.WAITING) return;

        cState = CState.DRAW_CARD;
        currentCard = null;
    }

    /**
     * Get possible locations for given card
     * @param c
     */
    public List<Pair<Integer, Integer>> getPossibleLocations(Card c) {
        List<Pair<Integer, Integer>> locations = new ArrayList<>();

        List<Card> cards = getGameState().cards;

        int originalX = c.getxCoordinate();
        int originalY = c.getyCoordinate();

        for (Card temp : cards) {
            int x = temp.getxCoordinate();
            int y = temp.getyCoordinate();

            c.setxCoordinate(x + 1);
            c.setyCoordinate(y);
            if (checkPosition(cards, c) && gameEngine.checkPlaceable(c)) {
                locations.add(new Pair<>(x + 1, y));
            }
            c.setxCoordinate(x - 1);
            c.setyCoordinate(y);
            if (checkPosition(cards, c) && gameEngine.checkPlaceable(c)) {
                locations.add(new Pair<>(x - 1, y));
            }
            c.setxCoordinate(x);
            c.setyCoordinate(y + 1);
            if (checkPosition(cards, c) && gameEngine.checkPlaceable(c)) {
                locations.add(new Pair<>(x, y + 1));
            }
            c.setxCoordinate(x);
            c.setyCoordinate(y - 1);
            if (checkPosition(cards, c) && gameEngine.checkPlaceable(c)) {
                locations.add(new Pair<>(x, y - 1));
            }
        }

        c.setxCoordinate(originalX);
        c.setyCoordinate(originalY);

        return locations;
    }


    @Override
    public CState getCState() {
        return cState;
    }

    @Override
    public boolean isDebug() {
        return isDebug;
    }

    @Override
    public void debug(boolean flag) {
        isDebug = flag;
    }

    @Override
    public Card getCurrentCard() {
        return currentCard;
    }

    @Override
    public void setCurrentCard(Card c) {
        currentCard = c;
        cState = CState.PLACE_CARD;
    }

    /**
     * Check the position of given card is already set
     * @param cards
     * @param c
     */
    private boolean checkPosition(List<Card> cards, Card c) {
        boolean free = true;
        for (Card temp :
                cards) {
            if (temp.getxCoordinate() == c.getxCoordinate() &&
                    temp.getyCoordinate() == c.getyCoordinate()) {
                free = false;
                break;
            }
        }

        return free;
    }

    @Override
    public void setPoints(List<Integer> points, int multiplier) {
        for(int i=0;i<points.size();i++){
            gameEngine.addScore(points.get(i) * multiplier, i);
        }
    }

    @Override
    public void checkPoints(Card card) {

        //todo: Kirchen

        List<Score> cardscore = gameEngine.getScoreChanges(card);
        int mult = 1;
        for (int i = 0; i < cardscore.size(); i++) {
            Score it = cardscore.get(i);
            if (it.isClosed()) {
                //Bastele Multiplier zusammen
                if (it.getBase() == CardSide.CASTLE) {
                    mult = 2;
                } else if (it.getBase() == CardSide.STREET) {
                    mult = 1;
                } else {
                    mult = 0;
                }

                //Finde most Peep Anzahl
                int mpoints = 0;
                for (int j = 0; j < 5; j++) {
                    if (it.getPpeepcount().get(i) > mpoints) {
                        mpoints = it.getPpeepcount().get(i);
                    }
                }
                //Erhöhe Punkte der Spieler, die die meisten Peeps haben
                ArrayList<Integer> mvps = new ArrayList<>(4);
                for (int j = 0; j < 5; j++) {
                    if (it.getPpeepcount().get(i) == mpoints) {
                        mvps.set(i, it.getCardlist().size());
                    } else {
                        mvps.set(i, 0);
                    }
                }
                setPoints(mvps, mult);

                //entferne gezählte Peeps
                for (int j = 0; j < it.getPeeplist().size(); j++) {
                    removePeep(it.getPeeplist().get(j));
                }
            } else {
                //offene Strecken bringen keine Punkte
            }
        }
    }

    private void removePeep(Peep peep) {
        GameState gs = getGameState();
        gs.getPeeps().remove(peep);
    }

    @Override
    public GameState getGameState() {
        return gameEngine.getState();
    }

    @Override
    public void setState(GameState s) {
        gameEngine.setState(s);
    }

    /**
     * Start the game.
     * Host owner must call this method
     */
    @Override
    public void startGame() {

        gameEngine = new GameEngine();
        gameEngine.init(Orientation.NORTH);
        cState = CState.WAITING;

        INetworkController controller = CarcassonneApp.getNetworkController();

        CarcassonneMessage message = new CarcassonneMessage(CarcassonneMessage.HOST_START_GAME);
        message.playerMappings = controller.createPlayerMappings();
        GameState state = getGameState();
        state.currentPlayer = 0;
        state.maxPlayerCount = controller.getDeviceCount();
        message.state = state;
        initPlayerMappings();


        cState = CState.DRAW_CARD;
        controller.sendToAllDevices(message);
    }

    @Override
    public void updateGameState() {
        INetworkController controller = CarcassonneApp.getNetworkController();
        CarcassonneMessage message = new CarcassonneMessage();
        message.type = CarcassonneMessage.GAME_STATE_UPDATE;
        message.state = getGameState();
        if (controller.isHost()) {
            controller.sendToAllDevices(message);
        } else if (controller.isClient()) {
            controller.sendToHost(message);
        }
    }

    @Override
    public boolean isMyTurn() {
        return getGameState().myTurn(CarcassonneApp.getNetworkController().getDevicePlayerNumber());
    }



    @Override
    public boolean hasPlacedCard() {
        return cardPlaced;
    }

    @Override
    public void initPlayerMappings() {
        playerHashMap = new HashMap<>();
        for (PlayerInfo playerInfo : CarcassonneApp.getNetworkController().getPlayerMappings().values()
                ) {
            playerHashMap.put(playerInfo.playerNumber, Player.getRaceFromPlayer(playerInfo.raceType, playerInfo.playerNumber));
        }

    }
}
