package seedu.address.model.country;

import static java.util.Objects.requireNonNull;

import java.util.Objects;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.note.CountryNote;

/**
 * A high-level class that is responsible for mapping ISO3166 countries to countryNotes.
 */
public class CountryNotesManager {

    private final ObservableList<CountryNote> internalCountryNoteList = FXCollections.observableArrayList();
    private final ObservableList<CountryNote> internalCountryNoteUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalCountryNoteList);

    /**
     * Checks if {@code countryNote} already exists.
     *
     * @param countryNote The note to be checked.
     * @return Whether {@code countryNote} already exists.
     */
    public boolean hasCountryNote(CountryNote countryNote) {
        requireNonNull(countryNote);
        return internalCountryNoteList.contains(countryNote);
    }

    /**
     * Adds the {@code countryNote}.
     *
     * @param countryNote The country note to be added.
     */
    public void addCountryNote(CountryNote countryNote) {
        assert CountryCodeVerifier.isValidCountryCode(countryNote.getCountry().getCountryCode())
                : "The country note to be added is associated with an invalid country";
        requireNonNull(countryNote);

        if (!hasCountryNote(countryNote)) {
            internalCountryNoteList.add(countryNote);
        }
    }

    /**
     * Returns all Country Notes as an unmodifiable List.
     *
     * @return Unmodifiable list of all country notes.
     */
    public ObservableList<CountryNote> asUnmodifiableObservableList() {
        return internalCountryNoteUnmodifiableList;
    }

    /**
     * Deletes the given country note.
     *
     * @param countryNoteToDelete The country note to be deleted.
     */
    public void deleteCountryNote(CountryNote countryNoteToDelete) {
        assert CountryCodeVerifier.isValidCountryCode(countryNoteToDelete.getCountry().getCountryCode())
                : "The country note to be deleted is associated with an invalid country";
        requireNonNull(countryNoteToDelete);
        assert hasCountryNote(countryNoteToDelete);

        internalCountryNoteList.remove(countryNoteToDelete);
    }

    /**
     * Replaces the old country note with the new country note.
     *
     * @param oldCountryNote The old country note.
     * @param newCountryNote The new country note.
     */
    public void setCountryNote(CountryNote oldCountryNote, CountryNote newCountryNote) {
        assert hasCountryNote(oldCountryNote) : "old country note must exist in internal list";
        int targetIndx = internalCountryNoteList.indexOf(oldCountryNote);
        internalCountryNoteList.set(targetIndx, newCountryNote);
    }

    /**
     * Clears all country notes in the CountryNotesManager.
     */
    public void clear() {
        internalCountryNoteList.clear();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof CountryNotesManager // instanceof handles nulls
                && internalCountryNoteList
                        .equals(((CountryNotesManager) other).internalCountryNoteList)); // state check
    }

    @Override
    public int hashCode() {
        return Objects.hash(internalCountryNoteList);
    }

}
