package com.ednaldoluiz.websocket.infra.persistence.utils;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.*;

public final class BatchStreamUtils {

    private BatchStreamUtils() {}

    public static <K, T> Stream<T> streamByBatch(
            K initialCursor,
            int batchSize,
            BiFunction<K, Integer, List<T>> fetchBatchFn,
            Function<T, K> nextCursorFn
    ) {
        Objects.requireNonNull(fetchBatchFn);
        Objects.requireNonNull(nextCursorFn);

        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(new Iterator<T>() {
            private K cursor = initialCursor;
            private List<T> currentBatch = fetchBatchFn.apply(cursor, batchSize);
            private int batchIndex = 0;

            @Override
            public boolean hasNext() {
                return currentBatch != null && batchIndex < currentBatch.size();
            }

            @Override
            public T next() {
                if (!hasNext()) throw new NoSuchElementException();
                T nextVal = currentBatch.get(batchIndex++);
                cursor = nextCursorFn.apply(nextVal);
                if (batchIndex >= currentBatch.size()) {
                    currentBatch = fetchBatchFn.apply(cursor, batchSize);
                    batchIndex = 0;
                    if (currentBatch.isEmpty()) currentBatch = null;
                }
                return nextVal;
            }
        }, 0), false);
    }
}

