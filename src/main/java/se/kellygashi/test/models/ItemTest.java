package se.kellygashi.test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import se.kellygashi.main.models.Item;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ItemTest {

    @Test
    @DisplayName("Create item with uppercase ID")
    void createItemWithUppercaseId() {
        Item item = new Item("ID");
        assertEquals("id", item.toString());
    }

    @Test
    @DisplayName("Set item ID to null should fail")
    void setItemIdToNull() {
        Item item = new Item("ID");
        assertThrows(NullPointerException.class, () -> {
            item.setId(null);
        });
    }
}