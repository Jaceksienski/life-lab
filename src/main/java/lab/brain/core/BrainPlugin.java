package lab.brain.core;

public interface BrainPlugin {
    String id();
    default int order() { return 100; }
    void onEvent(BrainEvent event, BrainHub hub);
}
