package se.kellygashi.main;

import se.kellygashi.main.Item;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

public class Buffer {

    protected Queue<Item> buffer = new LinkedList<>();

    public synchronized boolean add(Item item) {
        if (item == null) {
            throw new NullPointerException("Cannot add null item to the buffer.");
        }
        boolean isAdded = buffer.add(item);
        notifyAll();
        return isAdded;
    }

    public synchronized Item remove() throws InterruptedException {
        while (buffer.isEmpty()) {
            wait();
        }
        return buffer.remove();
    }


    public synchronized Item tryRemove() {
        if (buffer.isEmpty()) {
            throw new NoSuchElementException("Buffer is empty.");
        }
        return buffer.remove();
    }
}
