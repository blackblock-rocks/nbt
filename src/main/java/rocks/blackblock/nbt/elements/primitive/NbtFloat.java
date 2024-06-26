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
 * The float tag (type ID 5) is used for storing a single-precision 32-bit IEEE 754 floating point value; a Java primitive {@code float}.
 *
 * @author dewy
 */
@NoArgsConstructor
@AllArgsConstructor
public class NbtFloat extends AbstractNbtNumber<Float> {
    private float value;

    /**
     * Constructs a float tag with a given name and value.
     *
     * @param name the tag's name.
     * @param value the tag's {@code float} value.
     */
    public NbtFloat(String name, float value) {
        this.setName(name);
        this.setValue(value);
    }

    @Override
    public byte getTypeId() {
        return NbtType.FLOAT.getId();
    }

    @Override
    public Float getValue() {
        return this.value;
    }

    /**
     * Sets the {@code float} value of this float tag.
     *
     * @param value new {@code float} value to be set.
     */
    public void setValue(float value) {
        this.value = value;
    }

    @Override
    public void write(DataOutput output, int depth, NbtTypeRegistry registry) throws IOException {
        output.writeFloat(this.value);
    }

    @Override
    public NbtFloat read(DataInput input, int depth, NbtTypeRegistry registry) throws IOException {
        this.value = input.readFloat();

        return this;
    }

    @Override
    public String toSnbt(int depth, NbtTypeRegistry registry, SnbtConfig config) {
        return this.value + "f";
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
    public NbtFloat fromJson(JsonObject json, int depth, NbtTypeRegistry registry) {
        if (json.has("name")) {
            this.setName(json.getAsJsonPrimitive("name").getAsString());
        } else {
            this.setName(null);
        }

        this.value = json.getAsJsonPrimitive("value").getAsFloat();

        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NbtFloat nbtFloat = (NbtFloat) o;

        return Float.compare(nbtFloat.value, value) == 0;
    }

    @Override
    public int hashCode() {
        return (value != 0.0f ? Float.floatToIntBits(value) : 0);
    }
}
