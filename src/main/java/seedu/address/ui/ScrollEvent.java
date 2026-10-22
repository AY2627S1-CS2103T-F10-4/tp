package seedu.address.ui;

import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.Node;

/**
 * A custom {@code Event} emitted by children nodes to request scrolling.
 */
public class ScrollEvent extends Event {
    /**
     * The only valid EventType for the `ScrollEvent`.
     */
    public static final EventType<ScrollEvent> ANY = new EventType<>(Event.ANY, "SCROLL");

    /**
     * Creates a new {@code ScrollEvent}.
     */
    public ScrollEvent() {
        super(ANY);
    }

    /**
     * Creates a new {@code ScrollEvent}.
     *
     * @param source the node from which the request is sent
     * @param target the node to scroll to.
     */
    public ScrollEvent(Node source, Node target) {
        super(source, target, ANY);
    }

    @Override
    public Node getSource() {
        return source == NULL_SOURCE_TARGET ? null : (Node) source;
    }

    /**
     * Returns the target {@code Node} to scroll to.
     */
    @Override
    public Node getTarget() {
        return target == NULL_SOURCE_TARGET ? null : (Node) target;
    }
}
