package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.ReadOnlyTbmManager;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.UserPrefs;

/**
 * API of the Storage component.
 */
public interface Storage extends TbmManagerStorage, UserPrefsStorage {

    @Override
    Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException;

    @Override
    void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException;

    @Override
    Path getTbmManagerFilePath();

    @Override
    Optional<ReadOnlyTbmManager> readTbmManager() throws DataConversionException, IOException;

    @Override
    void saveTbmManager(ReadOnlyTbmManager tbmManager) throws IOException;

}
