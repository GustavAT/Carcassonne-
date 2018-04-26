package distudios.at.carcassonne.engine.logic;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;



public enum CardSide {
    RIVER, CASTLE, GRASS, STREET;

    private static final List<CardSide> VALUES =
            Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    public static CardSide randomCarSide() {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }
}
