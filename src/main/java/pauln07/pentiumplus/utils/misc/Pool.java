 /*
 * This file is part of the Pentium plus (https://github.com/pauln07org/Pentium-plus).
 * Copyright (c) 2022 Pentium plus.
 */

package pauln07.pentiumplus.utils.misc;

import java.util.ArrayDeque;
import java.util.Queue;

public class Pool<T> {
    private final Queue<T> items = new ArrayDeque<>();
    private final Producer<T> producer;

    public Pool(Producer<T> producer) {
        this.producer = producer;
    }

    public synchronized T get() {
        if (items.size() > 0) return items.poll();
        return producer.create();
    }

    public synchronized void free(T obj) {
        items.offer(obj);
    }
}
