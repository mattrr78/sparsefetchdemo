package org.mattrr78.sparsefetchdemo;

import java.util.LinkedHashMap;
import java.util.Map;

public class SparseMap extends LinkedHashMap<String, Object> {
    SparseMap(Map<String, Object> map)  {
        super(map);
    }

    @Override
    public Object get(Object key) {
        if (!containsKey(key))  {
            throw new IllegalArgumentException("Field '" + key + "' was not sparse fetched.");
        }
        return super.get(key);
    }
}
