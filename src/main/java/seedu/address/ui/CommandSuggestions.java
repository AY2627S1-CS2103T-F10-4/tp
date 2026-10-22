package seedu.address.ui;

import java.util.List;

import seedu.address.logic.commands.CommandCatalogue;

/**
 * Matches incomplete command names without depending on JavaFX.
 */
class CommandSuggestions {
    private final List<String> commandWords;

    /**
     * Creates a matcher for the application's supported command names.
     */
    CommandSuggestions() {
        this(CommandCatalogue.COMMAND_WORDS);
    }

    /**
     * Creates a matcher using an immutable snapshot of the supplied command names.
     *
     * @param commandWords Non-null command names to offer for completion.
     */
    CommandSuggestions(List<String> commandWords) {
        this.commandWords = List.copyOf(commandWords);
    }

    /**
     * Returns sorted, case-sensitive matches while the user is entering a command name.
     * Ignores leading whitespace; blank input, exact commands and input containing arguments have no suggestions.
     *
     * @param input Non-null text currently in the command field.
     * @return An immutable list of matching command names.
     */
    List<String> suggest(String input) {
        String prefix = input.stripLeading();
        if (prefix.isEmpty() || prefix.chars().anyMatch(Character::isWhitespace)
                || commandWords.contains(prefix)) {
            return List.of();
        }
        return commandWords.stream()
                .filter(word -> word.startsWith(prefix))
                .sorted()
                .toList();
    }
}
