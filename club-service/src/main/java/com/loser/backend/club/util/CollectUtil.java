package com.loser.backend.club.util;


import java.util.Collection;
import java.util.Optional;

/**
 * @author ~~trading
 * @date 16:27 2022/03/18
 * @desc
 */
public class CollectUtil {

    public static <E> Collection<E> addIfPresent(Collection<E> collection, E target) {
        Optional.ofNullable(target).ifPresent(collection::add);
        return collection;
    }

}
