package events;

import java.awt.event.KeyEvent;

public class KeyReleasedEvent extends BaseEvent {
    private KeyEvent keyEvent;

    public KeyReleasedEvent(KeyEvent e) {
        this.keyEvent = e;
    }

    public KeyEvent getKeyEvent() {
        return keyEvent;
    }

    @Override
    public String toString() {
        return String.format("%d:false", this.keyEvent.getKeyCode());
    }
}
