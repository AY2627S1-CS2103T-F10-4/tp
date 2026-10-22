package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.stream.Collectors;

import seedu.address.logic.Messages;
import seedu.address.model.Model;

/**
 * Copies the currently displayed clients to the system clipboard.
 */
public class CopyClientCommand extends Command {

    public static final String COMMAND_WORD = "cpc";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Copies all currently displayed clients to the "
            + "clipboard.\n"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "Copied %1$d client(s) to clipboard.";

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);

        String copiedText = model.getFilteredClientList().stream()
                .map(Messages::format)
                .collect(Collectors.joining(System.lineSeparator()));
        int copiedClientCount = model.getFilteredClientList().size();

        return new CommandResult(String.format(MESSAGE_SUCCESS, copiedClientCount), copiedText);
    }
}
