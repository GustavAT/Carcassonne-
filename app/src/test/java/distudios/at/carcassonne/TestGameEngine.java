package distudios.at.carcassonne;

import android.util.Log;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import distudios.at.carcassonne.engine.logic.Card;
import distudios.at.carcassonne.engine.logic.CardDataBase;
import distudios.at.carcassonne.engine.logic.CardSide;
import distudios.at.carcassonne.engine.logic.GameEngine;
import distudios.at.carcassonne.engine.logic.GameState;
import distudios.at.carcassonne.engine.logic.Orientation;


public class TestGameEngine {
    GameEngine ge;
    GameState gs;
    CardDataBase cdb=CardDataBase.getInstance();

    @Before
    public void setUp(){
        ge=new GameEngine();
        ge.init(Orientation.NORTH);
        gs=ge.getGamestate();
        cdb.cardDB.get(19).setDown(CardSide.CASTLE);    //Anpassung Startkarte
        cdb.cardDB.get(20).setLeft(CardSide.GRASS);

        cdb.cardDB.get(19).setRight(CardSide.CASTLE);   //Anpassung Neue Karte Check
        cdb.cardDB.get(20).setTop(CardSide.RIVER);

        cdb.cardDB.get(21).setDown(CardSide.RIVER);     //Anpassung Neue Karte Check
        cdb.cardDB.get(21).setLeft(CardSide.CASTLE);
    }

    @Test
    public void shuffleWorks(){
        ArrayList<Integer> al=gs.getStack();
        for(int i=0;i<al.size();i++){
            System.out.println("Listelement "+i+" : "+al.get(i));
        }
    }

    @Test
    public void checkPlaceableMethod(){
        System.out.println("\nPlatziere 3 Karten und überprüfe auf Platzierbarkeit");
        Card card=new Card(20,0,1,Orientation.NORTH);
        Assert.assertTrue(ge.checkPlaceable(card));     //Test Placeable
        ge.placeCard(card);
        Assert.assertFalse(ge.checkPlaceable(card));    //Test reflexive Placeable false
        card=new Card(21,1,0,Orientation.NORTH);
        Assert.assertTrue(ge.checkPlaceable(card));     //Test placeable
        ge.placeCard(card);
        card=new Card(22,1,1,Orientation.NORTH);
        Assert.assertTrue(ge.checkPlaceable(card));     //Test placeable with 2 Connection
        ge.placeCard(card);
        printCards(gs);

    }

    @Test
    public void switchOrientation(){
        Orientation result=Card.getAbsoluteOrientation(Orientation.EAST, Orientation.SOUTH);
        Orientation compare=Card.getAbsoluteOrientation(Orientation.WEST, Orientation.NORTH);
        Assert.assertTrue(result==compare);
    }




    private void printCards(GameState pgs){
        ArrayList<Card> cardList = pgs.getCards();

        for(int i=0;i<cardList.size();i++){
            System.out.println("Card "+i+" : "+cardList.get(i).toString());
        }
    }
}
