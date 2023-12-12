package se.kellygashi.test.helpers;

import se.kellygashi.main.models.Item;

public class ItemManager extends Item {

    public ItemManager(String id) {
        super(id);
    }


    public String getItemId() {
        return super.id;
    }

    public void updateItemId(String newId) {
        setId(newId);
    }
}
