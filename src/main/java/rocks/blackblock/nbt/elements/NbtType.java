package rocks.blackblock.nbt.elements;

import rocks.blackblock.nbt.api.registry.NbtTypeRegistry;
import rocks.blackblock.nbt.api.registry.NbtTypeRegistryException;
import rocks.blackblock.nbt.elements.array.NbtByteArray;
import rocks.blackblock.nbt.elements.array.NbtIntArray;
import rocks.blackblock.nbt.elements.array.NbtLongArray;
import rocks.blackblock.nbt.elements.collection.NbtCompound;
import rocks.blackblock.nbt.elements.collection.NbtList;
import rocks.blackblock.nbt.elements.primitive.*;

import java.util.ArrayList;

/**
 * Defines the 12 standard NBT tag types and their IDs supported by this library, laid out in the Notchian spec.
 *
 * @author dewy
 */
public enum NbtType {
    /**
     * ID: 1
     *
     * @see NbtByte
     */
    BYTE(1),

    /**
     * ID: 2
     *
     * @see NbtShort
     */
    SHORT(2),

    /**
     * ID: 3
     *
     * @see NbtInt
     */
    INT(3),

    /**
     * ID: 4
     *
     * @see NbtLong
     */
    LONG(4),

    /**
     * ID: 5
     *
     * @see NbtFloat
     */
    FLOAT(5),

    /**
     * ID: 6
     *
     * @see NbtDouble
     */
    DOUBLE(6),

    /**
     * ID: 7
     *
     * @see NbtByteArray
     */
    BYTE_ARRAY(7),

    /**
     * ID: 8
     *
     * @see NbtString
     */
    STRING(8),

    /**
     * ID: 9
     *
     * @see NbtList
     */
    LIST(9),

    /**
     * ID: 10
     *
     * @see NbtCompound
     */
    COMPOUND(10),

    /**
     * ID: 11
     *
     * @see NbtIntArray
     */
    INT_ARRAY(11),

    /**
     * ID: 12
     *
     * @see NbtLongArray
     */
    LONG_ARRAY(12);

    private final int id;

    NbtType(int id) {
        this.id = id;
    }

    public byte getId() {
        return (byte) id;
    }

    public static void registerAll(NbtTypeRegistry registry) {
        try {
            registry.registerTagType(BYTE.getId(), NbtByte.class, () -> new NbtByte(0));
            registry.registerTagType(SHORT.getId(), NbtShort.class, () -> new NbtShort(0));
            registry.registerTagType(INT.getId(), NbtInt.class, () -> new NbtInt(0));
            registry.registerTagType(LONG.getId(), NbtLong.class, () -> new NbtLong(0));
            registry.registerTagType(FLOAT.getId(), NbtFloat.class, () -> new NbtFloat(0));
            registry.registerTagType(DOUBLE.getId(), NbtDouble.class, () -> new NbtDouble(0));
            registry.registerTagType(BYTE_ARRAY.getId(), NbtByteArray.class, () -> new NbtByteArray(new ArrayList<>()));
            registry.registerTagType(STRING.getId(), NbtString.class, () -> new NbtString(""));
            registry.registerTagType(LIST.getId(), NbtList.class, NbtList::new);
            registry.registerTagType(COMPOUND.getId(), NbtCompound.class, NbtCompound::new);
            registry.registerTagType(INT_ARRAY.getId(), NbtIntArray.class, () -> new NbtIntArray(new ArrayList<>()));
            registry.registerTagType(LONG_ARRAY.getId(), NbtLongArray.class, () -> new NbtLongArray(new ArrayList<>()));
        } catch (NbtTypeRegistryException e) {
            // Should never happen.
            e.printStackTrace();
        }
    }
}
