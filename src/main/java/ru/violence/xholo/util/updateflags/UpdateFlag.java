package ru.violence.xholo.util.updateflags;

import ru.violence.xholo.api.VirtualEntityData;

public abstract class UpdateFlag<T extends VirtualEntityData> {
    private final int id;

    public UpdateFlag(int id) {
        this.id = id;
    }

    public final int getId() {
        return this.id;
    }

    public abstract boolean isChanged(T o, T n);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UpdateFlag<?> that = (UpdateFlag<?>) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "UpdateFlag{" +
               "id=" + id +
               '}';
    }
}
