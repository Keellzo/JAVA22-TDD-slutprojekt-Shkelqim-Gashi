package se.kellygashi.main.models;

import java.util.LinkedList;
import java.util.Queue;

public class Buffer {

    protected Queue<Item> buffer = new LinkedList<Item>();

    public synchronized boolean add(Item item) {
        buffer.add(item);
        System.out.println(buffer);
        notify();
        return true;
    }

    public synchronized Item remove() {
        if (buffer.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Item item = buffer.remove();
        return item;
    }
}