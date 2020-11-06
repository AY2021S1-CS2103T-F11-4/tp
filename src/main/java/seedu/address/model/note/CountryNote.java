package seedu.address.model.note;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Objects;
import java.util.Set;

import seedu.address.model.country.Country;
import seedu.address.model.tag.Tag;

/**
 * The class that models a country note.
 */
public class CountryNote extends Note {

    public static final CountryNote NULL_COUNTRY_NOTE = new CountryNote("", Country.NULL_COUNTRY);

    private final Country country;

    /**
     * Initializes a country note.
     *
     * @param content The content of the country note.
     * @param country The country that is associate with the note.
     */
    public CountryNote(String content, Country country) {
        super(content);

        requireNonNull(country);
        this.country = country;
    }

    /**
     * Initializes the country note with the given content, country and tags.
     *
     * @param content The content of the country note.
     * @param country The country that the country note belongs to.
     * @param tags The tags that are associated with the country note.
     */
    public CountryNote(String content, Country country, Set<Tag> tags) {
        super(content);
        requireAllNonNull(content, country, tags);
        this.country = country;
        super.setTags(tags);
    }

    /**
     * Gets the country that is being associated with this country note.
     *
     * @return The country that is being associated with this country note.
     */
    public Country getCountry() {
        return country;
    }

    @Override
    public boolean isClientNote() {
        return false;
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (this == other) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof CountryNote)) {
            return false;
        }

        // state check, super checks for note content and tags
        return super.equals(other) && country.equals(((CountryNote) other).country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), country);
    }

    @Override
    public String toString() {
        return "[" + getCountry() + "] " + super.toString();
    }

}
