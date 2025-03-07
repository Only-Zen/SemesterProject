package main;

public class Coordinate {
    private int x;
    private int y;
    GamePanel gp;
    
    public Coordinate(int x, int y, GamePanel gp){
        this.x = x;
        this.y = y;
        this.gp = gp;
    }

    public void setX(int x){
        this.x = x;
    }

    public void setY(int y){
        this.y = y;
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    public Coordinate getCoord(){
        return new Coordinate(this.x, this.y, gp);
    }
    
    public Coordinate getGrid(){
        Coordinate gridPosition = new Coordinate(0,0, gp);
        gridPosition.setX(Math.floorDiv(x, gp.TILESIZE) * gp.TILESIZE);
        gridPosition.setY(Math.floorDiv(y, gp.TILESIZE) * gp.TILESIZE);
        
        return gridPosition;
    }

    // Override toString for debugging
    @Override
    public String toString() {
        return "Coord(x=" + x + ", y=" + y + ")";
    }
}
