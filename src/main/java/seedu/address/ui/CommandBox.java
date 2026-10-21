package seedu.address.ui;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * The UI component that is responsible for receiving user command inputs.
 */
public class CommandBox extends UiPart<Region> {

    public static final String ERROR_STYLE_CLASS = "error";
    private static final String FXML = "CommandBox.fxml";
    private static final int MAX_VISIBLE_SUGGESTIONS = 5;
    private static final int SUGGESTION_HEIGHT_PADDING = 2;

    private final CommandExecutor commandExecutor;
    private final CommandHistory commandHistory = new CommandHistory();
    private final CommandSuggestions commandSuggestions;
    private boolean areSuggestionsSuppressed;

    @FXML
    private TextField commandTextField;

    @FXML
    private ListView<String> suggestionList;

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
     * Limits the list height so longer lists scroll instead of displacing more of the main window.
     */
    private void updateSuggestions() {
        suggestionList.getItems().setAll(commandSuggestions.suggest(commandTextField.getText()));
        boolean isVisible = !suggestionList.getItems().isEmpty();
        suggestionList.setVisible(isVisible);
        suggestionList.setManaged(isVisible);
        int visibleRows = Math.min(MAX_VISIBLE_SUGGESTIONS, suggestionList.getItems().size());
        suggestionList.setPrefHeight(visibleRows * suggestionList.getFixedCellSize() + SUGGESTION_HEIGHT_PADDING);
        suggestionList.getSelectionModel().selectFirst();
    }

    /**
     * Hides suggestions and releases their space in the window layout.
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
