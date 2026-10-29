package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Arrays;
import java.util.Set;
import java.util.function.BiFunction;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AbstractTagCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.Tag;

/**
 * Parses the index and tags shared by all tag and untag commands.
 *
 * @param <T> Type of command to create.
 */
public class TagCommandParser<T extends AbstractTagCommand> implements Parser<T> {
    private final BiFunction<Index, Set<Tag>, T> commandFactory;
    private final String messageUsage;

    /** Creates a parser with the concrete command's factory and usage message. */
    protected TagCommandParser(BiFunction<Index, Set<Tag>, T> commandFactory, String messageUsage) {
        this.commandFactory = requireNonNull(commandFactory);
        this.messageUsage = requireNonNull(messageUsage);
    }

    @Override
    public T parse(String args) throws ParseException {
        requireNonNull(args);
        String[] parts = args.trim().split("\\s+", 2);
        if (parts.length < 2) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, messageUsage));
        }
        Index index = ParserUtil.parseIndex(parts[0]);
        Set<Tag> tags = ParserUtil.parseTags(Arrays.asList(parts[1].split("\\s+")));
        return commandFactory.apply(index, tags);
    }
}
