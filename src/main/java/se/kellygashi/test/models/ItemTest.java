package se.kellygashi.test.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import se.kellygashi.test.helpers.ItemManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ItemTest {

    private ItemManager itemManager;

    @BeforeEach
    public void setUp() {
        itemManager = new ItemManager("initial");
    }

    @Test
    @DisplayName("Create item with uppercase ID")
    void createItemWithUppercaseId() {
        itemManager.updateItemId("ID");
        assertEquals("id", itemManager.getItemId(), "ID should be converted to lowercase");
    }

    @Test
    @DisplayName("Set item ID to null should fail")
    void setItemIdToNull() {
        assertThrows(NullPointerException.class, () -> itemManager.updateItemId(null), "Setting ID to null should throw NullPointerException");
    }

    @Test
    @DisplayName("Create item with empty ID")
    void createItemWithEmptyId() {
        itemManager.updateItemId("");
        assertEquals("", itemManager.getItemId(), "Setting empty ID should result in empty ID");
    }

    @Test
    @DisplayName("Create item with null ID")
    void createItemWithNullId() {
        assertThrows(NullPointerException.class, () -> new ItemManager(null), "Creating ItemManager with null ID should throw NullPointerException");
    }

    @Test
    @DisplayName("Create item with mixed case ID")
    void createItemWithMixedCaseId() {
        itemManager.updateItemId("TeStId");
        assertEquals("testid", itemManager.getItemId(), "Mixed case ID should be converted to lowercase");
    }

    @Test
    @DisplayName("Set item ID with valid string")
    void setItemIdWithValidString() {
        itemManager.updateItemId("newId");
        assertEquals("newid", itemManager.getItemId(), "Valid string ID should be updated correctly");
    }

    @Test
    @DisplayName("Set item ID with empty string")
    void setItemIdWithEmptyString() {
        itemManager.updateItemId("");
        assertEquals("", itemManager.getItemId(), "Empty string ID should be set correctly");
    }

    @Test
    @DisplayName("Set item ID with mixed case string")
    void setItemIdWithMixedCaseString() {
        itemManager.updateItemId("MiXeDCaSe");
        assertEquals("mixedcase", itemManager.getItemId(), "Mixed case string ID should be converted to lowercase");
    }

    @Test
    @DisplayName("Test toString method")
    void testToStringMethod() {
        itemManager.updateItemId("testId");
        assertEquals("testid", itemManager.getItemId(), "toString method should return the lowercase ID");
    }

}
