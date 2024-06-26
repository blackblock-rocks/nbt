package rocks.blackblock.nbt.elements.primitive;

import com.google.gson.JsonObject;
import rocks.blackblock.nbt.api.NbtElement;
import rocks.blackblock.nbt.api.json.JsonSerializable;
import rocks.blackblock.nbt.api.registry.NbtTypeRegistry;
import rocks.blackblock.nbt.api.snbt.SnbtConfig;
import rocks.blackblock.nbt.api.snbt.SnbtSerializable;
import rocks.blackblock.nbt.elements.NbtType;
import rocks.blackblock.nbt.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;

/**
 * The string tag (type ID 8) is used for storing a UTF-8 encoded {@code String}, prefixed by a length value stored as a 32-bit {@code int}.
 *
 * @author dewy
 */
@NoArgsConstructor
@AllArgsConstructor
public class NbtString extends NbtElement implements SnbtSerializable, JsonSerializable {
    private @NonNull String value;

    /**
     * Constructs a string tag with a given name and value.
     *
     * @param name the tag's name.
     * @param value the tag's {@code String} value.
     */
    public NbtString(String name, @NonNull String value) {
        this.setName(name);
        this.setValue(value);
    }

    @Override
    public byte getTypeId() {
        return NbtType.STRING.getId();
    }

    @Override
    public String getValue() {
        return this.value;
    }

    /**
     * Sets the {@code String} value of this string tag.
     *
     * @param value new {@code String} value to be set.
     */
    public void setValue(@NonNull String value) {
        this.value = value;
    }

    @Override
    public void write(DataOutput output, int depth, NbtTypeRegistry registry) throws IOException {
        output.writeUTF(this.value);
    }

    @Override
    public NbtString read(DataInput input, int depth, NbtTypeRegistry registry) throws IOException {
        this.value = input.readUTF();

        return this;
    }

    @Override
    public String toSnbt(int depth, NbtTypeRegistry registry, SnbtConfig config) {
        return StringUtils.escapeSnbt(this.value);
    }

    @Override
    public JsonObject toJson(int depth, NbtTypeRegistry registry) {
        JsonObject json = new JsonObject();
        json.addProperty("type", this.getTypeId());

        if (this.getName() != null) {
            json.addProperty("name", this.getName());
        }

        json.addProperty("value", this.value);

        return json;
    }

    @Override
    public NbtString fromJson(JsonObject json, int depth, NbtTypeRegistry registry) {
        if (json.has("name")) {
            this.setName(json.getAsJsonPrimitive("name").getAsString());
        } else {
            this.setName(null);
        }

        this.value = json.getAsJsonPrimitive("value").getAsString();

        return this;
    }

    @Override
    public String toString() {
        return this.toSnbt(0, new NbtTypeRegistry(), new SnbtConfig());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NbtString nbtString = (NbtString) o;

        return Objects.equals(value, nbtString.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
