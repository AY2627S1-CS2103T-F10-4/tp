package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOB;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMARK;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Client;

/**
 * Adds a client to the address book.
 */
public class AddClientCommand extends Command {

    public static final String COMMAND_WORD = "addc";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a client to TalentLink. "
            + "Parameters: "
            + PREFIX_NAME + "<name> "
            + PREFIX_PHONE + "<phone number> "
            + PREFIX_EMAIL + "<email> "
            + PREFIX_ADDRESS + "<address> "
            + "[" + PREFIX_REMARK + "<remarks>] "
            + "[" + PREFIX_JOB + "<job title>]... "
            + "[" + PREFIX_TAG + "<tag> [<more tags>]...]\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "Irfan Ibrahim "
            + PREFIX_PHONE + "92492021 "
            + PREFIX_EMAIL + "irfan@example.com "
            + PREFIX_ADDRESS + "Blk 47 Tampines Street 20, #17-35 "
            + PREFIX_REMARK + "immediate "
            + PREFIX_TAG + "classmates employer";

    public static final String MESSAGE_SUCCESS = "New client added: %1$s";
    public static final String MESSAGE_DUPLICATE_CLIENT = "This client already exists in TalentLink.";

    private final Client toAdd;

    /**
     * Creates an AddClientCommand to add the specified {@code Client}
     */
    public AddClientCommand(Client client) {
        requireNonNull(client);
        // A newly added client always starts unhidden; AddClientCommandParser guarantees this.
        assert !client.isHidden() : "A newly added client must not be hidden";
        toAdd = client;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasClient(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_CLIENT);
        }

        model.addClient(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(toAdd)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddClientCommand otherAddCommand)) {
            return false;
        }

        return toAdd.equals(otherAddCommand.toAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toAdd", toAdd)
                .toString();
    }
}
