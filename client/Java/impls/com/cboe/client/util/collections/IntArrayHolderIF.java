package com.cboe.client.util.collections;

/**
 * IntArrayHolderIF.java
 *
 * @author Dmitry Volpyansky
 *
 * FILE GENERATED BY VELOCITY TEMPLATE ENGINE FROM /vobs/dte/client/generator/DV_XArrayHolderIF.java (KEY_TYPE=int)
 *
 */

import com.cboe.client.util.*;

public interface IntArrayHolderIF extends HasSizeIF
{
    public int size();
    public boolean isEmpty();
    public int capacity();
    public int ensureCapacity(int capacity);
    public int[] keys();
    public int[] getKeys();
    public void add(int key);
    public void add(int[] keys);
    public void add(int[] keys, int offset, int length);
    public int getKey(int index);
    public boolean containsKey(int key);
    public IntArrayHolderIF clear();
    public IntVisitorIF acceptVisitor(IntVisitorIF visitor);
}

