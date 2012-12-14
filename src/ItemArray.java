import java.util.Arrays;

/**
 * ItemArray.java - Interface to jh[] so I don't have to copy+paste this a bunch
 * of times
 * 
 * @param <C> The container of the items
 * @author James
 */
public abstract class ItemArray<C extends Container<OItemStack>> {
    protected C container;

    public ItemArray(C container) {
        this.container = container;
    }

    public int getContentsSize() {
        return container.getContentsSize();
    }

    /**
     * Adds the specified item. If the item doesn't have a slot, it will get the
     * nearest available slot. If amount is equal to 0, it will delete the item
     * if a slot is specified.
     * 
     * @param item
     *            item to add
     */
    public void addItem(Item item) {
        if (item == null) {
            return;
        }

        int slot = item.getSlot();
        int size = getContentsSize();

        if (slot < size && slot >= 0) {
            if (item.getAmount() <= 0) {
                removeItem(slot);
            } else if (Item.isValidItem(item.getItemId())) {
                setSlot(item, slot);
            }
        } else if (slot == -1) {
            int newSlot = getEmptySlot();

            if (newSlot != -1) {
                setSlot(item, newSlot);
                item.setSlot(newSlot);
            }
        }
    }

    /**
     * Retrieves from the slot
     * 
     * @param slot
     *            slot to get item from
     * @return item
     */
    public Item getItemFromSlot(int slot) {
        int size = getContentsSize();

        if (slot < size && slot >= 0) {
            OItemStack result = container.getContentsAt(slot);

            if (result != null) {
                return new Item(result, slot);
            }
        }

        return null;
    }

    /**
     * Retrieves from the slot
     * 
     * @param type
     * @return item
     */
    public Item getItemFromId(Item.Type type) {
        return getItemFromId(type.getId());
    }

    /**
     * Retrieves from the slot
     * 
     * @param id
     * @return item
     */
    public Item getItemFromId(int id) {
        Item[] items = getContents();

        for (Item item : items) {
            if ((item != null) && (item.getItemId() == id)) {
                return item;
            }
        }

        return null;
    }

    /**
     * Retrieves from the slot
     * 
     * @param type
     * @param maxAmount
     * @return item
     */
    public Item getItemFromId(Item.Type type, int maxAmount) {
        return getItemFromId(type.getId());
    }

    /**
     * Retrieves from the slot
     * 
     * @param id
     * @param maxAmount
     * @return item
     */
    public Item getItemFromId(int id, int maxAmount) {
        Item[] items = getContents();

        for (Item item : items) {
            if ((item != null) && (item.getItemId() == id) && (item.getAmount() <= maxAmount)) {
                return item;
            }
        }

        return null;
    }

    /**
     * Gets the nearest empty slot. -1 if there's no empty slots
     * 
     * @return nearest empty slot
     */
    public int getEmptySlot() {
        int size = getContentsSize();

        for (int i = 0; size > i; i++) {
            if (container.getContentsAt(i) != null) {
                continue;
            }
            return i;
        }

        return -1;
    }

    /**
     * Removes the item from the slot
     * 
     * @param slot
     *            slot to remove item from
     */
    public void removeItem(int slot) {
        int size = getContentsSize();

        if (slot < size && slot >= 0) {
            container.setContentsAt(slot, null);
        }
    }

    /**
     * Sets the specified slot with item
     * 
     * @param item
     *            item to set
     * @param slot
     *            slot to use
     */
    public void setSlot(Item item, int slot) {
        int size = getContentsSize();

        if (slot < size && slot >= 0) {
            container.setContentsAt(slot, item == null ? null : item.getBaseItem());
        }
    }

    /**
     * Replaces the slot with the specified item.
     * 
     * @param type
     *            type of the item to put into the slot.
     * @param amount
     *            amount of the item to put into the slot.
     * @param slot
     *            the id of the slot.
     */
    public void setSlot(Item.Type type, int amount, int slot) {
        setSlot(type.getId(), amount, slot);
    }

    /**
     * Replaces the slot with the specified item.
     * 
     * @param itemId
     *            item id of the item to put into the slot.
     * @param amount
     *            amount of the item to put into the slot.
     * @param slot
     *            the id of the slot.
     */
    public void setSlot(int itemId, int amount, int slot) {
        setSlot(itemId, amount, 0, slot);
    }

    /**
     * Replaces the slot with the specified item.
     * 
     * @param itemId
     *            item id of the item to put into the slot.
     * @param amount
     *            amount of the item to put into the slot.
     * @param damage
     *            remaining damage of the item to put into the slot.
     * @param slot
     *            the id of the slot.
     */
    public void setSlot(int itemId, int amount, int damage, int slot) {
        int size = getContentsSize();

        if (slot < size && slot >= 0) {
            container.setContentsAt(slot, new OItemStack(itemId, (amount > 64 ? (amount == 255 ? -1 : 64) : amount), damage));
        }
    }

    /**
     * Removes the item. No slot needed, it will go through the inventory until
     * the exact item specified is removed.
     * 
     * @param item
     *            item to remove
     */
    public void removeItem(Item item) {
        Item[] items = getContents();

        for (Item i : items) {
            if(i != null && i.equalsIgnoreSlot(item)) {
                removeItem(i.getSlot());
                break;
            }
        }
    }

    /**
     * Removes the item. No slot needed, it will go through the inventory until
     * the amount specified is removed.
     * 
     * @param type
     *            item to remove
     * @param amount
     *            amount to remove
     */
    public void removeItem(Item.Type type, int amount) {
        removeItem(type.getId(), amount);
    }

