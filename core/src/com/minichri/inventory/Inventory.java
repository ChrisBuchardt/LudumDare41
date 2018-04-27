package com.minichri.inventory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Inventory {

    public static final int SIZE = 5;

    private ArrayList<Item> items;
    private List<InventoryListener> listeners;

    public Inventory() {
        items = new ArrayList<>(SIZE);
        listeners = new LinkedList<>();
    }

    public Item get(int i) {
        return items.get(i);
    }

    /** @return true if the item was added */
    public boolean add(Item item) {
        if (isFull()) return false;

        items.add(item);
        updateListeners();
        return true;
    }

    public boolean remove(Item item) {
        boolean wasRemoved = items.remove(item);
        if (wasRemoved) updateListeners();
        return wasRemoved;
    }

    public boolean isFull() {
        return items.size() == SIZE;
    }

    public int slotsLeft() {
        return SIZE - items.size();
    }

    private void updateListeners() {
        for (InventoryListener listener : listeners) {
            listener.onChange(this);
        }
    }

    public void addListener(InventoryListener listener) {
        listeners.add(listener);
    }
}
