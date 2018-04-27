package com.minichri.inventory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Inventory {

    public static final int SIZE = 5;

    private Item[] items;
    private int itemCount = 0;
    private int nextEmpty = 0;
    private int selectedSlot = 0;
    private List<InventoryListener> listeners;

    public Inventory() {
        items = new Item[SIZE];
        listeners = new LinkedList<>();
    }

    public Item get(int i) {
        return items[i];
    }

    /** @return true if the item was added */
    public boolean add(Item item) {
        if (isFull()) return false;

        items[nextEmpty] = item;
        itemCount++;
        for (int i = nextEmpty; i < SIZE; i++) {
            if (items[i] == null) {
                nextEmpty = i;
                break;
            }
        }

        updateListeners();
        return true;
    }

    /** @return true if successful. */
    public boolean remove(int i) {
        boolean canRemoved = items[i] != null;
        if (canRemoved) {
            items[i] = null;
            if (i < nextEmpty) {
                nextEmpty = i;
            }
        }
        updateListeners();
        return canRemoved;
    }

    public boolean isFull() {
        return itemCount == SIZE;
    }

    /** @return empty slot count. */
    public int slotsLeft() {
        return SIZE - itemCount;
    }

    public void setSelectedSlot(int i) {
        selectedSlot = Math.min(Math.max(0, i), SIZE);
    }

    /** Move selected slot. slots = 1 is one to the right. Negative for left. */
    public void moveSelectedBy(int slots) {
        setSelectedSlot(selectedSlot + slots);
    }

    /** @return index of selected slot. */
    public int getSelectedSlot() {
        return selectedSlot;
    }

    public int getItemCount() {
        return itemCount;
    }

    /** @return item in selected slot. Can be null. */
    public Item getSelectedItem() {
        return items[selectedSlot];
    }

    private void updateListeners() {
        for (InventoryListener listener : listeners) {
            listener.onChange(this);
        }
    }

    /** Listeners are called with anything changes in the inventory. */
    public void addListener(InventoryListener listener) {
        listeners.add(listener);
    }
}
