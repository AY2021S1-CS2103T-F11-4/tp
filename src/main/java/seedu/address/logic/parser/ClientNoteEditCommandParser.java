package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Set;
import java.util.stream.Stream;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.ClientNoteAddCommand;
import seedu.address.logic.commands.ClientNoteEditCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.note.Note;
import seedu.address.model.note.TagNoteMap;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new ClientNoteEditCommandParser object.
 */
public class ClientNoteEditCommandParser implements Parser<ClientNoteEditCommand> {

    private final TagNoteMap tagNoteMap;

    /**
     * Initializes a {@code ClientNoteEditCommandParser} with the {@code tagNoteMap} object.
     */
    public ClientNoteEditCommandParser(TagNoteMap tagNoteMap) {
        requireNonNull(tagNoteMap);
        this.tagNoteMap = tagNoteMap;
    }

    @Override
    public ClientNoteEditCommand parse(String restOfCommand) throws ParseException {
        requireNonNull(restOfCommand);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(restOfCommand, PREFIX_NOTE, PREFIX_TAG);
        // NEED TO PASS IN NOTE STRING AT LEAST, TAGS ARE OPTIONAL
        if (!arePrefixesPresent(argMultimap, PREFIX_NOTE) || argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    ClientNoteEditCommand.MESSAGE_USAGE));
        }
        Index targetClientIndex;
        Index targetClientNoteIndex;
        try {
            String[] splitPreamble = argMultimap.getPreamble().split(" ");
            if (splitPreamble.length != 2) { // restOfCommand: 1 1 all space delimited ==> 2 elems only
                throw new ParseException("nope");
            }
            targetClientIndex = ParserUtil.parseIndex(splitPreamble[splitPreamble.length - 2]);
            targetClientNoteIndex = ParserUtil.parseIndex(splitPreamble[splitPreamble.length - 1]);
            Set<Tag> tags = tagNoteMap.getUniqueTags(argMultimap.getAllValues(PREFIX_TAG));
            Note newNote = ParserUtil.parseNote(argMultimap.getValue(PREFIX_NOTE).orElseThrow(() -> new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    ClientNoteAddCommand.MESSAGE_USAGE))));
            newNote.setTags(tags);
            return new ClientNoteEditCommand(targetClientIndex, targetClientNoteIndex, newNote);
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    ClientNoteEditCommand.MESSAGE_USAGE), pe);
        }
    }
    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }


}
