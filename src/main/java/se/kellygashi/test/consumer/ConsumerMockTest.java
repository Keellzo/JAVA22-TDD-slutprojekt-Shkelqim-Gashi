package se.kellygashi.test.consumer;

import org.junit.jupiter.api.*;
import se.kellygashi.test.helpers.BufferManager;
import se.kellygashi.test.helpers.ItemManager;

import static org.junit.jupiter.api.Assertions.*;

public class ConsumerMockTest {

    private BufferManager bufferManager;
    private ConsumerMock consumerMock;

    @BeforeEach
    public void setUp() {
        bufferManager = new BufferManager();
        consumerMock = new ConsumerMock(bufferManager);
    }

    private ItemManager createItemManager(String id) {
        return new ItemManager(id);
    }

    @Test
    @DisplayName("Test consumeItem Removes and Returns Correct Item")
    public void testConsumeItem() {
        ItemManager itemManager = createItemManager("item1");
        bufferManager.add(itemManager);
        assertEquals(itemManager, consumerMock.consumeItem(), "consumeItem should remove and return the first item");
    }


    @Test
    @DisplayName("Test consumeItem with Multiple Items in Buffer")
    public void testConsumeItemWithMultipleItems() {
        ItemManager firstItem = createItemManager("item1");
        ItemManager secondItem = createItemManager("item2");
        bufferManager.add(firstItem);
        bufferManager.add(secondItem);

        assertEquals(firstItem, consumerMock.consumeItem(), "First consume should return the first item");
        assertEquals(secondItem, consumerMock.consumeItem(), "Second consume should return the second item");
    }


    @Test
    @DisplayName("Test consumeItem Does Not Alter Other Buffer Elements")
    public void testConsumeItemDoesNotAlterBuffer() {
        ItemManager firstItem = createItemManager("item1");
        ItemManager secondItem = createItemManager("item2");
        bufferManager.add(firstItem);
        bufferManager.add(secondItem);

        consumerMock.consumeItem(); // Consume the first item
        assertFalse(bufferManager.getBufferQueue().contains(firstItem), "First item should be consumed");
        assertTrue(bufferManager.getBufferQueue().contains(secondItem), "Second item should remain in the buffer");
    }

    @Test
    @DisplayName("Test Consume Item on Buffer with One Item")
    public void testConsumeItemOnBufferWithOneItem() {
        ItemManager item = createItemManager("item1");
        bufferManager.add(item);

        assertEquals(item, consumerMock.consumeItem(), "Consume should return the only item");
        assertTrue(bufferManager.getBufferQueue().isEmpty(), "Buffer should be empty after consuming the only item");
    }

    @Test
    @DisplayName("Test Consume Item on Buffer with Duplicate Items")
    public void testConsumeItemOnBufferWithDuplicateItems() {
        ItemManager item1 = createItemManager("item");
        ItemManager item2 = createItemManager("item");
        bufferManager.add(item1);
        bufferManager.add(item2);

        assertEquals(item1, consumerMock.consumeItem(), "First consume should return the first 'item'");
        assertEquals(item2, consumerMock.consumeItem(), "Second consume should return the second 'item'");
    }


    @Test
    @DisplayName("Test consumeItem on Empty Buffer with Timeout")
    public void testConsumeItemOnEmptyBuffer() {
        Thread consumerThread = new Thread(() -> {
            ItemManager consumedItem = (ItemManager) consumerMock.consumeItem();
            assertNull(consumedItem, "No item should be consumed from an empty buffer");
        });

        consumerThread.start();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        assertTrue(consumerThread.isAlive(), "Consumer should be waiting as buffer is empty");

        consumerThread.interrupt();
        try {
            consumerThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


    @Test
    @DisplayName("Consume Single Item in Buffer")
    public void testConsumeSingleItem() {
        ItemManager singleItem = createItemManager("single");
        bufferManager.add(singleItem);
        assertEquals(singleItem, consumerMock.consumeItem(), "Consume should return the single item");
        assertTrue(bufferManager.getBufferQueue().isEmpty(), "Buffer should be empty after consuming the single item");
    }



    @Test
    @DisplayName("Validate Buffer State After Consume")
    public void validateBufferStateAfterConsume() {
        ItemManager item = createItemManager("item1");
        bufferManager.add(item);
        consumerMock.consumeItem();
        assertFalse(bufferManager.getBufferQueue().contains(item), "Buffer should not contain the consumed item");
    }

    @Test
    @DisplayName("Test that run method executes without error")
    public void testRunMethod() {
        assertDoesNotThrow(() -> consumerMock.run(), "The run method should execute without throwing exceptions");
    }

    @Test
    @DisplayName("Test that stopRunning method executes without error")
    public void testStopRunningMethod() {
        assertDoesNotThrow(() -> consumerMock.stopRunning(), "The stopRunning method should execute without throwing exceptions");
    }

}
