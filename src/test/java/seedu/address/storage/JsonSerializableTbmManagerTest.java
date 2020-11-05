package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.JsonUtil;
import seedu.address.model.TbmManager;
import seedu.address.testutil.TypicalClients;

public class JsonSerializableTbmManagerTest {

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonSerializableTbmManagerTest");
    private static final Path TYPICAL_CLIENTS_FILE = TEST_DATA_FOLDER.resolve("typicalClientsTbmManager.json");
    private static final Path INVALID_CLIENT_FILE = TEST_DATA_FOLDER.resolve("invalidClientTbmManager.json");
    private static final Path DUPLICATE_CLIENT_FILE = TEST_DATA_FOLDER.resolve("duplicateClientTbmManager.json");

    @Test
    public void toModelType_typicalClientsFile_success() throws Exception {
        JsonSerializableTbmManager dataFromFile = JsonUtil.readJsonFile(TYPICAL_CLIENTS_FILE,
                JsonSerializableTbmManager.class).orElse(null);
        assert dataFromFile != null : "null JsonSerializableTbmManager object assigned";
        TbmManager tbmManagerFromFile = dataFromFile.toModelType();
        TbmManager typicalClientsTbmManager = TypicalClients.getTypicalTbmManager();
        assertEquals(tbmManagerFromFile.getClientList(), typicalClientsTbmManager.getClientList());
    }

    @Test
    public void toModelType_invalidClientFile_throwsIllegalValueException() throws Exception {
        JsonSerializableTbmManager dataFromFile = JsonUtil.readJsonFile(INVALID_CLIENT_FILE,
                JsonSerializableTbmManager.class).orElse(null);
        assert dataFromFile != null : "null JsonSerializableTbmManager object assigned";
        assertThrows(IllegalValueException.class, dataFromFile::toModelType);
    }

    @Test
    public void toModelType_duplicateClients_throwsIllegalValueException() throws Exception {
        JsonSerializableTbmManager dataFromFile = JsonUtil.readJsonFile(DUPLICATE_CLIENT_FILE,
                JsonSerializableTbmManager.class).orElse(null);
        assert dataFromFile != null : "null JsonSerializableTbmManager object assigned";
        assertThrows(IllegalValueException.class, JsonSerializableTbmManager.MESSAGE_DUPLICATE_CLIENT,
                dataFromFile::toModelType);
    }

}
