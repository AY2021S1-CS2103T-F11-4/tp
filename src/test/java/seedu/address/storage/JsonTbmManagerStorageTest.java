package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalClients.ALICE;
import static seedu.address.testutil.TypicalClients.HOON;
import static seedu.address.testutil.TypicalClients.IDA;
import static seedu.address.testutil.TypicalClients.getTypicalTbmManager;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.ReadOnlyTbmManager;
import seedu.address.model.TbmManager;

public class JsonTbmManagerStorageTest {

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonTbmManagerStorageTest");

    @TempDir
    public Path testFolder;

    @Test
    public void readTbmManager_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> readTbmManager(null));
    }

    private java.util.Optional<ReadOnlyTbmManager> readTbmManager(String filePath) throws Exception {
        return new JsonTbmManagerStorage(Paths.get(filePath)).readTbmManager(addToTestDataPathIfNotNull(filePath));
    }

    private Path addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null
                ? TEST_DATA_FOLDER.resolve(prefsFileInTestDataFolder)
                : null;
    }

    @Test
    public void read_missingFile_emptyResult() throws Exception {
        assertFalse(readTbmManager("NonExistentFile.json").isPresent());
    }

    @Test
    public void read_notJsonFormat_exceptionThrown() {
        assertThrows(DataConversionException.class, () -> readTbmManager("notJsonFormatTbmManager.json"));
    }

    @Test
    public void readTbmManager_invalidClientTbmManager_throwDataConversionException() {
        assertThrows(DataConversionException.class, () -> readTbmManager("invalidClientTbmManager.json"));
    }

    @Test
    public void readTbmManager_invalidAndValidClientTbmManager_throwDataConversionException() {
        assertThrows(DataConversionException.class, () -> readTbmManager("invalidAndValidClientTbmManager.json"));
    }

    @Test
    public void readAndSaveTbmManager_allInOrder_success() throws Exception {
        Path filePath = testFolder.resolve("TempTbmManager.json");
        TbmManager original = getTypicalTbmManager();
        JsonTbmManagerStorage jsonTbmManagerStorage = new JsonTbmManagerStorage(filePath);

        // Save in new file and read back
        jsonTbmManagerStorage.saveTbmManager(original, filePath);
        ReadOnlyTbmManager readBack = jsonTbmManagerStorage.readTbmManager(filePath).orElse(null);
        assertEquals(original, new TbmManager(readBack));

        // Modify data, overwrite exiting file, and read back
        original.addClient(HOON);
        original.removeClient(ALICE);
        jsonTbmManagerStorage.saveTbmManager(original, filePath);
        readBack = jsonTbmManagerStorage.readTbmManager(filePath).orElse(null);
        assertEquals(original, new TbmManager(readBack));

        // Save and read without specifying file path
        original.addClient(IDA);
        jsonTbmManagerStorage.saveTbmManager(original); // file path not specified
        readBack = jsonTbmManagerStorage.readTbmManager().orElse(null); // file path not specified
        assertEquals(original, new TbmManager(readBack));

    }

    @Test
    public void saveTbmManager_nullTbmManager_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveTbmManager(null, "SomeFile.json"));
    }

    /**
     * Saves {@code tbmManager} at the specified {@code filePath}.
     */
    private void saveTbmManager(ReadOnlyTbmManager tbmManager, String filePath) {
        try {
            new JsonTbmManagerStorage(Paths.get(filePath))
                    .saveTbmManager(tbmManager, addToTestDataPathIfNotNull(filePath));
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file.", ioe);
        }
    }

    @Test
    public void saveTbmManager_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveTbmManager(new TbmManager(), null));
    }

}
