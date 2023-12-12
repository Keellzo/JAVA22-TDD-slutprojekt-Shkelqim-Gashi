package se.kellygashi.test.consumer;

import se.kellygashi.main.interfaces.Consumer;
import se.kellygashi.main.models.Item;
import se.kellygashi.test.helpers.BufferManager;


public class ConsumerStub implements Consumer {

    private final BufferManager bufferMgr;

    public ConsumerStub(BufferManager bufferMgr) {
        this.bufferMgr = bufferMgr;
    }

    public Item consumeItem() {
        return bufferMgr.remove();
    }

    @Override
    public void run() {
    }

    @Override
    public void stopRunning() {
    }
}

