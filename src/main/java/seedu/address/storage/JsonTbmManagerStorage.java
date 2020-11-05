package seedu.address.storage;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.FileUtil;
import seedu.address.commons.util.JsonUtil;
import seedu.address.model.ReadOnlyTbmManager;

/**
 * A class to access TbmManager data stored as a json file on the hard disk.
 */
public class JsonTbmManagerStorage implements TbmManagerStorage {

    private static final Logger logger = LogsCenter.getLogger(JsonTbmManagerStorage.class);

    private final Path filePath;

    /**
     * Constructs a {@code JsonTbmManagerStorage} with an associated {@code Path} for storage.
     * @param filePath {@code Path} where the {@code TbmManager} is stored as a JSON object.
     */
    public JsonTbmManagerStorage(Path filePath) {
        this.filePath = filePath;
    }

    /**
     * Gets the {@code Path} where the {@code TbmManager} is stored as a JSON object.
     * @return {@code Path} where {@code TbmManager} is stored.
     */
    public Path getTbmManagerFilePath() {
        return filePath;
    }

    @Override
    public Optional<ReadOnlyTbmManager> readTbmManager() throws DataConversionException {
        return readTbmManager(filePath);
    }

    /**
     * Similar to {@link #readTbmManager()}.
     *
     * @param filePath location of the data. Cannot be null.
     * @throws DataConversionException if the file is not in the correct format.
     */
    public Optional<ReadOnlyTbmManager> readTbmManager(Path filePath) throws DataConversionException {
        requireNonNull(filePath);

        Optional<JsonSerializableTbmManager> jsonTbmManager = JsonUtil.readJsonFile(
                filePath, JsonSerializableTbmManager.class);
        if (jsonTbmManager.isEmpty()) {
            return Optional.empty();
        }

        try {
            return Optional.of(jsonTbmManager.get().toModelType());
        } catch (IllegalValueException ive) {
            logger.info("Illegal values found in " + filePath + ": " + ive.getMessage());
            throw new DataConversionException(ive);
        }
    }

    @Override
    public void saveTbmManager(ReadOnlyTbmManager tbmManager) throws IOException {
        saveTbmManager(tbmManager, filePath);
    }

    /**
     * Similar to {@link #saveTbmManager(ReadOnlyTbmManager)}.
     *
     * @param filePath location of the data. Cannot be null.
     */
    public void saveTbmManager(ReadOnlyTbmManager tbmManager, Path filePath) throws IOException {
        requireAllNonNull(tbmManager, filePath);

        FileUtil.createIfMissing(filePath);
        JsonUtil.saveJsonFile(new JsonSerializableTbmManager(tbmManager), filePath);
    }

}
