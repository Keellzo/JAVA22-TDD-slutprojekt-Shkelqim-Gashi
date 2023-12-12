package se.kellygashi.test.producer;

import se.kellygashi.main.models.Buffer;
import se.kellygashi.main.models.Item;
import se.kellygashi.main.interfaces.Producer;
import se.kellygashi.test.helpers.BufferManager;

public class ProducerStub implements Producer {

    private final BufferManager bufferMgr;

    public ProducerStub(BufferManager bufferMgr) {
        this.bufferMgr = bufferMgr;
    }

    public boolean addItem(Item item) {
        return bufferMgr.add(item);
    }

    @Override
    public void run() {

    }

    @Override
    public void stopRunning() {

    }
}
