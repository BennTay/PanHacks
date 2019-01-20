package logic;

import input.InputManager;

public class Entity {

    public Vector cSpeed;

    public Vector center;
    public float radius;

    public Entity() {
        cSpeed = Vector.zero;
        center = Vector.zero;
        radius = 0;
    }

    public void update(InputManager inputManager, GameState gameState) {
        // update positions
        updatePhysics();
    }

    /**
     * Updates the center
     */
    public void updatePhysics() {
        cSpeed = cSpeed.mulConst(0.95f);
        Vector dist = cSpeed.mulConst(Time.deltaTime);
        center = center.add(dist); //updating circle's center += speed*deltaTime, updating circle's new center
    }

}
