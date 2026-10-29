package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Client;
import seedu.address.model.tag.Tag;

/**
 * Removes tags from a client identified by their displayed index.
 */
public class UntagClientCommand extends AbstractTagCommand {

    public static final String COMMAND_WORD = "untagc";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Removes tags from the client identified by their displayed index.\n"
            + "Parameters: <index> <tag>...\n"
            + "Example: " + COMMAND_WORD + " 1 priority finance";
    public static final String MESSAGE_SUCCESS = "Updated client tags: %1$s";
    public static final String MESSAGE_NO_CHANGE = "This client does not have any of the specified tags.";

    /**
     * Creates a command to remove {@code tagsToRemove} from the client at {@code targetIndex}.
     * A defensive copy of the tags is stored.
     */
    public UntagClientCommand(Index targetIndex, Set<Tag> tagsToRemove) {
        super(targetIndex, tagsToRemove, TagOperation.REMOVE);
    }

    /**
     * Removes specified tags from the client at the displayed index, preserving other tags and ignoring missing tags.
     *
     * @return Feedback indicating whether tags were removed.
     * @throws CommandException If the index is outside the currently displayed client list.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        Client target = getTarget(model.getFilteredClientList(),
                Messages.MESSAGE_INVALID_CLIENT_DISPLAYED_INDEX);
        Optional<Set<Tag>> updatedTags = getUpdatedTags(target);
        if (updatedTags.isEmpty()) {
            return new CommandResult(MESSAGE_NO_CHANGE);
        }

        Client updatedClient = new Client(target.getName(), target.getPhone(), target.getEmail(),
                target.getAddress(), updatedTags.get(), target.getRemark(), target.getAvailableJobs(),
                target.isHidden());
        model.setClient(target, updatedClient);
        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(updatedClient)));
    }

}
