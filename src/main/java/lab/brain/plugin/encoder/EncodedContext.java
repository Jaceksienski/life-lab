package lab.brain.plugin.encoder;

public record EncodedContext(
        long tick,
        short needsBucket,
        int[] pointers
) {
    public boolean isHungry() { return (needsBucket & 0b0001) != 0; }
    public boolean isInjured() { return (needsBucket & 0b0010) != 0; }
}
