package lab.brain.plugin.stm;

import java.util.*;

public final class RingBuffer<T> {
    private final Object[] buf;
    private int idx = 0;
    private int size = 0;

    public RingBuffer(int cap) { this.buf = new Object[cap]; }

    public void add(T x) {
        buf[idx] = x;
        idx = (idx + 1) % buf.length;
        size = Math.min(size + 1, buf.length);
    }

    public List<T> last(int n) {
        int k = Math.min(n, size);
        ArrayList<T> out = new ArrayList<>(k);
        for (int i = 0; i < k; i++) {
            int p = idx - 1 - i;
            if (p < 0) p += buf.length;
            @SuppressWarnings("unchecked")
            T v = (T) buf[p];
            out.add(v);
        }
        return out;
    }

    public int size() { return size; }
}
