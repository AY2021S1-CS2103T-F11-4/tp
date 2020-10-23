package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COUNTRY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_CLIENT;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.ClientAddCommand;
import seedu.address.logic.commands.ClientDeleteCommand;
import seedu.address.logic.commands.ClientEditCommand;
import seedu.address.logic.commands.ClientEditCommand.EditClientDescriptor;
import seedu.address.logic.commands.ClientFindCommand;
import seedu.address.logic.commands.ClientNoteAddCommand;
import seedu.address.logic.commands.ClientViewCommand;
import seedu.address.logic.commands.CountryFilterCommand;
import seedu.address.logic.commands.CountryNoteCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.client.Client;
import seedu.address.model.client.ClientCountryMatchesInputCountryPredicate;
import seedu.address.model.client.NameContainsKeywordsPredicate;
import seedu.address.model.country.Country;
import seedu.address.model.note.CountryNote;
import seedu.address.model.note.Note;
import seedu.address.model.note.TagNoteMap;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.ClientBuilder;
import seedu.address.testutil.ClientUtil;
import seedu.address.testutil.EditClientDescriptorBuilder;

public class AddressBookParserTest {

    private final TagNoteMap tagNoteMap = new TagNoteMap();
    private final AddressBookParser parser = new AddressBookParser(tagNoteMap);

    @Test
    public void parseClientCommands_addClient() throws Exception {
        Client client = new ClientBuilder().build();
        ClientAddCommand command = (ClientAddCommand) parser.parseCommand(ClientUtil.getAddCommand(client));
        assertEquals(new ClientAddCommand(client), command);
    }

    @Test
    public void parseClientCommands_editClient() throws Exception {
        Client client = new ClientBuilder().build();
        EditClientDescriptor descriptor = new EditClientDescriptorBuilder(client).build();
        ClientEditCommand command = (ClientEditCommand) parser.parseCommand(ClientEditCommand.COMMAND_WORD + " "
                + INDEX_FIRST_CLIENT.getOneBased() + " " + ClientUtil.getEditClientDescriptorDetails(descriptor));
        assertEquals(new ClientEditCommand(INDEX_FIRST_CLIENT, descriptor), command);
    }

    @Test
    public void parseClientCommands_deleteClient() throws Exception {
        ClientDeleteCommand command = (ClientDeleteCommand) parser.parseCommand(
                ClientDeleteCommand.COMMAND_WORD + " " + INDEX_FIRST_CLIENT.getOneBased());
        assertEquals(new ClientDeleteCommand(INDEX_FIRST_CLIENT), command);
    }

    @Test
    public void parseClientCommands_findClient() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        ClientFindCommand command = (ClientFindCommand) parser.parseCommand(
                ClientFindCommand.COMMAND_WORD + " " + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new ClientFindCommand(new NameContainsKeywordsPredicate(keywords)), command);
    }

    @Test
    public void parseClientCommands_viewClient() throws Exception {
        ClientViewCommand command = (ClientViewCommand) parser.parseCommand(
                ClientViewCommand.COMMAND_WORD + " " + INDEX_FIRST_CLIENT.getOneBased());
        assertEquals(new ClientViewCommand(INDEX_FIRST_CLIENT), command);
    }

    @Test
    public void parseClientCommands_unrecognisedInput_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                HelpCommand.MESSAGE_USAGE), () -> parser.parseCommand("client"));
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                HelpCommand.MESSAGE_USAGE), () -> parser.parseCommand("client "));
    }

    @Test
    public void parseClientCommands_unknownCommand_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, () ->
                parser.parseCommand("client unknownCommand"));
        assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, () ->
                parser.parseCommand("client unknownCommand "));
    }

    @Test
    public void parseCountryCommands_addCountryNote() throws Exception {
        final String countryString = "SG";
        final String noteString = "is hot";
        final String commandString = CountryNoteCommand.COMMAND_WORD + " " + PREFIX_COUNTRY + countryString
                + " " + PREFIX_NOTE + noteString;
        CountryNoteCommand command = (CountryNoteCommand) parser.parseCommand(commandString);
        assertEquals(
            new CountryNoteCommand(new CountryNote(noteString, new Country(countryString))),
            command);
    }

    @Test
    public void parseCountryCommands_countryFilter() throws Exception {
        final String countryString = "SG";
        final String commandString = CountryFilterCommand.COMMAND_WORD + " " + PREFIX_COUNTRY + countryString;
        CountryFilterCommand command = (CountryFilterCommand) parser.parseCommand(commandString);
        final ClientCountryMatchesInputCountryPredicate predicate =
                new ClientCountryMatchesInputCountryPredicate(new Country(countryString));
        assertEquals(new CountryFilterCommand(predicate), command);
    }

    @Test
    public void parseCountryCommands_unrecognisedInput_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                HelpCommand.MESSAGE_USAGE), () -> parser.parseCommand("country"));
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                HelpCommand.MESSAGE_USAGE), () -> parser.parseCommand("country "));
    }

    @Test
    public void parseCountryCommands_unknownCommand_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, () ->
                parser.parseCommand("country unknownCommand"));
        assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, () ->
                parser.parseCommand("country unknownCommand "));
    }

    @Test
    public void parseClientNoteCommands_addClientNote() throws Exception {
        final String noteString = "likes cats";
        final String commandString = ClientNoteAddCommand.COMMAND_WORD + " " + INDEX_FIRST_CLIENT.getOneBased()
                + " " + PREFIX_NOTE + noteString;
        TagNoteMap tagNoteMap = new TagNoteMap();
        Set<Tag> tags = tagNoteMap.getUniqueTags(Collections.emptyList());
        final Note note = new Note(noteString);
        note.setTags(tags);
        ClientNoteAddCommand command = (ClientNoteAddCommand) parser.parseCommand(commandString);
        assertEquals(new ClientNoteAddCommand(INDEX_FIRST_CLIENT, note), command);
    }

    @Test
    public void parseClientNoteCommands_unrecognisedInput_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                HelpCommand.MESSAGE_USAGE), () -> parser.parseCommand("client note"));
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                HelpCommand.MESSAGE_USAGE), () -> parser.parseCommand("client note "));
    }

    @Test
    public void parseClientNoteCommands_unknownCommand_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, () ->
                parser.parseCommand("client note unknownCommand"));
        assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, () ->
                parser.parseCommand("client note unknownCommand "));
    }

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD + " 3") instanceof ClearCommand);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
    }

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_list() throws Exception {
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD) instanceof ListCommand);
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD + " 3") instanceof ListCommand);
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE), ()
            -> parser.parseCommand(""));
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, () -> parser.parseCommand("unknownCommand"));
    }
}
