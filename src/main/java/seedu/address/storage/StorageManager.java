package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.ReadOnlyTbmManager;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.UserPrefs;

/**
 * Manages storage of TbmManager data in local storage.
 */
public class StorageManager implements Storage {

    private static final Logger logger = LogsCenter.getLogger(StorageManager.class);
    private final TbmManagerStorage tbmManagerStorage;
    private final UserPrefsStorage userPrefsStorage;

    /**
     * Creates a {@code StorageManager} with the given {@code TbmManagerStorage} and {@code UserPrefStorage}.
     */
    public StorageManager(TbmManagerStorage tbmManagerStorage, UserPrefsStorage userPrefsStorage) {
        super();
        this.tbmManagerStorage = tbmManagerStorage;
        this.userPrefsStorage = userPrefsStorage;
    }

    // ================ UserPrefs methods ==============================

    @Override
    public Path getUserPrefsFilePath() {
        return userPrefsStorage.getUserPrefsFilePath();
    }

    @Override
    public Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException {
        return userPrefsStorage.readUserPrefs();
    }

    @Override
    public void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException {
        userPrefsStorage.saveUserPrefs(userPrefs);
    }


    // ================ TbmManager methods ==============================

    @Override
    public Path getTbmManagerFilePath() {
        return tbmManagerStorage.getTbmManagerFilePath();
    }

    @Override
    public Optional<ReadOnlyTbmManager> readTbmManager() throws DataConversionException, IOException {
        return readTbmManager(tbmManagerStorage.getTbmManagerFilePath());
    }

    @Override
    public Optional<ReadOnlyTbmManager> readTbmManager(Path filePath) throws DataConversionException, IOException {
        logger.fine("Attempting to read data from file: " + filePath);
        return tbmManagerStorage.readTbmManager(filePath);
    }

    @Override
    public void saveTbmManager(ReadOnlyTbmManager tbmManager) throws IOException {
        saveTbmManager(tbmManager, tbmManagerStorage.getTbmManagerFilePath());
    }

    @Override
    public void saveTbmManager(ReadOnlyTbmManager tbmManager, Path filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        tbmManagerStorage.saveTbmManager(tbmManager, filePath);
    }

}
