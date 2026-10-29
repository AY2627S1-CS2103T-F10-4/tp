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
 * Adds tags to a client identified by their displayed index.
 */
public class TagClientCommand extends AbstractTagCommand {

    public static final String COMMAND_WORD = "tagc";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Adds tags to the client identified by their displayed index.\n"
            + "Parameters: <index> <tag>...\n"
            + "Example: " + COMMAND_WORD + " 1 priority finance";
    public static final String MESSAGE_SUCCESS = "Updated client tags: %1$s";
    public static final String MESSAGE_NO_CHANGE = "This client already has all the specified tags.";

    /**
     * Creates a command to add {@code tagsToAdd} to the client at {@code targetIndex}.
     * A defensive copy of the tags is stored.
     */
    public TagClientCommand(Index targetIndex, Set<Tag> tagsToAdd) {
        super(targetIndex, tagsToAdd, TagOperation.ADD);
    }

    /**
     * Adds tags to the client at the displayed index, preserving existing tags and ignoring duplicates.
     *
     * @return Feedback indicating whether tags were added.
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
