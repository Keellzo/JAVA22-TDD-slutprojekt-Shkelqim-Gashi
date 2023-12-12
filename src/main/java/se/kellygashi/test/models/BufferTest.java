package se.kellygashi.test.models;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import se.kellygashi.test.consumer.ConsumerMock;
import se.kellygashi.test.helpers.BufferManager;
import se.kellygashi.main.models.Item;
import se.kellygashi.test.producer.ProducerMock;

import static org.junit.Assert.*;

import java.util.concurrent.*;

public class BufferTest {

    private BufferManager bufferManager;
    private ProducerMock producerMock;
    private ConsumerMock consumerMock;

    private Item createItem(String id) {
        return new Item(id);
    }

    @Before
    public void setUp() {
        bufferManager = new BufferManager();
        producerMock = new ProducerMock(bufferManager);
        consumerMock = new ConsumerMock(bufferManager);
    }

    @Test
    @DisplayName("Test Adding Item to Buffer")
    public void testAddingItemToBuffer() {
        Item item = createItem("item1");
        assertTrue(producerMock.addItem(item));
        assertEquals(item, consumerMock.consumeItem());
    }

    @Test
    @DisplayName("Test Removing Item from Buffer")
    public void testRemovingItemFromBuffer() {
        Item item = createItem("item1");
        producerMock.addItem(item);
        assertEquals(item, consumerMock.consumeItem());
    }

    @Test
    @DisplayName("Test Add Returns True")
    public void testAddReturnsTrue() {
        assertTrue(producerMock.addItem(createItem("item1")));
    }

    @Test
    @DisplayName("Test Remove Returns Correct Item")
    public void testRemoveReturnsCorrectItem() {
        Item firstItem = createItem("item1");
        Item secondItem = createItem("item2");
        producerMock.addItem(firstItem);
        producerMock.addItem(secondItem);
        assertEquals(firstItem, consumerMock.consumeItem());
    }

    @Test
    @DisplayName("Test Multiple Adds and Removes")
    public void testMultipleAddsAndRemoves() {
        Item firstItem = createItem("item1");
        Item secondItem = createItem("item2");
        producerMock.addItem(firstItem);
        producerMock.addItem(secondItem);
        assertEquals(firstItem, consumerMock.consumeItem());
        assertEquals(secondItem, consumerMock.consumeItem());
    }

    @Test
    @DisplayName("Test Remove After Add")
    public void testRemoveAfterAdd() {
        Item item = createItem("item1");
        producerMock.addItem(item);
        assertEquals(item, consumerMock.consumeItem());
    }

    @Test
    @DisplayName("Test Buffer Ordering")
    public void testBufferOrdering() {
        Item firstItem = createItem("item1");
        Item secondItem = createItem("item2");
        producerMock.addItem(firstItem);
        producerMock.addItem(secondItem);
        assertEquals(firstItem, consumerMock.consumeItem());
        assertEquals(secondItem, consumerMock.consumeItem());
    }

    @Test(timeout = 1000)
    @DisplayName("Test Remove Blocks When Buffer Is Empty")
    public void testRemoveBlocksWhenBufferIsEmpty() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Callable<Item> task = () -> consumerMock.consumeItem();

        Future<Item> future = executor.submit(task);
        try {
            future.get(500, TimeUnit.MILLISECONDS);
            fail("Expected TimeoutException");
        } catch (TimeoutException ex) {
            // Expected, means the remove() is blocking as it should
        } catch (InterruptedException | ExecutionException e) {
            fail("Test interrupted or execution exception: " + e.getMessage());
        } finally {
            executor.shutdownNow();
        }
    }
}
