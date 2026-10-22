package seedu.address.ui;

import java.util.stream.Collectors;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import seedu.address.commons.util.AppUtil;

/**
 * Button that copies the text content of a text node to the user's clipboard.
 * The currently supported node types are {@link Text}, {@link Label}, and {@link TextFlow}
 */
public class CopyButton extends Button {
    private final ObjectProperty<Node> target = new SimpleObjectProperty<>(this, "target");

    /**
     * Creates a {@code CopyButton} without any copy target.
     */
    public CopyButton() {
        super();
        getStyleClass().add("copy-button");
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

        ImageView graphic = new ImageView();
        graphic.setImage(AppUtil.getImage("/images/copy.png"));
        graphic.setFitWidth(20);
        graphic.setFitHeight(20);
        setGraphic(graphic);

        setOnAction(this::onAction);
    }

    /**
     * Creates a {@code CopyButton} targeting the given node.
     */
    public CopyButton(Node target) {
        this();
        setTarget(target);
    }

    public void setTarget(Node target) {
        if (!(target instanceof Text
                || target instanceof Label
                || target instanceof TextFlow)) {
            throw new IllegalArgumentException("The node type "
                    + target.getClass().getSimpleName() + " is not supported for copying.");
        }
        this.target.set(target);
    }
    public Node getTarget() {
        return target.get();
    }
    public ObjectProperty<Node> targetProperty() {
        return target;
    }

    private void onAction(ActionEvent event) {
        ClipboardContent content = new ClipboardContent();
        content.putString(nodeToString(getTarget()));
        Clipboard.getSystemClipboard().setContent(content);
    }

    private String nodeToString(Node node) {
        return switch (node) {
            case Text text -> text.getText();
            case Label label -> label.getText();
            case TextFlow textFlow -> textFlow.getChildren().stream()
                    .map(this::nodeToString)
                    .collect(Collectors.joining());
            default -> "";
        };
    }
}
