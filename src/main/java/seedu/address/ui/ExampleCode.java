package seedu.address.ui;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * A UI component that represent a command snippet that can be copied.
 */
public class ExampleCode extends HBox {

    private final StringProperty code = new SimpleStringProperty(null, "code", "");

    /**
     * Creates an empty {@code ExampleCode}.
     */
    public ExampleCode() {
        super();
        setSpacing(4);
        setAlignment(Pos.BASELINE_LEFT);
        getStylesheets().add(getClass().getResource("/view/HelpWindow.css").toExternalForm());
        getStyleClass().add("code");

        Button copyButton = new Button();
        copyButton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        copyButton.setPadding(new Insets(0));
        copyButton.setBackground(Background.EMPTY);
        copyButton.setBorder(Border.EMPTY);
        copyButton.setOnAction(_ -> copyExample());

        ImageView copyImage = new ImageView(getClass().getResource("/images/copy.png").toExternalForm());
        copyImage.setFitWidth(10);
        copyImage.setFitHeight(10);
        copyButton.setGraphic(copyImage);

        Label codeLabel = new Label();
        codeLabel.setWrapText(true);
        codeLabel.setMinHeight(USE_PREF_SIZE);
        codeLabel.textProperty().bind(code);

        getChildren().addAll(copyButton, codeLabel);
    }

    public String getCode() {
        return code.get();
    }

    public void setCode(String code) {
        this.code.set(code);
    }

    public StringProperty codeProperty() {
        return code;
    }

    private void copyExample() {
        ClipboardContent content = new ClipboardContent();
        content.putString(code.get());
        Clipboard.getSystemClipboard().setContent(content);
    }
}
