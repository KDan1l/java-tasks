package ru.mail.polis.homework.collections;


import java.util.*;
import java.util.function.Consumer;


/**
 * Написать структуру данных, реализующую интерфейс мапы + набор дополнительных методов.
 * 2 дополнительных метода должны вовзращать самый популярный ключ и его популярность.
 * Популярность - это количество раз, который этот ключ учавствовал в других методах мапы, такие как
 * containsKey, get, put, remove (в качестве параметра и возвращаемого значения).
 * Считаем, что null я вам не передю ни в качестве ключа, ни в качестве значения
 * <p>
 * Важный момент, вам не надо реализовывать мапу, вы должны использовать композицию.
 * Вы можете использовать любые коллекции, которые есть в java.
 * <p>
 * Помните, что по мапе тоже можно итерироваться
 * <p>
 * for (Map.Entry<K, V> entry : map.entrySet()) {
 * entry.getKey();
 * entry.getValue();
 * }
 * <p>
 * <p>
 * Дополнительное задание описано будет ниже
 *
 * @param <K> - тип ключа
 * @param <V> - тип значения
 */
public class PopularMap<K, V> implements Map<K, V> {

    private final Map<K, V> map;
    private final Map<K, Integer> keyPopularity;
    private final Map<V, Integer> valuePopularity;

    public PopularMap() {
        this(new HashMap<>());
    }

    public PopularMap(Map<K, V> map) {
        this.map = map;
        this.keyPopularity = new HashMap<>();
        this.valuePopularity = new HashMap<>();
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
        incrementPopularity((K) key, keyPopularity);
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        incrementPopularity((V) value, valuePopularity);
        return map.containsValue(value);
    }

    @Override
    public V get(Object key) {
        V value = map.get(key);
        incrementPopularity((K) key, keyPopularity);
        incrementPopularity(value, valuePopularity);
        return value;
    }

    @Override
    public V put(K key, V value) {
        incrementPopularity(key, keyPopularity);
        incrementPopularity(value, valuePopularity);
        V exValue = map.put(key, value);
        incrementPopularity(exValue, valuePopularity);
        return exValue;
    }

    @Override
    public V remove(Object key) {
        V value = map.remove(key);
        incrementPopularity((K) key, keyPopularity);
        incrementPopularity(value, valuePopularity);
        return value;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        map.putAll(m);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Set<K> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<V> values() {
        return map.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return map.entrySet();
    }

    /**
     * Возвращает самый популярный, на данный момент, ключ
     */
    public K getPopularKey() {
        return getPopularElement(keyPopularity);
    }


    /**
     * Возвращает количество использование ключа
     */
    public int getKeyPopularity(K key) {
        return keyPopularity.getOrDefault(key, 0);
    }

    /**
     * Возвращает самое популярное, на данный момент, значение. Надо учесть что значений может быть более одного
     */
    public V getPopularValue() {
        return getPopularElement(valuePopularity);
    }

    /**
     * Возвращает количество использований значений в методах: containsValue, get, put (учитывается 2 раза, если
     * старое значение и новое - одно и тоже), remove (считаем по старому значению).
     */
    public int getValuePopularity(V value) {
        return valuePopularity.getOrDefault(value, 0);
    }

    private <T> T getPopularElement(Map<T, Integer> popularityMap) {
        T popular = null;
        int max = 0;

        for (Entry<T, Integer> elem : popularityMap.entrySet()) {
            if (elem.getValue() > max) {
                max = elem.getValue();
                popular = elem.getKey();
            }
        }
        return popular;
    }

    /**
     * Вернуть итератор, который итерируется по значениям (от самых НЕ популярных, к самым популярным)
     */
    public Iterator<V> popularIterator() {
        List<V> list = new ArrayList<>(this.valuePopularity.keySet());
        list.sort(Comparator.comparing(valuePopularity::get));
        return list.iterator();
    }

    private <T> void incrementPopularity(T key, Map<T, Integer> popularMap) {
        if (key != null) {
            popularMap.put(key, popularMap.getOrDefault(key, 0) + 1);
        }
    }
}
