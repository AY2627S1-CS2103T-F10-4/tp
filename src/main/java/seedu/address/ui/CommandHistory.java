package seedu.address.ui;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores submitted commands and the current browsing position for one command box session.
 */
class CommandHistory {
    private final List<String> history = new ArrayList<>();
    private int historyIndex;
    private String draft = "";

    /**
     * Records a nonblank submission and resets browsing to the draft position.
     * Blank submissions reset browsing but are not stored.
     *
     * @param command Non-null submitted input, preserved exactly as entered.
     */
    void add(String command) {
        if (!command.isBlank()) {
            history.add(command);
        }
        historyIndex = history.size();
        draft = "";
    }

    /**
     * Returns the previous command, saving the current input when browsing begins.
     * At the oldest entry, leaves the current input unchanged.
     *
     * @param currentInput Non-null input to save as the draft when browsing begins.
     * @return The older command, or the unchanged input when no older command exists.
     */
    String previous(String currentInput) {
        if (historyIndex == 0) {
            return currentInput;
        }
        if (historyIndex == history.size()) {
            draft = currentInput;
        }
        historyIndex--;
        return history.get(historyIndex);
    }

    /**
     * Returns the next command, or restores the draft after the newest command.
     * Outside history browsing, leaves the current input unchanged.
     *
     * @param currentInput Non-null input to preserve when not browsing history.
     * @return The newer command, the saved draft, or the unchanged input.
     */
    String next(String currentInput) {
        if (historyIndex == history.size()) {
            return currentInput;
        }
        historyIndex++;
        return historyIndex == history.size() ? draft : history.get(historyIndex);
    }
}
