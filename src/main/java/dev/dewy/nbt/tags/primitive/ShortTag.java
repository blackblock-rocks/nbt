package dev.dewy.nbt.tags.primitive;

import dev.dewy.nbt.TagType;
import dev.dewy.nbt.TagTypeRegistry;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

@NoArgsConstructor
@AllArgsConstructor
public class ShortTag extends NumericalTag<Short> {
    private short value;

    public ShortTag(@NonNull Number value) {
        this(null, value);
    }

    public ShortTag(String name, @NonNull Number value) {
        this(name, value.shortValue());
    }

    public ShortTag(String name, short value) {
        this.setName(name);
        this.setValue(value);
    }

    @Override
    public byte getTypeId() {
        return TagType.SHORT.getId();
    }

    @Override
    public Short getValue() {
        return this.value;
    }

    public void setValue(short value) {
        this.value = value;
    }

    @Override
    public void write(DataOutput output, int depth, TagTypeRegistry registry) throws IOException {
        output.writeShort(this.value);
    }

    @Override
    public ShortTag read(DataInput input, int depth, TagTypeRegistry registry) throws IOException {
        this.value = input.readShort();

        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ShortTag shortTag = (ShortTag) o;

        return value == shortTag.value;
    }

    @Override
    public int hashCode() {
        return value;
    }
}
