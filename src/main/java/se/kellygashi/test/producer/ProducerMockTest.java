package se.kellygashi.test.producer;

import org.junit.jupiter.api.*;
import se.kellygashi.test.helpers.BufferManager;
import se.kellygashi.test.helpers.ItemManager;

import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

public class ProducerMockTest {

    private BufferManager bufferManager;
    private ProducerMock producerMock;
    private ExecutorService executorService;

    @BeforeEach
    void setUp() {
        bufferManager = new BufferManager();
        producerMock = new ProducerMock(bufferManager);
        executorService = Executors.newSingleThreadExecutor();
    }

    @AfterEach
    void tearDown() {
        executorService.shutdownNow();
    }

    private ItemManager createItemManager(String id) {
        return new ItemManager(id);
    }

    @Test
    @DisplayName("Producer adds a specific item to the buffer")
    void testProducerAddsSpecificItem() {
        ItemManager itemManager = createItemManager("item1");
        producerMock.addItem(itemManager);
        assertTrue(bufferManager.getBufferQueue().contains(itemManager), "Buffer should contain the specific item added by producer");
    }



    @Test
    @DisplayName("Producer adds multiple items")
    void testProducerAddsMultipleItems() {
        producerMock.addItem(createItemManager("item1"));
        producerMock.addItem(createItemManager("item2"));
        assertEquals(2, bufferManager.getBufferQueue().size(), "Buffer should have two items added by producer");
    }

    @Test
    @DisplayName("Buffer state after producer adds and stops")
    void testBufferStateAfterProducerAddsAndStops() {
        producerMock.addItem(createItemManager("item1"));
        producerMock.stopRunning();
    }

    @Test
    @DisplayName("Test that run method executes without error")
    public void testRunMethod() {
        assertDoesNotThrow(() -> producerMock.run(), "The run method should execute without throwing exceptions");
    }

}
