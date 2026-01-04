package lab.ai.actions;

public interface Action {
    /**
     * Sprawdza, czy akcja jest aktualnie aktywna.
     * @return true jeśli akcja jest w trakcie wykonywania
     */
    boolean isActive();

    /**
     * Aktualizuje stan akcji.
     * @param dt czas delta od ostatniej aktualizacji (w sekundach)
     */
    void update(float dt);

    /**
     * Sprawdza, czy akcja zakończyła się.
     * @return true jeśli akcja się zakończyła
     */
    default boolean isFinished() {
        return !isActive();
    }
}

