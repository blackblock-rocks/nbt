package rocks.blackblock.nbt.io;

import rocks.blackblock.nbt.api.NbtElement;
import rocks.blackblock.nbt.api.registry.NbtTypeRegistry;
import rocks.blackblock.nbt.elements.NbtType;
import rocks.blackblock.nbt.elements.collection.NbtCompound;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.io.DataInput;
import java.io.IOException;

/**
 * Used to read root {@link NbtCompound}s using a certain {@link NbtTypeRegistry}.
 *
 * @author dewy
 */
@AllArgsConstructor
public class NbtReader {
    private @NonNull NbtTypeRegistry typeRegistry;

    /**
     * Read a non-root NBT element from a stream.
     *
     * @author  Jelle De Loecker jelle@elevenways.be
     * @since   1.6.0
     *
     * @throws IOException if any I/O error occurs.
     *
     * @param  input the stream to read from.
     *
     * @return The {@link NbtElement} read from the stream.
     */
    public NbtElement elementFromStream(@NonNull DataInput input) throws IOException {

        byte type = input.readByte();

        NbtElement result = this.typeRegistry.createInstanceFromId(type);

        if (result == null) {
            return null;
        }

        result.read(input, 0, this.typeRegistry);

        return result;
    }

    /**
     * Reads a root {@link NbtCompound} from a {@link DataInput} stream.
     *
     * @param input the stream to read from.
     * @return the root {@link NbtCompound} read from the stream.
     * @throws IOException if any I/O error occurs.
     */
    public NbtCompound rootFromStream(@NonNull DataInput input) throws IOException {
        if (input.readByte() != NbtType.COMPOUND.getId()) {
            throw new IOException("Root tag in NBT structure must be a compound tag.");
        }

        NbtCompound result = new NbtCompound();

        result.setName(input.readUTF());
        result.read(input, 0, this.typeRegistry);

        return result;
    }

    /**
     * Returns the {@link NbtTypeRegistry} currently in use by this reader.
     *
     * @return the {@link NbtTypeRegistry} currently in use by this reader.
     */
    public NbtTypeRegistry getTypeRegistry() {
        return typeRegistry;
    }

    /**
     * Sets the {@link NbtTypeRegistry} currently in use by this reader. Used to utilise custom-made tag types.
     *
     * @param typeRegistry the new {@link NbtTypeRegistry} to be set.
     */
    public void setTypeRegistry(@NonNull NbtTypeRegistry typeRegistry) {
        this.typeRegistry = typeRegistry;
    }
}
