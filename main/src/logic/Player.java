package logic;


import graphics.Wall;
import input.InputManager;


public class Player extends Entity {

    public int id;
    private int speed = 10;

    public Player(Vector center, float radius, int id) {
        super();
        this.center = center;
        this.radius = radius;
        this.id = id;

        InputManager.initialize(id);
    }

    @Override
    public void update(InputManager inputManager, GameState gameState) {
        super.update(inputManager, gameState);

        if (InputManager.getKey("up", id)) {
            this.cSpeed.y -= speed;
        }

        if (InputManager.getKey("down", id)) {
            this.cSpeed.y += speed;
        }

        if (InputManager.getKey("left", id)) {
            this.cSpeed.x -= speed;
        }

        if (InputManager.getKey("right", id)) {
            this.cSpeed.x += speed;
        }

        //in each frame, check for collision for each player against all other players
        for (Player p : gameState.getPlayers()) {
            for (Player p1 : gameState.getPlayers()) {
                if (p != p1) { //not ownself
                    p.collisionControl(p1);
                }
            }
        }

        for (Wall w: gameState.getWalls()) {
            Vector delta = w.center.sub(this.center);
            if (Math.abs(delta.x) * 2< this.radius + w.radius
             && Math.abs(delta.y) * 2< this.radius + w.radius) {
                double angle = Math.atan2(-delta.y, -delta.x);
                this.cSpeed = this.cSpeed.add(new Vector(Math.cos(angle), Math.sin(angle)).mulConst(10));
                // Vector pos = new Vector((float) Math.cos(angle), (float) Math.sin(angle)).mulConst(this.radius + w.radius).mulConst(0.5f).add(w.center);
                // this.center = pos;
            }
        }


    }



    /**
     *Method to check if the two circles collided, by checking if the distance between the centers of the two circles is
     *more or less than the sum of the two radii
     **/
    public boolean overlaps(Player other) {
        if ( Math.abs(center.x - other.center.x) > radius + other.radius ) {
            return false;
        } else if ( Math.abs(center.y - other.center.y) > radius + other.radius ) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Calculating distance between two centres of two players
     */
    public double distBtwnCenters (Player other) {

        double xdiff = this.center.x - other.center.x;
        double ydiff = this.center.y - other.center.y;

        return Math.sqrt( ( xdiff * xdiff ) + ( ydiff * ydiff ) );
    }

    /**
     * Get vector connecting two centres
     */
    public Vector vecBtwnCenters (Player other) {
        Vector center1 = this.center;
        Vector center2 = other.center;

        return center2.sub(center1);
    }

    /**
     * If two entities collide
     */
    public void collisionControl(Player other) {

       if (this.distBtwnCenters(other) < this.radius) { //the dist between centers<radius of circle
           Vector recoveryVector = this.vecBtwnCenters(other); //one player move in direction if this vector, other opp
           this.center = this.center.sub(recoveryVector.mulConst(0.3f));
           other.center = other.center.add(recoveryVector.mulConst(0.3f));

       }
    }
}