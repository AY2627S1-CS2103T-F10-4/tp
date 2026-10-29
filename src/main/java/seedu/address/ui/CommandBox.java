package seedu.address.ui;

import java.util.function.UnaryOperator;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import seedu.address.commons.core.InputLimits;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * The UI component that is responsible for receiving user command inputs.
 */
public class CommandBox extends UiPart<Region> {

    public static final String ERROR_STYLE_CLASS = "error";
    public static final String WARNING_STYLE_CLASS = "warning";
    private static final String COMMAND_HINT = "↑/↓ command history • Type to see suggestions";
    private static final String OVERFLOW_WARNING = "Input truncated at " + InputLimits.MAX_COMMAND_LENGTH
            + " characters";
    private static final String FXML = "CommandBox.fxml";

    private final CommandExecutor commandExecutor;
    private final CommandHistory commandHistory = new CommandHistory();
    private final CommandSuggestions commandSuggestions;
    private boolean areSuggestionsSuppressed;
    private boolean inputOverflowAttempted;

    @FXML
    private TextField commandTextField;

    @FXML
    private ListView<String> suggestionList;

    @FXML
    private StackPane suggestionOverlay;

    @FXML
    private Label commandHint;

    /**
     * Creates a {@code CommandBox} with the given {@code CommandExecutor}.
     */
    public CommandBox(CommandExecutor commandExecutor) {
        this(commandExecutor, new CommandSuggestions());
    }

