package distudios.at.carcassonne;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;

import distudios.at.carcassonne.engine.logic.Card;
import distudios.at.carcassonne.engine.logic.CardDataBase;
import distudios.at.carcassonne.engine.logic.CardSide;
import distudios.at.carcassonne.engine.logic.GameEngine;
import distudios.at.carcassonne.engine.logic.GameState;
import distudios.at.carcassonne.engine.logic.Orientation;
import distudios.at.carcassonne.engine.logic.Peep;
import distudios.at.carcassonne.engine.logic.PeepPosition;

import static distudios.at.carcassonne.engine.logic.CardSide.CASTLE;
import static distudios.at.carcassonne.engine.logic.CardSide.GRASS;
import static distudios.at.carcassonne.engine.logic.CardSide.STREET;
import static distudios.at.carcassonne.engine.logic.Orientation.EAST;
import static distudios.at.carcassonne.engine.logic.Orientation.NORTH;
import static distudios.at.carcassonne.engine.logic.Orientation.SOUTH;
import static distudios.at.carcassonne.engine.logic.Orientation.WEST;

import static distudios.at.carcassonne.engine.logic.PeepPosition.Bottom;
import static distudios.at.carcassonne.engine.logic.PeepPosition.Center;
import static distudios.at.carcassonne.engine.logic.PeepPosition.Left;
import static distudios.at.carcassonne.engine.logic.PeepPosition.Right;
import static distudios.at.carcassonne.engine.logic.PeepPosition.Top;


public class TestGameEngine {
    GameEngine ge;
    GameState gs;
    CardDataBase cdb = CardDataBase.getInstance();

    @Before
    public void setUp() {
        ge = new GameEngine();
        ge.init(Orientation.NORTH);
        gs = ge.getGamestate();

        cdb.cardDB.get(19).setDown(CASTLE);
        cdb.cardDB.get(19).setTop(CardSide.GRASS);
        cdb.cardDB.get(19).setLeft(CardSide.GRASS);
        cdb.cardDB.get(19).setRight(CASTLE);   //Anpassung Neue Karte Check

        cdb.cardDB.get(20).setTop(CardSide.GRASS);
        cdb.cardDB.get(20).setDown(CardSide.GRASS);
        cdb.cardDB.get(20).setLeft(CardSide.GRASS);
        cdb.cardDB.get(20).setRight(CardSide.STREET);

        cdb.cardDB.get(21).setDown(CardSide.GRASS);     //Anpassung Neue Karte Check
        cdb.cardDB.get(21).setLeft(CASTLE);
        cdb.cardDB.get(21).setTop(CardSide.GRASS);
        cdb.cardDB.get(21).setRight(CASTLE);

        cdb.cardDB.get(22).setRight(CardSide.GRASS);
        cdb.cardDB.get(22).setDown(CardSide.STREET);
        cdb.cardDB.get(22).setTop(CardSide.GRASS);
        cdb.cardDB.get(22).setLeft(CardSide.STREET);

        cdb.cardDB.get(23).setTop(CardSide.STREET);
        cdb.cardDB.get(23).setRight(CardSide.STREET);
        cdb.cardDB.get(23).setLeft(CardSide.GRASS);
        cdb.cardDB.get(23).setDown(CardSide.GRASS);

        cdb.cardDB.get(25).setRight(CASTLE);
        cdb.cardDB.get(25).setLeft(CardSide.STREET);
        cdb.cardDB.get(25).setTop(CardSide.GRASS);

        cdb.cardDB.get(11).setRight(CardSide.STREET);
        cdb.cardDB.get(11).setTop(CardSide.STREET);
        cdb.cardDB.get(11).setDown(CardSide.GRASS);
        cdb.cardDB.get(11).setLeft(CardSide.GRASS);

        cdb.cardDB.get(26).setRight(CardSide.GRASS);
        cdb.cardDB.get(26).setTop(CardSide.STREET);
        cdb.cardDB.get(26).setDown(CardSide.STREET);
        cdb.cardDB.get(26).setLeft(CASTLE);

        cdb.cardDB.get(2).setRight(CardSide.CASTLE);
        cdb.cardDB.get(2).setTop(CASTLE);
        cdb.cardDB.get(2).setDown(CASTLE);
        cdb.cardDB.get(2).setLeft(GRASS);
        cdb.cardDB.get(2).setCathedral(false);

        cdb.cardDB.get(3).setRight(CASTLE);
        cdb.cardDB.get(3).setTop(CardSide.GRASS);
        cdb.cardDB.get(3).setDown(CASTLE);
        cdb.cardDB.get(3).setLeft(CASTLE);
    }

    @Test
    public void shuffleWorks() {
        ArrayList<Integer> al = gs.getStack();
        for (int i = 0; i < al.size(); i++) {
            System.out.println("Listelement " + i + " : " + al.get(i));
        }
    }

    @Test
    public void checkPlaceableMethod() {
        System.out.println("\nPlatziere 3 Karten und überprüfe auf Platzierbarkeit");
        Card card = new Card(20, 0, 1, Orientation.NORTH);
        Assert.assertTrue(ge.checkPlaceable(card));     //Test Placeable
        ge.placeCard(card);
        Assert.assertFalse(ge.checkPlaceable(card));    //Test reflexive Placeable false
        card = new Card(21, 1, 0, Orientation.NORTH);
        Assert.assertTrue(ge.checkPlaceable(card));     //Test placeable
        ge.placeCard(card);
        card = new Card(22, 1, 1, Orientation.NORTH);
        Assert.assertTrue(ge.checkPlaceable(card));     //Test placeable with 2 Connection
        ge.placeCard(card);
        card=new Card(23,-1,0, NORTH);
        Assert.assertTrue(ge.checkPlaceable(card));
        ge.placeCard(card);
        card=new Card(24,-1,-1, NORTH);
        Assert.assertTrue(ge.checkPlaceable(card));
        ge.placeCard(card);
        card=new Card(25,0,-1, NORTH);
        Assert.assertTrue(ge.checkPlaceable(card));
        ge.placeCard(card);
        card=new Card(26,1,-1, NORTH);
        Assert.assertTrue(ge.checkPlaceable(card));
        ge.placeCard(card);
        //card=new Card(12,-2,0, NORTH);
        //Assert.assertTrue(ge.checkPlaceable(card));
        //ge.placeCard(card);
        printCards(gs);
    }



    @Test
    public void testCheckBorder() {
        //Settings...
        Card card;
        ge.placeCard(card = new Card(20, 0, 1, NORTH));
        ge.placeCard(card = new Card(21, 1, 0, NORTH));
        //ge.placeCard(card = new Card(22, 1, 1, NORTH));
        Card testCard = new Card(22, 1, 1, NORTH);
        ArrayList<Card> field = gs.getCards();

        Boolean checkBorder = ge.checkBorder(testCard, field, NORTH);
        Assert.assertTrue(checkBorder == true);

    }

    @Test
    public void switchOrientation() {
        Orientation result = Card.getAbsoluteOrientation(Orientation.EAST, Orientation.SOUTH);
        Orientation compare = Card.getAbsoluteOrientation(Orientation.WEST, Orientation.NORTH);
        Assert.assertTrue(result == compare);
    }

    private void printCards(GameState pgs) {
        ArrayList<Card> cardList = pgs.getCards();

        for (int i = 0; i < cardList.size(); i++) {
            System.out.println("Card " + i + " : " + cardList.get(i).toString());
        }
    }
}
