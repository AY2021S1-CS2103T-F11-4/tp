package seedu.address.model.country;

import java.util.Locale;

/**
 * The class that is responsible for verification of ISO3166 country-codes.
 */
public class CountryCodeVerifier {

    public static final String MESSAGE_CONSTRAINTS = "Country code must be a valid "
            + "2-letter ISO3166 country code";
    private static final String[] COUNTRY_CODES = Locale.getISOCountries();

    /**
     * Checks if countryCode is a valid ISO3166 code.
     *
     * @param countryCode The country code.
     * @return Whether countryCode is a valid ISO3166 code.
     */
    public static boolean isValidCountryCode(String countryCode) {
        for (int i = 0; i < COUNTRY_CODES.length; i++) {
            if (COUNTRY_CODES[i].equals(countryCode)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the array of valid country-codes.
     *
     * @return The array of valid country-codes.
     */
    public static String[] getCountryCodes() {
        return COUNTRY_CODES;
    }
}
