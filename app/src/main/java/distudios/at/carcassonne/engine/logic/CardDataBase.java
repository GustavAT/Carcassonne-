package distudios.at.carcassonne.engine.logic;

import java.util.ArrayList;



public class CardDataBase {

    //Singleton Class CardDataBase that currently generates a full stack of cards to choose from



    public ArrayList<ExtendedCard> cardDB=new ArrayList<>();
    private final int STACK_SIZE=50;

    private static CardDataBase INSTANCE = new CardDataBase();


    private CardDataBase(){
        cardDB.add(new ExtendedCard(1, CardSide.CASTLE, CardSide.STREET, CardSide.GRASS, CardSide.RIVER));
        for(int i=0;i<STACK_SIZE;i++){
            cardDB.add(generateRandomCard(i+2));
            // Card Stack IDs go from 2 to 51. ID 1 is always start card
        }
    }

    public static CardDataBase getInstance(){
        return (INSTANCE);
    }


    public static CardDataBase cardDataBase = CardDataBase.getInstance();

    private ExtendedCard generateRandomCard(int id){
        return new ExtendedCard(id, CardSide.randomCarSide(), CardSide.randomCarSide(), CardSide.randomCarSide(), CardSide.randomCarSide());
    }

    //method orientation + id returns CardSide

    public CardSide getOrientation (int id, Orientation orientation){
        switch (orientation){
            case NORTH:
                return cardDB.get(id-1).getTop();

            case WEST:
                return cardDB.get(id-1).getLeft();

            case EAST:
                return cardDB.get(id-1).getRight();

            case SOUTH:
                return cardDB.get(id-1).getDown();

            default:
                return null;

        }
    }
}