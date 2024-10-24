package com.loser.backend.club.common.http;


import cn.hutool.core.collection.CollectionUtil;
import com.github.pagehelper.Page;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;


@Getter
@Setter
public class PageResult<T> {

    private Integer page;

    private Integer page_size;

    private Integer total_pages;

    private Long count;

    private List<T> items;

    private static <T> PageResult<T> defaultPage() {
        PageResult<T> result = new PageResult<>();
        result.setPage(1);
        result.setPage_size(10);
        return result;
    }

    public static <T> PageResult<T> empty() {
        PageResult<T> result = defaultPage();
        result.setTotal_pages(0);
        result.setCount(0L);
        result.setItems(new ArrayList<>());
        return result;
    }

    public static <T> PageResult<T> ofPage(Page<T> page) {
        PageResult<T> pageResult = new PageResult<>();
        pageResult.setTotal_pages(page.getPages());
        pageResult.setPage(page.getPageNum());
        pageResult.setPage_size(page.getPageSize());
        pageResult.setCount(page.getTotal());
        pageResult.setItems(page.getResult());
        return pageResult;
    }

    public static <T,E> PageResult<T> copyPageNoItems(PageResult<E> page) {
        PageResult<T> pageResult = new PageResult<>();
        pageResult.setTotal_pages(page.getTotal_pages());
        pageResult.setPage(page.getPage());
        pageResult.setPage_size(page.getPage_size());
        pageResult.setCount(page.getCount());
        return pageResult;
    }

    public static <T, E> PageResult<T> fromAnother(PageResult<E> page, Function<E, T> converter) {
        PageResult<T> pageResult = new PageResult<>();
        pageResult.setTotal_pages(page.getTotal_pages());
        pageResult.setPage(page.getPage());
        pageResult.setPage_size(page.getPage_size());
        pageResult.setCount(page.getCount());
        pageResult.setItems(page.getItems().stream().map(converter).collect(Collectors.toList()));
        return pageResult;
    }

    public boolean isEmpty() {
        return CollectionUtil.isEmpty(items);
    }

}
