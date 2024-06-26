package rocks.blackblock.nbt.elements.primitive;

import rocks.blackblock.nbt.api.NbtElement;
import rocks.blackblock.nbt.api.json.JsonSerializable;
import rocks.blackblock.nbt.api.registry.NbtTypeRegistry;
import rocks.blackblock.nbt.api.snbt.SnbtConfig;
import rocks.blackblock.nbt.api.snbt.SnbtSerializable;

/**
 * An abstract superclass of all NBT tags representing numeric values that can be converted to the primitive types.
 *
 * @param <T> the {@code Number} type this NBT tag represents.
 * @author dewy
 */
public abstract class AbstractNbtNumber<T extends Number> extends NbtElement implements SnbtSerializable, JsonSerializable {
    @Override
    public abstract T getValue();

    /**
     * Returns the value held by this tag as a primitive {@code byte}.
     *
     * @return the value held by this tag as a primitive {@code byte}.
     */
    public byte byteValue() {
        return this.getValue().byteValue();
    }

    /**
     * Returns the value held by this tag as a primitive {@code short}.
     *
     * @return the value held by this tag as a primitive {@code short}.
     */
    public short shortValue() {
        return this.getValue().shortValue();
    }

    /**
     * Returns the value held by this tag as a primitive {@code int}.
     *
     * @return the value held by this tag as a primitive {@code int}.
     */
    public int intValue() {
        return this.getValue().intValue();
    }

    /**
     * Returns the value held by this tag as a primitive {@code long}.
     *
     * @return the value held by this tag as a primitive {@code long}.
     */
    public long longValue() {
        return this.getValue().longValue();
    }

    /**
     * Returns the value held by this tag as a primitive {@code float}.
     *
     * @return the value held by this tag as a primitive {@code float}.
     */
    public float floatValue() {
        return this.getValue().floatValue();
    }

    /**
     * Returns the value held by this tag as a primitive {@code double}.
     *
     * @return the value held by this tag as a primitive {@code double}.
     */
    public double doubleValue() {
        return this.getValue().doubleValue();
    }

    @Override
    public String toString() {
        return this.toSnbt(0, new NbtTypeRegistry(), new SnbtConfig());
    }
}
