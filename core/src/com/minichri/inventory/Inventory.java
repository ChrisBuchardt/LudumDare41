package com.minichri.inventory;

import java.util.LinkedList;
import java.util.List;

public class Inventory {

    public static final int SIZE = 5;

    private Item[] items;
    private int itemCount = 0;
    private int nextEmpty = 0;
    private int selectedSlot = 0;
    private int gatheredResources = 0;
    public int totalResources =0;
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

    /** This method will make the inventory select the next filled slot. Does nothing if inventory is empty.
     * Returns true if anything was focused. */
    public boolean focus() {
        if (itemCount == 0) return false;
        for (int i = selectedSlot; i < SIZE; i++) {
            if (get(i) != null) {
                setSelectedSlot(i);
                return true;
            }
        }
        for (int i = selectedSlot; i >= 0; i--) {
            if (get(i) != null) {
                setSelectedSlot(i);
                return true;
            }
        }
        return false;
    }

    /** @return true if successful. */
    public boolean remove(int i) {
        boolean canRemoved = items[i] != null;
        if (canRemoved) {
            items[i] = null;
            itemCount--;
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
        selectedSlot = Math.min(Math.max(0, i), SIZE - 1);
        updateListeners();
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

    public int getRemainingResources(){
        return totalResources-gatheredResources;
    }
    public  void addResource(){
        gatheredResources++;
    }
    public void setTotalResources(int i){
        totalResources = i;
    }

}
