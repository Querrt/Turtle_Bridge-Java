package p02.events;

import java.util.EventObject;

public class PlusOneEvent extends EventObject {
    public PlusOneEvent(Object source) {
        super(source);
    }
}