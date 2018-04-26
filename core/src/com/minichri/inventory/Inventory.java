package com.minichri.inventory;

import java.util.ArrayList;

public class Inventory {

    public static final int SIZE = 5;

    ArrayList<Item> items;

    public Inventory() {
        items = new ArrayList<>(SIZE);
    }

    public Item get(int i) {
        return items.get(i);
    }

    /** @return true if the item was added */
    public boolean add(Item item) {
        if (isFull()) return false;

        items.add(item);
        return true;
    }

    public boolean remove(Item item) {
        return items.remove(item);
    }

    public boolean isFull() {
        return items.size() == SIZE;
    }

    public int slotsLeft() {
        return SIZE - items.size();
    }
}
