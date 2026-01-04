package lab.brain.plugin.encoder;

import lab.brain.api.*;
import lab.brain.core.*;
import lab.brain.keys.BrainKeys;

import java.util.*;

public final class EncoderPlugin implements BrainPlugin {
    @Override public String id() { return "encoder"; }
    @Override public int order() { return 20; }

    @Override
    public void onEvent(BrainEvent event, BrainHub hub) {
        if (event.type() != BrainEventType.PERCEPTION) return;

        PerceptionFrame frame = hub.perception().orElse(null);
        InternalState st = hub.internal().orElse(null);
        if (frame == null || st == null) return;

        short needsBucket = bucketNeeds(st);

        ArrayList<Integer> ptrs = new ArrayList<>(128);
        for (Stimulus s : frame.stimuli()) {
            int dir8 = dir8(s.dx(), s.dy());
            int dist = distRing(s.dx(), s.dy());

            ptrs.add(220000 + s.channel().ordinal());
            ptrs.add(200000 + dir8);
            ptrs.add(210000 + dist);
            for (TraitId t : s.traits()) ptrs.add(100000 + t.value());
        }

        int[] pointers = ptrs.stream().distinct().mapToInt(Integer::intValue).toArray();
        hub.put(BrainKeys.CTX, new EncodedContext(frame.tick(), needsBucket, pointers));
    }

    private short bucketNeeds(InternalState st) {
        short b = 0;
        if (st.energy() < 0.3f) b |= 0b0001;     // hungry
        if (st.integrity() < 0.4f) b |= 0b0010;  // injured
        return b;
    }

    private int distRing(int dx, int dy) {
        int d = Math.max(Math.abs(dx), Math.abs(dy));
        if (d <= 0) return 0;
        if (d == 1) return 1;
        if (d == 2) return 2;
        return 3; // 3+
    }

    private int dir8(int dx, int dy) {
        int sx = Integer.compare(dx, 0);
        int sy = Integer.compare(dy, 0);
        if (sx == 0 && sy < 0) return 0;
        if (sx > 0 && sy < 0) return 1;
        if (sx > 0 && sy == 0) return 2;
        if (sx > 0 && sy > 0) return 3;
        if (sx == 0 && sy > 0) return 4;
        if (sx < 0 && sy > 0) return 5;
        if (sx < 0 && sy == 0) return 6;
        return 7;
    }
}
