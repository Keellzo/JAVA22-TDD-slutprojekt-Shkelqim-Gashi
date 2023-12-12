package se.kellygashi.test.helpers;

import se.kellygashi.main.models.Buffer;
import se.kellygashi.main.models.Item;

import java.util.Queue;

public class BufferManager extends Buffer {

    public Queue<Item> getBufferQueue() {
        return super.buffer;
    }
}