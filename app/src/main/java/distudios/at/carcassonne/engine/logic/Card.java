package distudios.at.carcassonne.engine.logic;

public class Card {

    private int id;
    private Orientation orientation;
    private int xCoordinate;
    private int yCoordinate;

    public Card(int id,  int xCoordinate, int yCoordinate, Orientation orientation) {
        this.id = id;
        this.orientation = orientation;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    public int getxCoordinate() {
        return xCoordinate;
    }

    public void setxCoordinate(int xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public int getyCoordinate() {
        return yCoordinate;
    }

    public void setyCoordinate(int yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    @Override
    public String toString(){
        return "Id: "+ id + " Coordinates: " + xCoordinate + " | " + yCoordinate;
    }
     public static Orientation getAbsoluteOrientation(Orientation card, Orientation offset){
        int rotation=0;
        if(offset==Orientation.NORTH){
            rotation=0;
        }
        else if(offset==Orientation.EAST){
            rotation=1;
        }
        else if(offset==Orientation.SOUTH){
            rotation=2;
        }
        else{
            rotation=3;
        }


        for(int i=0;i<rotation;i++){
            if(card==Orientation.NORTH){
                card=Orientation.WEST;
            }
            else if(card==Orientation.EAST){
                card=Orientation.NORTH;
            }
            else if(card==Orientation.SOUTH){
                card=Orientation.EAST;
            }
            else{
                card=Orientation.SOUTH;
            }
        }

        return card;
     }
}


