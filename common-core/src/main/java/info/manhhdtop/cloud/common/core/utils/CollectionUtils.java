package info.manhhdtop.cloud.common.core.utils;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("unused")
public class CollectionUtils {
    private CollectionUtils() {
    }

    public static boolean isEmpty(Object... array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isEmpty(Stream<?> stream) {
        return stream == null;
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    public static <K, E> Map<K, E> toMap(Collection<E> collection, Function<E, K> getKeyFunc) {
        if (isEmpty(collection)) {
            return new HashMap<>();
        }
        return toMap(collection.stream(), getKeyFunc);
    }

    public static <E, K, V> Map<K, V> toMap(Collection<E> collection, Function<E, K> getKeyFunction, Function<E, V> getValueFunction) {
        if (isEmpty(collection)) {
            return new HashMap<>();
        }
        return toMap(collection.stream(), getKeyFunction, getValueFunction);
    }

    public static <E, K, V> Map<K, V> toMap(Collection<E> collection, Function<E, K> getKeyFunction, Function<E, V> getValueFunction, Supplier<Map<K, V>> mapSupplier) {
        if (isEmpty(collection)) {
            return mapSupplier.get();
        }
        return toMap(collection.stream(), getKeyFunction, getValueFunction, mapSupplier);
    }

    public static <K, E> Map<K, E> toMap(Stream<E> stream, Function<E, K> getKeyFunction) {
        if (isEmpty(stream)) {
            return new HashMap<>();
        }
        Map<K, E> result = new HashMap<>();
        stream.forEach(element -> result.put(getKeyFunction.apply(element), element));
        return result;
    }

    public static <E, K, V> Map<K, V> toMap(Stream<E> stream, Function<E, K> getKeyFunction, Function<E, V> getValueFunction) {
        return toMap(stream, getKeyFunction, getValueFunction, HashMap::new);
    }

    public static <E, K, V> Map<K, V> toMap(Stream<E> stream, Function<E, K> getKeyFunction, Function<E, V> getValueFunction, Supplier<Map<K, V>> mapSupplier) {
        if (isEmpty(stream)) {
            return mapSupplier.get();
        }
        Map<K, V> result = mapSupplier.get();
        stream.forEach(element -> result.put(getKeyFunction.apply(element), getValueFunction.apply(element)));
        mapSupplier.get();
        return result;
    }

    public static boolean equals(List<String> list1, List<String> list2) {
        if (list1 == null && list2 == null) {
            return true;
        }
        if (list1 == null || list2 == null) {
            return false;
        }
        return list1.equals(list2);
    }

    public static <U extends Collection<?>> U getOrDefault(U collection, U defaultCollection) {
        if (Objects.isNull(collection)) {
            return defaultCollection;
        }
        return collection;
    }

    public static <T> List<List<T>> splitList(List<T> originalList, int maxSize) {
        List<List<T>> result = new ArrayList<>();
        for (int i = 0; i < originalList.size(); i += maxSize) {
            result.add(originalList.subList(i, Math.min(i + maxSize, originalList.size())));
        }
        return result;
    }

    public static <E, K> Map<K, List<E>> group(Collection<E> collection, Function<E, K> getKeyFunction) {
        return group(collection.stream(), getKeyFunction);
    }

    public static <E, K> Map<K, List<E>> group(Stream<E> stream, Function<E, K> getKeyFunction) {
        return stream.collect(Collectors.groupingBy(getKeyFunction));
    }

    public static <E, K, V> Map<K, List<V>> group(Collection<E> collection, Function<E, K> getKeyFunction, Function<E, V> getValueFunction) {
        return group(collection.stream(), getKeyFunction, getValueFunction);
    }

    public static <E, K, V> Map<K, List<V>> group(Stream<E> stream, Function<E, K> getKeyFunction, Function<E, V> getValueFunction) {
        return stream.collect(Collectors.groupingBy(getKeyFunction, Collectors.mapping(getValueFunction, Collectors.toList())));
    }

    public static int length(Collection<?> collection) {
        if (isEmpty(collection)) {
            return 0;
        }
        return collection.size();
    }

    public static <T> boolean contains(Collection<T> collection, T obj) {
        if (isEmpty(collection)) {
            return false;
        }
        return collection.contains(obj);
    }

    public static <T> T getFirst(List<T> collection) {
        if (isEmpty(collection)) {
            return null;
        }
        return collection.getFirst();
    }

    public static <T> T getFirst(Collection<T> collection) {
        if (isEmpty(collection)) {
            return null;
        }
        if (collection instanceof List<T> list) {
            return getFirst(list);
        }
        return collection.iterator().next();
    }

    public static <T> T get(List<T> collection, int index) {
        if (isEmpty(collection)) {
            return null;
        }
        return collection.size() > index ? collection.get(index) : null;
    }

    public static <T> T get(Collection<T> collection, int index) {
        if (isEmpty(collection)) {
            return null;
        }
        if (collection instanceof List<T> list) {
            return get(list, index);
        }
        return collection.stream().skip(index).findFirst().orElse(null);
    }

    public static <T> T getLast(List<T> collection) {
        if (isEmpty(collection)) {
            return null;
        }
        return collection.getLast();
    }

    public static <T> T getLast(Collection<T> collection) {
        if (isEmpty(collection)) {
            return null;
        }
        if (collection instanceof List<T> list) {
            return getLast(list);
        }
        return collection.stream().reduce((first, second) -> second).orElse(null);
    }
}
