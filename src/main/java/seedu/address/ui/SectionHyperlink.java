package seedu.address.ui;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Hyperlink;

/**
 * Hyperlink that links to a help page when clicked.
 */
public class SectionHyperlink extends Hyperlink {
    /**
     * Creates a {@code SectionHyperlink} with no linked target.
     */
    public SectionHyperlink() {
        super();
        setPadding(new Insets(-1));
        setOnAction(this::onAction);
    }

    private void onAction(ActionEvent event) {
        fireEvent(new NavigationEvent(getText()));
    }
}
