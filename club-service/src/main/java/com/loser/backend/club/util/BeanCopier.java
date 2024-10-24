package com.loser.backend.club.util;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ReflectUtil;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author ~~trading
 * @date 11:15 04/06/22
 */
public class BeanCopier {

    public static <SRC, T> T copy(SRC source, Supplier<? extends T> supplier) {
        T t1 = supplier.get();
        BeanUtil.copyProperties(source, t1);
        return t1;
    }

    public static <SRC, T> List<T> copy(List<SRC> sources, Supplier<? extends T> supplier) {
        return sources.stream().map(src -> copy(src, supplier)).collect(Collectors.toList());
    }

}
