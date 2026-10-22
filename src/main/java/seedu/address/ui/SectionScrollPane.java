package seedu.address.ui;

import javafx.scene.Node;
import javafx.scene.control.ScrollPane;

/**
 * Scroll pane with a scroll-to-element feature.
 */
public class SectionScrollPane extends ScrollPane {
    /**
     * Creates a {@code SectionScrollPane}.
     */
    public SectionScrollPane() {
        addEventHandler(ScrollEvent.ANY, event -> {
            if (event.getTarget() != null) {
                scrollToSection(event.getTarget());
            }
            event.consume();
        });
    }

    /**
     * Scrolls to the given child node.
     *
     * @param target the node to scroll to.
     */
    public void scrollToSection(Node target) {
        Node content = getContent();

        double targetPos = content.sceneToLocal(target.localToScene(target.getBoundsInLocal())).getMinY();
        double contentHeight = content.getBoundsInLocal().getHeight();
        double viewportHeight = getViewportBounds().getHeight();

        if (contentHeight > viewportHeight) {
            double relativePos = targetPos / (contentHeight - viewportHeight);
            setVvalue(Math.clamp(relativePos, 0, 1));
        }
    }

}
