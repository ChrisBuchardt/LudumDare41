package com.minichri.inventory;

@FunctionalInterface
public interface InventoryListener {
    void onChange(Inventory inventory);
}
