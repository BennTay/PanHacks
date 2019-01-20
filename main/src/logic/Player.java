package logic;


import input.InputManager;

public class Player extends Entity {

    public int id;

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
            this.cSpeed.y -= 15;
        }

        if (InputManager.getKey("down", id)) {
            this.cSpeed.y += 15;
        }

        if (InputManager.getKey("left", id)) {
            this.cSpeed.x -= 15;
        }

        if (InputManager.getKey("right", id)) {
            this.cSpeed.x += 15;
        }
    }

    /**
     *Method to check if the two circles collided, by checking if the distance between the centers of the two circles is
     *more or less than the sum of the two radii
     */
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
     * If two entities collide
     */
    public void collisionControl(Player player) {

       cSpeed.x = 0;
       cSpeed.y = 0;

    }
}