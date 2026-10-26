package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Client;

/**
 * Copies the currently displayed clients, or a selected displayed client, to the system clipboard.
 */
public class CopyClientCommand extends Command {

    public static final String COMMAND_WORD = "cpc";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Copies the currently displayed clients, or the "
            + "client at the specified index, to the clipboard.\n"
            + "Parameters: [INDEX] (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_SUCCESS = "Copied %1$d client(s) to clipboard.";
    public static final String MESSAGE_SUCCESS_SINGLE = "Copied client to clipboard.";

    private final Optional<Index> targetIndex;

    /** Creates a command that copies all currently displayed clients. */
    public CopyClientCommand() {
        this(Optional.empty());
    }

    /** Creates a command that copies the client at the specified displayed index. */
    public CopyClientCommand(Index targetIndex) {
        this(Optional.of(requireNonNull(targetIndex)));
    }

    /** Stores the optional displayed index; an empty value means copy all displayed clients. */
    private CopyClientCommand(Optional<Index> targetIndex) {
        this.targetIndex = requireNonNull(targetIndex);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Client> displayedClients = model.getFilteredClientList();
        if (targetIndex.isPresent()) {
            int zeroBasedIndex = targetIndex.get().getZeroBased();
            if (zeroBasedIndex >= displayedClients.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_CLIENT_DISPLAYED_INDEX);
            }
            return new CommandResult(MESSAGE_SUCCESS_SINGLE, Messages.format(displayedClients.get(zeroBasedIndex)));
        }

        String copiedText = displayedClients.stream()
                .map(Messages::format)
                .collect(Collectors.joining(System.lineSeparator()));

        return new CommandResult(String.format(MESSAGE_SUCCESS, displayedClients.size()), copiedText);
    }
}
