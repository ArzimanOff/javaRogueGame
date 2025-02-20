package org.arzimanoff.model.items;

import java.util.ArrayList;
import java.util.List;

public class Satchel {
    private final List<Item> inventory;
    private static final int MAX_ITEMS = 9;

    public Satchel() {
        inventory = new ArrayList<>();
    }

    public List<Item> getItems() {
        return inventory;
    }

    public boolean addItem(Item item) {
        boolean result = false;
        if (inventory.size() < MAX_ITEMS) {
            inventory.add(item);
            result = true;
        }
        return result;
    }

    public void removeItem(Item item) {
        inventory.remove(item);
    }

    @Override
    public String toString() {
        return "Satchel{" +
               "inventory=" + inventory +
               '}';
    }
}
