package foodwhere.logic.parser;

import static foodwhere.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import foodwhere.commons.core.index.Index;
import foodwhere.logic.commands.SDeleteCommand;
import foodwhere.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new SDeleteCommand object
 */
public class SDeleteCommandParser implements Parser<SDeleteCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the SDeleteCommand
     * and returns a SDeleteCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public SDeleteCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new SDeleteCommand(index);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, SDeleteCommand.MESSAGE_USAGE), pe);
        }
    }

}