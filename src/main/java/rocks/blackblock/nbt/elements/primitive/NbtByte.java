package rocks.blackblock.nbt.elements.primitive;

import com.google.gson.JsonObject;
import rocks.blackblock.nbt.api.registry.NbtTypeRegistry;
import rocks.blackblock.nbt.api.snbt.SnbtConfig;
import rocks.blackblock.nbt.elements.NbtType;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * The byte tag (type ID 1) is used for storing an 8-bit signed two's complement integer; a Java primitive {@code byte}.
 *
 * @author dewy
 */
@NoArgsConstructor
@AllArgsConstructor
public class NbtByte extends AbstractNbtNumber<Byte> {
    private byte value;

    /**
     * Constructs a byte tag with a given value.
     *
     * @param value the tag's {@code Number} value, to be converted to {@code byte}.
     */
    public NbtByte(@NonNull Number value) {
        this(null, value);
    }

    /**
     * Constructs a byte tag with a given name and value.
     *
     * @param name the tag's name.
     * @param value the tag's {@code Number} value, to be converted to {@code byte}.
     */
    public NbtByte(String name, @NonNull Number value) {
        this(name, value.byteValue());
    }

    /**
     * Constructs a byte tag with a given name and value.
     *
     * @param name the tag's name.
     * @param value the tag's {@code byte} value.
     */
    public NbtByte(String name, byte value) {
        this.setName(name);
        this.setValue(value);
    }

    @Override
    public byte getTypeId() {
        return NbtType.BYTE.getId();
    }

    @Override
    public Byte getValue() {
        return this.value;
    }

    /**
     * Sets the {@code byte} value of this byte tag.
     *
     * @param value new {@code byte} value to be set.
     */
    public void setValue(byte value) {
        this.value = value;
    }

    @Override
    public void write(DataOutput output, int depth, NbtTypeRegistry registry) throws IOException {
        output.writeByte(this.value);
    }

    @Override
    public NbtByte read(DataInput input, int depth, NbtTypeRegistry registry) throws IOException {
        this.value = input.readByte();

        return this;
    }

    @Override
    public String toSnbt(int depth, NbtTypeRegistry registry, SnbtConfig config) {
        return this.value + "b";
    }

    @Override
    public JsonObject toJson(int depth, NbtTypeRegistry registry) throws IOException {
        JsonObject json = new JsonObject();
        json.addProperty("type", this.getTypeId());

        if (this.getName() != null) {
            json.addProperty("name", this.getName());
        }

        json.addProperty("value", this.value);

        return json;
    }

    @Override
    public NbtByte fromJson(JsonObject json, int depth, NbtTypeRegistry registry) throws IOException {
        if (json.has("name")) {
            this.setName(json.getAsJsonPrimitive("name").getAsString());
        } else {
            this.setName(null);
        }

        this.value = json.getAsJsonPrimitive("value").getAsByte();

        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NbtByte nbtByte = (NbtByte) o;

        return value == nbtByte.value;
    }

    @Override
    public int hashCode() {
        return value;
    }
}
