package se.kellygashi.main;

/**
 * Needs buffer to put se.kellygashi.main.Item in.
 * run starts se.kellygashi.producerconsumer.Producer
 * stopRunning stops se.kellygashi.producerconsumer.Producer
 */
public interface Producer {

    public void run();
    public void stopRunning();
}