    /**
     * Creates a command box with a supplied suggestion source.
     */
    CommandBox(CommandExecutor commandExecutor, CommandSuggestions commandSuggestions) {
        super(FXML);
        this.commandExecutor = commandExecutor;
        this.commandSuggestions = commandSuggestions;
        UnaryOperator<TextFormatter.Change> inputLengthFilter = change ->
                limitInputLength(change);
        commandTextField.setTextFormatter(new TextFormatter<String>(inputLengthFilter));
        suggestionList.visibleProperty().addListener((observable, oldValue, newValue) -> updateCommandHint());
        updateCommandHint();
        suggestionOverlay.visibleProperty().bind(suggestionList.visibleProperty());
        suggestionOverlay.layoutYProperty().bind(commandTextField.layoutYProperty()
                .add(commandTextField.heightProperty()));
        getRoot().widthProperty().addListener((observable, oldWidth, newWidth) ->
                suggestionOverlay.resize(newWidth.doubleValue(), suggestionOverlay.getPrefHeight()));
        commandTextField.textProperty().addListener((unused1, unused2, unused3) -> {
            setStyleToDefault();
            if (!areSuggestionsSuppressed) {
                updateSuggestions();
            }
        });
        commandTextField.focusedProperty().addListener((observable, oldValue, isFocused) -> {
            if (!isFocused) {
                hideSuggestions();
            }
        });
        commandTextField.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKeyPressed);
    }

    /**
     * Refreshes matches after an input edit and selects the first match.
     * Shows up to three rows over the output area without occupying space in the window layout.
     */
    private void updateSuggestions() {
        suggestionList.getItems().setAll(commandSuggestions.suggest(commandTextField.getText()));
        boolean isVisible = !suggestionList.getItems().isEmpty();
        suggestionList.setVisible(isVisible);
        suggestionList.setManaged(isVisible);
        suggestionList.getSelectionModel().selectFirst();
    }

    /** Truncates edits that would exceed the command limit and records the attempted overflow. */
    private TextFormatter.Change limitInputLength(TextFormatter.Change change) {
        if (change.getControlNewText().length() <= InputLimits.MAX_COMMAND_LENGTH) {
            return change;
        }
        int excess = change.getControlNewText().length() - InputLimits.MAX_COMMAND_LENGTH;
        change.setText(change.getText().substring(0, Math.max(0, change.getText().length() - excess)));
        inputOverflowAttempted = true;
        updateCommandHint();
        return change;
    }

    private void updateCommandHint() {
        if (inputOverflowAttempted) {
            commandHint.setText(OVERFLOW_WARNING);
            commandHint.getStyleClass().add(WARNING_STYLE_CLASS);
        } else if (suggestionList.isVisible()) {
            commandHint.setText("Suggestions: ↑/↓ select • Tab complete • Esc dismiss");
        } else {
            commandHint.setText(COMMAND_HINT);
        }
    }

    /**
     * Dismisses the overlay without changing the window layout.
     */
    private void hideSuggestions() {
        suggestionList.setVisible(false);
        suggestionList.setManaged(false);
    }

    /**
     * Replaces the incomplete command with the selected suggestion, preserving leading whitespace.
     * Requires a visible list with a selected match; does not execute or record the completion in history.
     */
    private void completeSuggestion() {
        String selected = suggestionList.getSelectionModel().getSelectedItem();
        String input = commandTextField.getText();
        String leadingWhitespace = input.substring(0, input.length() - input.stripLeading().length());
        replaceInputWithoutSuggestions(leadingWhitespace + selected);
    }

    /**
     * Replaces input during completion or history navigation without reopening suggestions.
     * Places the caret at the end of the replacement.
     */
    private void replaceInputWithoutSuggestions(String text) {
        areSuggestionsSuppressed = true;
        try {
            commandTextField.setText(text);
        } finally {
            areSuggestionsSuppressed = false;
        }
        hideSuggestions();
        commandTextField.positionCaret(commandTextField.getLength());
    }

    /**
     * Gives visible suggestions priority over history navigation for unmodified keys.
     * Consumes handled keys to prevent the text field from also acting on them.
     */
    private void handleKeyPressed(KeyEvent event) {
        if (event.isAltDown() || event.isControlDown() || event.isMetaDown() || event.isShiftDown()) {
            return;
        }
        if (suggestionList.isVisible()) {
            switch (event.getCode()) {
                case UP:
                case DOWN:
                    moveSuggestionSelection(event.getCode() == KeyCode.UP ? -1 : 1);
                    break;
                case TAB:
                case ENTER:
                    completeSuggestion();
                    break;
                case ESCAPE:
                    hideSuggestions();
                    break;
                default:
                    return;
            }
            event.consume();
            return;
        }
        handleHistoryNavigation(event);
    }

    /**
     * Moves the selection by {@code offset}, stopping at the list boundaries and keeping the match visible.
     */
    private void moveSuggestionSelection(int offset) {
        int requestedIndex = suggestionList.getSelectionModel().getSelectedIndex() + offset;
        int selectedIndex = Math.max(0, Math.min(requestedIndex, suggestionList.getItems().size() - 1));
        suggestionList.getSelectionModel().select(selectedIndex);
        suggestionList.scrollTo(selectedIndex);
    }

    /**
     * Recalls commands using unmodified arrow keys while the command field has focus.
     */
    private void handleHistoryNavigation(KeyEvent event) {
        String currentInput = commandTextField.getText();
        String recalledInput;
        if (event.getCode() == KeyCode.UP) {
            recalledInput = commandHistory.previous(currentInput);
        } else if (event.getCode() == KeyCode.DOWN) {
            recalledInput = commandHistory.next(currentInput);
        } else {
            return;
        }
        replaceInputWithoutSuggestions(recalledInput);
        event.consume();
    }

    /**
     * Completes a visible suggestion, or submits the input and records it in session history.
     * Failed submissions retain their input and receive error styling.
     */
    @FXML
    private void handleCommandEntered() {
        if (suggestionList.isVisible()) {
            completeSuggestion();
            return;
        }
        String commandText = commandTextField.getText();
        if (commandText.isEmpty()) {
            return;
        }

        commandHistory.add(commandText);
        try {
            commandExecutor.execute(commandText);
            commandTextField.setText("");
            inputOverflowAttempted = false;
            commandHint.getStyleClass().remove(WARNING_STYLE_CLASS);
            updateCommandHint();
        } catch (CommandException | ParseException e) {
            setStyleToIndicateCommandFailure();
        }
    }

    /**
     * Sets the command box style to use the default style.
     */
    private void setStyleToDefault() {
        commandTextField.getStyleClass().remove(ERROR_STYLE_CLASS);
    }

    /**
     * Sets the command box style to indicate a failed command.
     */
    private void setStyleToIndicateCommandFailure() {
        ObservableList<String> styleClass = commandTextField.getStyleClass();

        if (styleClass.contains(ERROR_STYLE_CLASS)) {
            return;
        }

        styleClass.add(ERROR_STYLE_CLASS);
    }

    /**
     * Represents a function that can execute commands.
     */
    @FunctionalInterface
    public interface CommandExecutor {
        /**
         * Executes the command and returns the result.
         *
         * @see seedu.address.logic.Logic#execute(String)
         */
        CommandResult execute(String commandText) throws CommandException, ParseException;
    }

}
