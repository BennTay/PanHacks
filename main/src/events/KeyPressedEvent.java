package events;

import java.awt.event.KeyEvent;

public class KeyPressedEvent extends BaseEvent{
    private KeyEvent keyEvent;

    public KeyPressedEvent(KeyEvent e) {
        this.keyEvent = e;
    }

    public KeyEvent getKeyEvent() {
        return keyEvent;
    }

    @Override
    public String toString() {
        return String.format("%d:true", this.keyEvent.getKeyCode());
    }
}
