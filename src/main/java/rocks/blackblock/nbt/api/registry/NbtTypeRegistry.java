package rocks.blackblock.nbt.api.registry;

import rocks.blackblock.nbt.api.NbtElement;
import rocks.blackblock.nbt.elements.NbtType;
import lombok.NonNull;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * A registry mapping {@code byte} tag type IDs to tag type classes. Used to register custom-made {@link NbtElement} types.
 *
 * @author dewy
 */
public class NbtTypeRegistry {
    private final Map<Byte, NbtElementInfo<? extends NbtElement>> registry = new HashMap<>();

    {
        NbtType.registerAll(this);
    }

    /**
     * Register a custom-made tag type with a unique {@code byte} ID. IDs 0-12 (inclusive) are reserved and may not be used.
     *
     * @param id the tag type's unique ID used in reading and writing.
     * @param clazz the tag type class.
     * @throws NbtTypeRegistryException if the ID provided is either registered already or is a reserved ID (0-12 inclusive).
     */
    public <T extends NbtElement> void registerTagType(byte id, @NonNull Class<T> clazz, Supplier<T> instantiator) throws NbtTypeRegistryException {
        if (id == 0) {
            throw new NbtTypeRegistryException("Cannot register NBT tag type " + clazz + " with ID " + id + ", as that ID is reserved.");
        }

        if (this.registry.containsKey(id)) {
            throw new NbtTypeRegistryException("Cannot register NBT tag type " + clazz + " with ID " + id + ", as that ID is already in use by the tag type " + this.registry.get(id).getElementClass().getSimpleName());
        }

        NbtElementInfo<T> info = new NbtElementInfo<>(clazz, instantiator);

        this.registry.put(id, info);
    }

    /**
     * Deregister a custom-made tag type with a provided tag type ID.
     *
     * @param id the ID of the tag type to deregister.
     * @return if the tag type was deregistered successfully.
     */
    public boolean deregisterTagType(byte id)  {
        if (id >= 0 && id <= 12) {
            return false;
        }

        return this.registry.remove(id) != null;
    }

    /**
     * Deregister a custom-made tag type with a provided tag type ID and class value.
     *
     * @param id the ID of the tag type to deregister.
     * @param clazz the class value of the tag type to deregister.
     * @return if the tag type was deregistered successfully.
     */
    public boolean deregisterTagType(byte id, Class<? extends NbtElement> clazz) {
        return this.registry.remove(id, clazz);
    }

    /**
     * Returns a tag type class value from the registry from a provided {@code byte} ID.
     *
     * @param id the ID of the tag type to retrieve.
     * @return a tag type class value from the registry from a provided {@code byte} ID.
     */
    public Class<? extends NbtElement> getClassFromId(byte id) {
        return this.registry.get(id).getElementClass();
    }

    /**
     * Get a new instance by its id
     *
     * @since   1.6.0
     */
    public NbtElement createInstanceFromId(byte id) {
        NbtElementInfo info = this.registry.get(id);

        if (info == null) {
            return null;
        }

        return info.createInstance();
    }

    /**
     * Returns an empty instance of the given {@link NbtElement} type, with a {@code null} name and a default (possibly {@code null}) value.
     * Only use this if you really know what you're doing.
     *
     * @param clazz the tag type to instantiate.
     * @return an empty instance of the tag type provided.
     * @throws NbtTypeRegistryException if a reflection error occurs when instantiating the tag.
     */
    public NbtElement instantiate(@NonNull Class<? extends NbtElement> clazz) throws NbtTypeRegistryException {
        try {
            Constructor<? extends NbtElement> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);

            return constructor.newInstance();
        } catch (ReflectiveOperationException e) {
            throw new NbtTypeRegistryException("Instance of tag type class " + clazz.getSimpleName() + " could not be created.", e);
        }
    }

    /**
     * NBT Element info
     *
     * @since   1.6.0
     */
    public static class NbtElementInfo<T extends NbtElement> {

        public final @NonNull Class<T> elementClass;
        public final Supplier<T> instantiator;

        public NbtElementInfo(@NonNull Class<T> elementClass, Supplier<T> instantiator) {
            this.elementClass = elementClass;
            this.instantiator = instantiator;
        }

        public @NonNull Class<T> getElementClass() {
            return this.elementClass;
        }

        public @NonNull T createInstance() {
            return this.instantiator.get();
        }
    }
}
