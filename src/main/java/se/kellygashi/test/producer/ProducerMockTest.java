package se.kellygashi.test.producer;

import org.junit.jupiter.api.*;
import se.kellygashi.main.models.Buffer;
import se.kellygashi.main.models.Item;
import se.kellygashi.test.ProducerStub;

import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProducerMockTest {

    private Buffer mockBuffer;
    private ProducerStub producerStub;
    private ExecutorService executorService;

    @BeforeEach
    void setUp() {
        mockBuffer = mock(Buffer.class);
        producerStub = new ProducerStub(mockBuffer);
        executorService = Executors.newSingleThreadExecutor();
    }

    @AfterEach
    void tearDown() {
        executorService.shutdownNow();
    }

    @Test
    @DisplayName("Producer adds an item to the buffer")
    void testProducerAddsItem() throws InterruptedException, ExecutionException {
        CountDownLatch latch = new CountDownLatch(1);
        doAnswer(invocation -> {
            latch.countDown();
            return null;
        }).when(mockBuffer).add(any(Item.class));

        Future<?> future = executorService.submit(producerStub::run);
        assertTrue(latch.await(1, TimeUnit.SECONDS), "Latch was not counted down");
        producerStub.stopRunning();
        future.get();
        verify(mockBuffer, atLeastOnce()).add(any(Item.class));
    }


    @Test
    @DisplayName("Producer stops gracefully while waiting")
    void testProducerStopsWhileWaitingImproved() throws InterruptedException, ExecutionException {
        CountDownLatch latch = new CountDownLatch(1);
        doAnswer(invocation -> {
            latch.countDown();
            Thread.sleep(200);
            return null;
        }).when(mockBuffer).add(any());

        Future<?> future = executorService.submit(() -> producerStub.run());
        assertTrue(latch.await(500, TimeUnit.MILLISECONDS));
        producerStub.stopRunning();
        future.get();
        assertTrue(future.isDone(), "Producer should stop gracefully while waiting");
        verify(mockBuffer, times(1)).add(any(Item.class));
    }


    @Test
    @DisplayName("Producer does not add item after being stopped")
    void testProducerDoesNotAddAfterStop() throws InterruptedException, ExecutionException {
        CountDownLatch addItemLatch = new CountDownLatch(1);
        CountDownLatch stopLatch = new CountDownLatch(1);

        doAnswer(invocation -> {
            addItemLatch.countDown();
            stopLatch.await();
            return true;
        }).when(mockBuffer).add(any(Item.class));

        Future<?> future = executorService.submit(() -> producerStub.run());
        assertTrue(addItemLatch.await(1, TimeUnit.SECONDS), "Producer didn't add an item in time");
        producerStub.stopRunning();
        stopLatch.countDown();
        future.get();
        verify(mockBuffer, times(1)).add(any(Item.class));
    }


    @Test
    @DisplayName("No additional items are added after stop")
    void testNoAdditionAfterStop() throws InterruptedException {
        CountDownLatch addItemLatch = new CountDownLatch(1);
        doAnswer(invocation -> {
            addItemLatch.countDown();
            return null;
        }).when(mockBuffer).add(any(Item.class));

        executorService.submit(producerStub::run);
        assertTrue(addItemLatch.await(1, TimeUnit.SECONDS), "Item was not added in time");
        producerStub.stopRunning();
        verify(mockBuffer, times(1)).add(any(Item.class));
    }
}
