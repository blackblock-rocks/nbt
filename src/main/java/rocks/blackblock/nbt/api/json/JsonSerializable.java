package rocks.blackblock.nbt.api.json;

import com.google.gson.JsonObject;
import rocks.blackblock.nbt.api.NbtElement;
import rocks.blackblock.nbt.api.registry.NbtTypeRegistry;

import java.io.IOException;

/**
 * Interface for JSON (de)serialization. Must be implemented if your tag will be JSON (de)serializable.
 *
 * @author dewy
 */
public interface JsonSerializable {
    /**
     * Serializes this tag into a GSON {@code JsonObject}.
     *
     * @param depth the current depth of the NBT data structure.
     * @param registry the {@link NbtTypeRegistry} to be used in serialization.
     * @return the serialized {@code JsonObject}.
     * @throws IOException if any I/O error occurs.
     */
    JsonObject toJson(int depth, NbtTypeRegistry registry) throws IOException;

    /**
     * Deserializes this tag from a give {@code JsonObject}.
     *
     * @param json the {@code JsonObject} to be deserialized.
     * @param depth the current depth of the NBT data structure.
     * @param registry the {@link NbtTypeRegistry} to be used in deserialization.
     * @return this (literally {@code return this;} after deserialization).
     * @throws IOException if any I/O error occurs.
     */
    NbtElement fromJson(JsonObject json, int depth, NbtTypeRegistry registry) throws IOException;
}