    /**
     * Removes the item. No slot needed, it will go through the inventory until
     * the amount specified is removed.
     * 
     * @param id
     *            item to remove
     * @param amount
     *            amount to remove
     */
    public void removeItem(int id, int amount) {
        Item[] items = getContents();
        int remaining = amount;

        for (Item item : items) {
            if ((item != null) && (item.getItemId() == id)) {
                if (item.getAmount() == remaining) {
                    removeItem(item.getSlot());
                    return;
                } else if (item.getAmount() > remaining) {
                    setSlot(id, item.getAmount() - remaining, item.getSlot());
                    return;
                } else {
                    removeItem(item.getSlot());
                    remaining -= item.getAmount();
                }
            }
        }
    }
    
    /**
     * Removes items from the inventory that match the given item until the amount in the given item is removed.
     * 
     * @param item The item type to remove
     */
    public void removeItemOverStacks(Item item) {
        Item[] items = getContents();
        int remaining = item.getAmount();
        
        for(Item i : items) {
            if(item.equalsIgnoreSlotAndAmount(i)) {
                if(i.getAmount() == remaining) {
                    removeItem(i.getSlot());
                    return;
                } else if(i.getAmount() > remaining) {
                    setSlot(i.getItemId(), i.getAmount() - remaining, i.getSlot());
                    return;
                } else {
                    removeItem(i.getSlot());
                    remaining -= i.getAmount();
                }
            }
        }
    }
    
    /**
     * Checks to see if this getArray() has an item identical to the one specified.
     * 
     * @param item
     * @return
     */
    public boolean hasItem(Item item) {
        Item[] items = getContents();
        
        for(Item i : items) {
            if(i != null && i.equalsIgnoreSlot(item)) {
                return true;
            }
        }
        
        return false;
    }

    /**
     * Checks to see if this getArray() has one slot that has the given item
     * type
     * 
     * @param type
     * @return
     */
    public boolean hasItem(Item.Type type) {
        return hasItem(type, 1);
    }

    /**
     * Checks to see if this getArray() has one slot that has the given item id
     * 
     * @param itemId
     * @return
     */
    public boolean hasItem(int itemId) {
        return hasItem(itemId, 1);
    }

    /**
     * Checks to see if this getArray() has one slot that has the item id and
     * equal or more to the amount.
     * 
     * @param type
     *            item to look for
     * @param minimum
     *            amount of items that must be in the stack
     * @return
     */
    public boolean hasItem(Item.Type type, int minimum) {
        Item[] items = getContents();

        for (Item item : items) {
            if ((item != null) && (item.getType() == type) && (item.getAmount() >= minimum)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks to see if this getArray() has one slot that has the item id and
     * equal or more to the amount.
     * 
     * @param itemId
     *            item to look for
     * @param minimum
     *            amount of items that must be in the stack
     * @return
     */
    public boolean hasItem(int itemId, int minimum) {
        Item[] items = getContents();

        for (Item item : items) {
            if ((item != null) && (item.getItemId() == itemId) && (item.getAmount() >= minimum)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks to see if this getArray() has one slot that has the item id and
     * equal or more to the amount.
     * 
     * @param itemId
     * @param minimum
     * @param maximum
     * @return
     */
    public boolean hasItem(int itemId, int minimum, int maximum) {
        Item[] items = getContents();

        for (Item item : items) {
            if ((item != null) && (item.getItemId() == itemId) && (item.getAmount() >= minimum) && (item.getAmount() <= maximum)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns the contents of this chest
     * 
     * @return contents
     */
    public Item[] getContents() {
        int arraySize = getContentsSize();
        Item[] rt = new Item[arraySize];

        for (int i = 0; i < arraySize; i++) {
            rt[i] = getItemFromSlot(i);
        }

        return rt;
    }

    /**
     * Sets the contents
     * 
     * @param contents
     *            contents to set
     */
    public void setContents(Item[] contents) {
        int arraySize = getContentsSize();

        for (int i = 0; i < arraySize; i++) {
            if (contents[i] == null) {
                removeItem(i);
            } else {
                setSlot(contents[i], i);
            }
        }
    }

    public void clearContents() {
        int size = getContentsSize();

        for (int i = 0; size > i; i++) {
            container.setContentsAt(i, null);
        }
    }
    
    /**
     * Adds the item to the set, appending to stacks
     * or with no or full stack, adds a new stack.
     * Stack sizes correspond with the max of the item
     *
     * @param item
     * @return true if all items are in the inventory,
     *          false when items are left over. item is updated to the leftover-amount.
     */
    public boolean insertItem(Item item) {
        int amount = item.getAmount();
        Item itemExisting;
        int maxAmount = item.getMaxAmount();
        
        while (amount > 0) {
            // Get an existing item with at least 1 spot free
            itemExisting = this.getItemFromId(item.getItemId(), maxAmount-1);
            
            // Add the items to the existing stack of items
            if (itemExisting != null) {
                // Add as much items as possible to the stack
                int k = Math.min(maxAmount - itemExisting.getAmount(), item.getAmount());
                this.setSlot(item.getItemId(), itemExisting.getAmount() + k, itemExisting.getSlot());
                amount -= k;
                continue;
            }
            // We still have slots, but no stack, create a new stack.
            if(this.getEmptySlot() != -1) {
                this.addItem(new Item(item.getItemId(), amount));
                amount = 0;
                continue;
            }
            
            // No free slots, no incomplete stacks: full
            // make sure the stored items are removed
            item.setAmount(amount);
            return false;
        }
        
        return true;
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ItemArray) {
            return Arrays.equals(getContents(), ((ItemArray) obj).getContents());
        }
        return false;
    }
    
    @Override
    public String toString() {
        return String.format("ItemArray[size=%d, contents=%s]", getContentsSize(), Arrays.toString(getContents()));
    }
}
