package org.lemonframework.common;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * 对象map,线程不安全.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
public class ObjectMap implements Map<Object, Object>, Serializable {

    private static final long serialVersionUID = 1L;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    private final Map<Object, Object> map;

    public ObjectMap() {
        this(DEFAULT_INITIAL_CAPACITY, false);
    }

    public ObjectMap(Map<Object, Object> map) {
        if (map == null) {
            throw new IllegalArgumentException("map is null.");
        }
        this.map = map;
    }

    public ObjectMap(boolean ordered) {
        this(DEFAULT_INITIAL_CAPACITY, ordered);
    }

    public ObjectMap(int initialCapacity) {
        this(initialCapacity, false);
    }

    public ObjectMap(int initialCapacity, boolean ordered) {
        if (ordered) {
            map = new LinkedHashMap<Object, Object>(initialCapacity);
        } else {
            map = new HashMap<Object, Object>(initialCapacity);
        }
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    public Object get(Object key) {
        return map.get(key);
    }

    @Override
    public Object put(Object key, Object value) {
        return map.put(key, value);
    }

    @Override
    public Object remove(Object key) {
        return map.remove(key);
    }

    @Override
    public void putAll(Map<? extends Object, ? extends Object> m) {
        map.putAll(m);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Set<Object> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<Object> values() {
        return map.values();
    }

    @Override
    public Set<Entry<Object, Object>> entrySet() {
        return map.entrySet();
    }

    @Override
    public boolean equals(Object obj) {
        return this.map.equals(obj);
    }

    @Override
    public int hashCode() {
        return this.map.hashCode();
    }

    public Map<Object, Object> getInnerMap() {
        return this.map;
    }

    public ObjectMap fluentPut(Object key, Object value) {
        map.put(key, value);
        return this;
    }

    public ObjectMap fluentPutAll(Map<? extends Object, ? extends Object> m) {
        map.putAll(m);
        return this;
    }

    public ObjectMap fluentClear() {
        map.clear();
        return this;
    }

    public ObjectMap fluentRemove(Object key) {
        map.remove(key);
        return this;
    }
}
