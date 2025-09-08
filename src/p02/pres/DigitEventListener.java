package p02.pres;

import p02.events.PlusOneEvent;
import p02.events.ResetEvent;
import p02.events.StartEvent;

import java.util.EventListener;

// EventListener to znacznik, nie zawiera metod
public interface DigitEventListener extends EventListener {
    void plusOneEvent(PlusOneEvent e);
}
