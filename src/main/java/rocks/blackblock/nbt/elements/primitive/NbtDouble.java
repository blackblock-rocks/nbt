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
 * The double tag (type ID 6) is used for storing a double-precision 64-bit IEEE 754 floating point value; a Java primitive {@code double}.
 *
 * @author dewy
 */
@NoArgsConstructor
@AllArgsConstructor
public class NbtDouble extends AbstractNbtNumber<Double> {
    private double value;

    /**
     * Constructs a double tag with a given name and value.
     *
     * @param name the tag's name.
     * @param value the tag's {@code double} value.
     */
    public NbtDouble(String name, double value) {
        this.setName(name);
        this.setValue(value);
    }

    @Override
    public byte getTypeId() {
        return NbtType.DOUBLE.getId();
    }

    @Override
    public Double getValue() {
        return this.value;
    }

    /**
     * Sets the {@code double} value of this double tag.
     *
     * @param value new {@code double} value to be set.
     */
    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public void write(DataOutput output, int depth, NbtTypeRegistry registry) throws IOException {
        output.writeDouble(this.value);
    }

    @Override
    public NbtDouble read(DataInput input, int depth, NbtTypeRegistry registry) throws IOException {
        this.value = input.readDouble();

        return this;
    }

    @Override
    public String toSnbt(int depth, NbtTypeRegistry registry, SnbtConfig config) {
        return this.value + "d";
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
    public NbtDouble fromJson(JsonObject json, int depth, NbtTypeRegistry registry) {
        if (json.has("name")) {
            this.setName(json.getAsJsonPrimitive("name").getAsString());
        } else {
            this.setName(null);
        }

        this.value = json.getAsJsonPrimitive("value").getAsDouble();

        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NbtDouble nbtDouble = (NbtDouble) o;

        return Double.compare(nbtDouble.value, value) == 0;
    }

    @Override
    public int hashCode() {
        long temp = Double.doubleToLongBits(value);
        return (int) (temp ^ (temp >>> 32));
    }
}
