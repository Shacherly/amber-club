package com.loser.backend.club.config;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.PageUtil;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.trading.backend.coupon.common.util.Functions;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author ~~ trading.s
 * @date 13:43 10/01/21
 */
@Slf4j
@Getter
@Configuration
public class ExecutorConfiguration {

    private final AtomicInteger COUNTER = new AtomicInteger(-1);

    private final ExecutorService EXECUTOR = new ThreadPoolExecutor(
            16,
            1024,
            3,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(),
            new ThreadFactoryBuilder()
                    .setNameFormat("Global-pool-%d")
                    .setUncaughtExceptionHandler((thread, throwable) -> {
                        log.error("Exception occurred in thread {}, cause {}", thread.getName(), ExceptionUtil.getRootCause(throwable), throwable);
                    })
                    .build(),
            new ThreadPoolExecutor.CallerRunsPolicy()
    );

    public <E> void parallelSubmit(List<E> sources, Consumer<List<E>> function) {
        COUNTER.getAndIncrement();
        if (CollectionUtil.isEmpty(sources) || Objects.isNull(function)) return;
        int pageSize = 3072;
        long count = sources.size();
        AtomicInteger counter = new AtomicInteger();
        List<Future<Boolean>> futures = new ArrayList<>(4000);
        Instant start = Instant.now();
        for (long limit = 0L; limit * pageSize < count; limit++) {
            Future<Boolean> submit = EXECUTOR.submit(() -> {
                int page = counter.getAndIncrement();
                // log.debug("Parallel task index = {}", page);
                List<E> partition = CollectionUtil.page(page, pageSize, sources);
                try {
                    function.accept(partition);
                    return true;
                } catch (Exception e) {
                    log.error("Parallel task error = {}", e.getMessage(), e);
                    return false;
                }
            });
            futures.add(submit);
        }
        boolean future = true;
        try {
            for (Future<Boolean> part : futures) {
                future = future && part.get();
            }
        } catch (InterruptedException | ExecutionException e) {
            log.error(e.getMessage(), e);
        }
        Instant end = Instant.now();
        long spend = Duration.between(start, end).toMillis();
        if (counter.get() % 3000 == 0)
            log.info("ParallelSubmit result = {}, spend = {} ms !",future, spend);
    }

    public <T, E> void serial(long total, BiFunction<Integer, Integer, List<T>> producer, Function<T, E> exchanger, Function<List<E>, Integer> consumer) {
        if (total == 0 || Objects.isNull(producer)
                || Objects.isNull(exchanger) || Objects.isNull(consumer)) return;
        int pageSize = 2000;
        int totalPage = PageUtil.totalPage((int) total, pageSize);
        AtomicInteger counter = new AtomicInteger();
        Instant start = Instant.now();
        for (long limit = 0L; limit * pageSize < total; limit++) {
            int page = counter.addAndGet(1);
            List<E> partition = Functions.toList(producer.apply(page, pageSize), exchanger);
            Integer rows = consumer.apply(partition);
            log.info("SerialSubmit page = {}, size = {}, total = {}, totalPage = {}, Effected rows = {}", page, partition.size(), total, totalPage, rows);
        }
        Instant end = Instant.now();
        long spend = Duration.between(start, end).toMillis();
        log.info("SerialSubmit spend = {} ms !", spend);
    }

    public <T, E> void serial(long total, BiFunction<Integer, Integer, List<T>> producer, Function<T, E> exchanger, Function<List<E>, Integer> consumer, Integer pageSize) {
        if (total == 0 || Objects.isNull(producer)
                || Objects.isNull(exchanger) || Objects.isNull(consumer)) return;
        AtomicInteger counter = new AtomicInteger();
        Instant start = Instant.now();
        for (long limit = 0L; limit * pageSize < total; limit++) {
            int page = counter.addAndGet(1);
            List<E> partition = Functions.toList(producer.apply(page, pageSize), exchanger);
            Integer rows = consumer.apply(partition);
            log.info("SerialSubmit page = {}, size = {}, total = {}, Effected rows = {}", page, partition.size(), String.valueOf(total), rows);
        }
        Instant end = Instant.now();
        long spend = Duration.between(start, end).toMillis();
        log.info("SerialSubmit spend = {} ms !", spend);
    }

    public <T, E> void serialPage(int total, BiFunction<Integer, Integer, List<T>> producer, Function<T, E> exchanger, Consumer<E> consumer) {
        if (total == 0 || Objects.isNull(producer)
                || Objects.isNull(exchanger) || Objects.isNull(consumer)) return;
        int pageSize = 2000;
        int totalPage = PageUtil.totalPage(total, pageSize);
        Instant start = Instant.now();
        for (int page = 1; page <= totalPage; page++) {
            List<E> partition = Functions.toList(producer.apply(page, pageSize), exchanger);
            partition.forEach(consumer);
            log.info("SerialPage page = {}, size = {}, total = {}", page, partition.size(), total);
        }
        Instant end = Instant.now();
        long spend = Duration.between(start, end).toMillis();
        log.info("SerialPage spend = {} ms !", spend);
    }
}
