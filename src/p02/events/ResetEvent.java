package p02.events;

import java.util.EventObject;

public class ResetEvent extends EventObject {
    public ResetEvent(Object source) {
        super(source);
    }
}