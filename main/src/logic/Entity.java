package logic;

import input.InputManager;

public class Entity {

    public Vector cSpeed;

    public Vector position;
    public float radius;

    public Entity() {
        cSpeed = Vector.zero;
        position = Vector.zero;
        radius = 0;
    }

    public void update(InputManager inputManager, GameState gameState) {
        // update positions
        updatePhysics();
    }

    /**
     * Updates the position
     */
    public void updatePhysics() {
        cSpeed = cSpeed.mulConst(0.95f);
        Vector dist = cSpeed.mulConst(Time.deltaTime);
        position = position.add(dist); //updating circle's position += speed*deltaTime, updating circle's new position
    }

}
