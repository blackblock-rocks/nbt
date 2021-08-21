package dev.dewy.nbt.tags.primitive;

import dev.dewy.nbt.TagRegistry;
import dev.dewy.nbt.TagType;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

@NoArgsConstructor
@AllArgsConstructor
public class DoubleTag extends NumericalTag<Double> {
    private double value;

    public DoubleTag(String name, double value) {
        this.setName(name);
        this.setValue(value);
    }

    @Override
    public byte getTypeId() {
        return TagType.DOUBLE.getId();
    }

    @Override
    public Double getValue() {
        return this.value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public void write(DataOutput output, int depth, TagRegistry registry) throws IOException {
        output.writeDouble(this.value);
    }

    @Override
    public DoubleTag read(DataInput input, int depth, TagRegistry registry) throws IOException {
        this.value = input.readDouble();

        return this;
    }
}
