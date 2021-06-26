package dev.dewy.nbt.tags;

import dev.dewy.nbt.Tag;
import dev.dewy.nbt.TagType;

import java.io.DataOutput;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of the compound tag. A map in its raw form.
 *
 * @author dewy
 */
public class CompoundTag implements Tag {
    private Map<String, Tag> value;

    /**
     * Constructs a new empty compound tag.
     */
    public CompoundTag() {
        this.value = new HashMap<>();
    }

    /**
     * Constructs a new compound tag with a given value.
     *
     * @param value The value to be contained within the tag.
     * @throws IllegalArgumentException If the value parameter is null.
     */
    public CompoundTag(Map<String, Tag> value) {
        if (value == null) {
            throw new IllegalArgumentException("Value of compound tag cannot be null.");
        }

        this.value = value;
    }

    /**
     * Returns the map value contained inside the tag.
     *
     * @return The map value contained inside the tag.
     */
    public Map<String, Tag> getValue() {
        return value;
    }

    /**
     * Sets the map value contained inside the tag.
     *
     * @param value The new map value to be contained inside this tag.
     */
    public void setValue(Map<String, Tag> value) {
        if (value == null) {
            throw new IllegalArgumentException("Value of compound tag cannot be null.");
        }

        this.value = value;
    }

    @Override
    public TagType getType() {
        return TagType.COMPOUND;
    }

    @Override
    public void write(DataOutput output) throws IOException {
        for (Map.Entry<String, Tag> tag : this.value.entrySet()) {
            output.writeByte(tag.getValue().getType().getId());

            if (tag.getValue().getType() != TagType.END) {
                output.writeUTF(tag.getKey());
                tag.getValue().write(output);
            }
        }

        output.writeByte(TagType.END.getId()); // 0x00
    }

    /**
     * Write the compound tag to a {@link DataOutput} stream as the root compound with no name of its own.
     *
     * @param output The stream to write to.
     * @param root Whether or not to write the tag as the root compound.
     * @throws IOException If any IO error occurs.
     */
    public void write(DataOutput output, boolean root) throws IOException {
        if (root) {
            write(output, "");
        } else {
            write(output);
        }
    }

    /**
     * Write the compound tag to a {@link DataOutput} stream as the root compound with a name of its own.
     *
     * @param output The stream to write to.
     * @param rootName The root compound's name.
     * @throws IOException If any IO error occurs.
     */
    public void write(DataOutput output, String rootName) throws IOException {
        output.writeByte(TagType.COMPOUND.getId());
        output.writeUTF(rootName);

        write(output);
    }

    /**
     * Returns true if this compound tag contains no entries.
     *
     * @return True if this compound tag contains no entries.
     */
    public boolean isEmpty() {
        return this.value.isEmpty();
    }

    /**
     * Returns the number of entries in this compound tag.
     *
     * @return The number of entries in this compound tag.
     */
    public int size() {
        return this.value.size();
    }

    /**
     * Associates the specified tag value with the specified name in this compound tag.
     * If the compound tag previously contained a mapping for the name, the old tag value is replaced by the specified tag value.
     *
     * @param name Name with which the specified tag value is to be associated.
     * @param tag Tag value to be associated with the specified name.
     * @return The previous tag value associated with name, or null if there was no mapping for name.
     */
    public Tag put(String name, Tag tag) {
        if (name == null || tag == null) {
            throw new IllegalArgumentException("Tag (name) must not be null to put.");
        }

        return this.value.put(name, tag);
    }

    /**
     * If the specified name is not already associated with a tag value (or is mapped to null) associates
     * it with the given tag value and returns null, else returns the current tag value. See Map.putIfAbsent.
     *
     * @param name Name with which the specified tag value is to be associated.
     * @param tag Value to be associated with the specified tag.
     * @return The previous tag value associated with the specified name, or null if there was no mapping for the name.
     */
    public Tag putIfAbsent(String name, Tag tag) {
        if (name == null || tag == null) {
            throw new IllegalArgumentException("Tag (name) must not be null to putIfAbsent.");
        }

        return this.value.putIfAbsent(name, tag);
    }

    /**
     * Returns the value to which the specified name is mapped, or null if this compound tag contains no mapping for the name.
     *
     * @param name The name whose associated tag value is to be returned.
     * @return The value to which the specified name is mapped, or null if this compound tag contains no mapping for the name.
     */
    public Tag get(String name) {
        return this.value.get(name);
    }

    /**
     * Removes the mapping for a name from this compound tag if it is present.
     *
     * @param name Name whose mapping is to be removed from the compound tag.
     * @return The previous value associated with name, or null if there was no mapping for name.
     */
    public Tag remove(String name) {
        return this.value.remove(name);
    }

    /**
     * Removes the entry for a specified named tag. Must be equal in its name and the tag itself to be removed.
     *
     * @param name Name with which the specified tag is associated.
     * @param tag Tag expected to be associated with the specified name.
     * @return True if the entry was removed.
     */
    public boolean remove(String name, Tag tag) {
        return this.value.remove(name, tag);
    }

    /**
     * Returns true if this compound tag contains a tag with the specified name.
     *
     * @param name The name whose presence is to be tested.
     * @return True if this compound tag contains a tag with the specified name.
     */
    public boolean contains(String name) {
        return this.value.containsKey(name);
    }

    /**
     * Returns true if this compound tag contains the specified tag, regardless of its name.
     *
     * @param tag The tag whose presence is to be tested.
     * @return True if this compound tag contains the specified tag, regardless of its name.
     */
    public boolean contains(Tag tag) {
        return this.value.containsValue(tag);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CompoundTag that = (CompoundTag) o;

        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}