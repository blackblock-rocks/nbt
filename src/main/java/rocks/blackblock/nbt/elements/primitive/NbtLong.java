package rocks.blackblock.nbt.elements.primitive;

import com.google.gson.JsonObject;
import rocks.blackblock.nbt.api.registry.NbtTypeRegistry;
import rocks.blackblock.nbt.api.snbt.SnbtConfig;
import rocks.blackblock.nbt.elements.NbtType;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * The long tag (type ID 4) is used for storing a 64-bit signed two's complement integer; a Java primitive {@code long}.
 *
 * @author dewy
 */
@NoArgsConstructor
@AllArgsConstructor
public class NbtLong extends AbstractNbtNumber<Long> {
    private long value;

    /**
     * Constructs a long tag with a given name and value.
     *
     * @param name the tag's name.
     * @param value the tag's {@code long} value.
     */
    public NbtLong(String name, long value) {
        this.setName(name);
        this.setValue(value);
    }

    @Override
    public byte getTypeId() {
        return NbtType.LONG.getId();
    }

    @Override
    public Long getValue() {
        return this.value;
    }

    /**
     * Sets the {@code long} value of this long tag.
     *
     * @param value new {@code long} value to be set.
     */
    public void setValue(long value) {
        this.value = value;
    }

    @Override
    public void write(DataOutput output, int depth, NbtTypeRegistry registry) throws IOException {
        output.writeLong(this.value);
    }

    @Override
    public NbtLong read(DataInput input, int depth, NbtTypeRegistry registry) throws IOException {
        this.value = input.readLong();

        return this;
    }

    @Override
    public String toSnbt(int depth, NbtTypeRegistry registry, SnbtConfig config) {
        return this.value + "L";
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
    public NbtLong fromJson(JsonObject json, int depth, NbtTypeRegistry registry) {
        if (json.has("name")) {
            this.setName(json.getAsJsonPrimitive("name").getAsString());
        } else {
            this.setName(null);
        }

        this.value = json.getAsJsonPrimitive("value").getAsLong();

        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NbtLong nbtLong = (NbtLong) o;

        return value == nbtLong.value;
    }

    @Override
    public int hashCode() {
        return (int) (value ^ (value >>> 32));
    }
}
