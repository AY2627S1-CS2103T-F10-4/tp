package seedu.address.ui;

import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.Node;

/**
 * A custom {@code Event} emitted by children nodes to request navigation.
 */
public class NavigationEvent extends Event {
    /**
     * The only valid EventType for the `NavigationEvent`.
     */
    public static final EventType<NavigationEvent> ANY = new EventType<>(Event.ANY, "SCROLL");

    private String pageName;

    /**
     * Creates a new {@code NavigationEvent}.
     */
    public NavigationEvent() {
        super(ANY);
    }

    /**
     * Creates a new {@code NavigationEvent}.
     *
     * @param pageName the name of the page to navigate to.
     */
    public NavigationEvent(String pageName) {
        super(ANY);
        this.pageName = pageName;
    }

    /**
     * Creates a new {@code NavigationEvent}.
     *
     * @param source the event source which sent the event.
     * @param target the event target to associate with the event.
     * @param pageName the name of the page to navigate to.
     */
    public NavigationEvent(Object source, Node target, String pageName) {
        super(source, target, ANY);
        this.pageName = pageName;
    }

    public String getPageName() {
        return pageName;
    }
}
