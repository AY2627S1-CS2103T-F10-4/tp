package seedu.address.ui;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;

/**
 * Hyperlink that links to a target node and scrolls to the target when clicked.
 */
public class SectionHyperlink extends Hyperlink {
    private ObjectProperty<Node> target = new SimpleObjectProperty<>(this, "target");

    /**
     * Creates a {@code SectionHyperlink} with no linked target.
     */
    public SectionHyperlink() {
        setOnAction(this::onAction);
    }

    /**
     * Creates a {@code SectionHyperlink} that links to the given target.
     *
     * @param target the target to scroll to when the link is clicked.
     */
    public SectionHyperlink(Node target) {
        this();
        setTarget(target);
    }

    public Node getTarget() {
        return target.get();
    }
    public void setTarget(Node target) {
        this.target.set(target);
    }
    public ObjectProperty<Node> targetProperty() {
        return target;
    }

    private void onAction(ActionEvent event) {
        Event.fireEvent(target.get(), new ScrollEvent());
    }
}
