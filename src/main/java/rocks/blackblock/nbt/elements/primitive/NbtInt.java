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
 * The int tag (type ID 3) is used for storing a 32-bit signed two's complement integer; a Java primitive {@code int}.
 *
 * @author dewy
 */
@NoArgsConstructor
@AllArgsConstructor
public class NbtInt extends AbstractNbtNumber<Integer> {
    private int value;

    /**
     * Constructs an int tag with a given name and value.
     *
     * @param name the tag's name.
     * @param value the tag's {@code int} value.
     */
    public NbtInt(String name, int value) {
        this.setName(name);
        this.setValue(value);
    }

    @Override
    public byte getTypeId() {
        return NbtType.INT.getId();
    }

    @Override
    public Integer getValue() {
        return this.value;
    }

    /**
     * Sets the {@code int} value of this int tag.
     *
     * @param value new {@code int} value to be set.
     */
    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public void write(DataOutput output, int depth, NbtTypeRegistry registry) throws IOException {
        output.writeInt(this.value);
    }

    @Override
    public NbtInt read(DataInput input, int depth, NbtTypeRegistry registry) throws IOException {
        this.value = input.readInt();

        return this;
    }

    @Override
    public String toSnbt(int depth, NbtTypeRegistry registry, SnbtConfig config) {
        return Integer.toString(this.value);
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
    public NbtInt fromJson(JsonObject json, int depth, NbtTypeRegistry registry) {
        if (json.has("name")) {
            this.setName(json.getAsJsonPrimitive("name").getAsString());
        } else {
            this.setName(null);
        }

        this.value = json.getAsJsonPrimitive("value").getAsInt();

        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NbtInt nbtInt = (NbtInt) o;

        return value == nbtInt.value;
    }

    @Override
    public int hashCode() {
        return value;
    }
}
