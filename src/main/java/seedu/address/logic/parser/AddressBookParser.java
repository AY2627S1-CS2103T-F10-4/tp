package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_COMMAND_TOO_LONG;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.commons.core.InputLimits;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.AddClientCommand;
import seedu.address.logic.commands.AddJobseekerCommand;
import seedu.address.logic.commands.ClearClientCommand;
import seedu.address.logic.commands.ClearJobseekerCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CopyClientCommand;
import seedu.address.logic.commands.CopyJobseekerCommand;
import seedu.address.logic.commands.DeleteClientCommand;
import seedu.address.logic.commands.DeleteJobseekerCommand;
import seedu.address.logic.commands.EditClientCommand;
import seedu.address.logic.commands.EditJobseekerCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FindClientCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.FindHiddenClientCommand;
import seedu.address.logic.commands.FindHiddenJobseekerCommand;
import seedu.address.logic.commands.FindJobseekerCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.HideClientCommand;
import seedu.address.logic.commands.HideJobseekerCommand;
import seedu.address.logic.commands.ListClientCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.ListHiddenClientCommand;
import seedu.address.logic.commands.ListHiddenJobseekerCommand;
import seedu.address.logic.commands.ListJobseekerCommand;
import seedu.address.logic.commands.TagClientCommand;
import seedu.address.logic.commands.TagJobseekerCommand;
import seedu.address.logic.commands.UnhideClientCommand;
import seedu.address.logic.commands.UnhideJobseekerCommand;
import seedu.address.logic.commands.UntagClientCommand;
import seedu.address.logic.commands.UntagJobseekerCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses user input.
 */
public class AddressBookParser {

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");
    private static final Logger logger = LogsCenter.getLogger(AddressBookParser.class);

    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string.
     * @return the command based on the user input.
     * @throws ParseException if the user input does not conform to the expected format.
     */
    public Command parseCommand(String userInput) throws ParseException {
        if (userInput.length() > InputLimits.MAX_COMMAND_LENGTH) {
            throw new ParseException(MESSAGE_COMMAND_TOO_LONG);
        }
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");

        // Note to developers: Change LOG_LEVEL in LogsCenter to enable lower level (i.e., FINE, FINER and lower)
        // log messages such as the one below.
        // Lower level log messages are used sparingly to minimize noise in the code.
        logger.fine("Command word: " + commandWord + "; Arguments: " + arguments);

        return switch (commandWord) {
            case AddClientCommand.COMMAND_WORD -> new AddClientCommandParser().parse(arguments);
            case AddJobseekerCommand.COMMAND_WORD -> new AddJobseekerCommandParser().parse(arguments);
            case EditJobseekerCommand.COMMAND_WORD -> new EditCommandParser().parse(arguments);
            case EditClientCommand.COMMAND_WORD -> new EditClientCommandParser().parse(arguments);
            case DeleteJobseekerCommand.COMMAND_WORD -> new DeleteJobseekerCommandParser().parse(arguments);
            case DeleteClientCommand.COMMAND_WORD -> new DeleteClientCommandParser().parse(arguments);
            case HideJobseekerCommand.COMMAND_WORD -> new HideJobseekerCommandParser().parse(arguments);
            case HideClientCommand.COMMAND_WORD -> new HideClientCommandParser().parse(arguments);
            case UnhideJobseekerCommand.COMMAND_WORD -> new UnhideJobseekerCommandParser().parse(arguments);
            case UnhideClientCommand.COMMAND_WORD -> new UnhideClientCommandParser().parse(arguments);
            case TagJobseekerCommand.COMMAND_WORD -> new TagJobseekerCommandParser().parse(arguments);
            case UntagJobseekerCommand.COMMAND_WORD -> new UntagJobseekerCommandParser().parse(arguments);
            case TagClientCommand.COMMAND_WORD -> new TagClientCommandParser().parse(arguments);
            case UntagClientCommand.COMMAND_WORD -> new UntagClientCommandParser().parse(arguments);
            case ClearJobseekerCommand.COMMAND_WORD -> new ClearJobseekerCommand();
            case ClearClientCommand.COMMAND_WORD -> new ClearClientCommand();
            case CopyJobseekerCommand.COMMAND_WORD -> new CopyJobseekerCommandParser().parse(arguments);
            case CopyClientCommand.COMMAND_WORD -> new CopyClientCommandParser().parse(arguments);
            case FindCommand.COMMAND_WORD -> new FindCommandParser().parse(arguments);
            case FindJobseekerCommand.COMMAND_WORD -> new FindJobseekerCommandParser().parse(arguments);
            case FindClientCommand.COMMAND_WORD -> new FindClientCommandParser().parse(arguments);
            case FindHiddenJobseekerCommand.COMMAND_WORD -> new FindHiddenJobseekerCommandParser().parse(arguments);
            case FindHiddenClientCommand.COMMAND_WORD -> new FindHiddenClientCommandParser().parse(arguments);
            case ListCommand.COMMAND_WORD -> new ListCommand();
            case ListJobseekerCommand.COMMAND_WORD -> new ListJobseekerCommand();
            case ListClientCommand.COMMAND_WORD -> new ListClientCommand();
            case ListHiddenJobseekerCommand.COMMAND_WORD -> new ListHiddenJobseekerCommand();
            case ListHiddenClientCommand.COMMAND_WORD -> new ListHiddenClientCommand();
            case ExitCommand.COMMAND_WORD -> new ExitCommand();
            case HelpCommand.COMMAND_WORD -> new HelpCommand();
            default -> {
                logger.finer("This user input caused a ParseException: " + userInput);
                throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
            }
        };
    }

}
