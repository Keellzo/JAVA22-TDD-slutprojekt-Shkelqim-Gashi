package se.kellygashi.test.consumer;

import org.junit.jupiter.api.*;
import se.kellygashi.main.models.Buffer;
import se.kellygashi.main.models.Item;

import java.util.NoSuchElementException;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

public class BufferTest {

    private Buffer buffer;
    private Item item;

    @BeforeEach
    void setUp() {
        buffer = new Buffer();
        item = new Item("TestItem");
    }

    @Test
    @DisplayName("Add an item to the buffer")
    void addItemToBuffer() throws InterruptedException {
        assertTrue(buffer.add(item));
        assertEquals(item, buffer.remove());
    }

    @Test
    @DisplayName("Remove an item from the buffer")
    void removeItemFromBuffer() throws InterruptedException {
        buffer.add(item);
        assertEquals(item, buffer.remove());
    }

    @Test
    @DisplayName("Attempt to remove from empty buffer")
    void removeFromEmptyBuffer() {
        assertThrows(NoSuchElementException.class, buffer::tryRemove);
    }


    @Test
    @DisplayName("Add null to buffer should throw NullPointerException")
    void addNullToBuffer() {
        assertThrows(NullPointerException.class, () -> buffer.add(null));
    }

    @Test
    @DisplayName("Test InterruptedException catch block")
    void testInterruptedExceptionCatchBlock() throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(2);
        Future<?> future = service.submit(this::testRun);

        Thread.sleep(100);
        future.cancel(true);

        service.shutdown();
        assertTrue(service.awaitTermination(1, TimeUnit.SECONDS), "Test didn't terminate in time");
    }

    @Test
    @DisplayName("Items are removed in the order they were added")
    void itemsRemovedInOrder() throws InterruptedException {
        Buffer buffer = new Buffer();
        Item item1 = new Item("Item1");
        Item item2 = new Item("Item2");
        Item item3 = new Item("Item3");

        buffer.add(item1);
        buffer.add(item2);
        buffer.add(item3);

        assertEquals(item1, buffer.remove(), "First item should be Item1");
        assertEquals(item2, buffer.remove(), "Second item should be Item2");
        assertEquals(item3, buffer.remove(), "Third item should be Item3");
    }



    @Test
    @DisplayName("Wait on empty buffer and then add an item")
    void testWaitOnEmptyBufferAndAdd() throws InterruptedException, ExecutionException {
        ExecutorService service = Executors.newSingleThreadExecutor();
        Future<Item> future = service.submit(() -> buffer.remove());

        Thread.sleep(500);

        buffer.add(item);


        Item removedItem = future.get();
        assertEquals(item, removedItem);

        service.shutdownNow();
    }

    private void testRun() {
        try {
            buffer.remove();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
