package ru.violence.xholo.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class UniqueInt {
    private final int value;
    private final int idCounter;

    public UniqueInt(int value) {
        this(value, 0);
    }

    public UniqueInt(int value, int idCounter) {
        this.value = value;
        this.idCounter = idCounter;
    }

    @Contract(pure = true)
    public int get() {
        return value;
    }

    @Contract(value = "_ -> new", pure = true)
    public @NotNull UniqueInt set(int value) {
        return new UniqueInt(value, idCounter + 1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UniqueInt uniqueInt = (UniqueInt) o;

        return idCounter == uniqueInt.idCounter;
    }

    @Override
    public int hashCode() {
        return idCounter;
    }
}
