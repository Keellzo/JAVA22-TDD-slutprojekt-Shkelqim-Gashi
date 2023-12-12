package se.kellygashi.main;

public class Item {

    protected String id = "";

    public Item(String id) {
        this.id = id.toLowerCase();
    }

    @Override
    public String toString() {
        return id.toLowerCase();
    }

    public void setId(String id) {
        this.id = id.toLowerCase();
    }

    public String getId() {
        return id;
    }
}
