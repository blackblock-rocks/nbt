package rocks.blackblock.nbt.io;

import rocks.blackblock.nbt.api.registry.NbtTypeRegistry;
import rocks.blackblock.nbt.elements.NbtType;
import rocks.blackblock.nbt.elements.collection.NbtCompound;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.io.DataOutput;
import java.io.IOException;

/**
 * Used to write root {@link NbtCompound}s using a certain {@link NbtTypeRegistry}.
 *
 * @author dewy
 */
@AllArgsConstructor
public class NbtWriter {
    private @NonNull NbtTypeRegistry typeRegistry;

    /**
     * Writes the given root {@link NbtCompound} to a {@link DataOutput} stream.
     *
     * @param compound the NBT structure to write, contained within a {@link NbtCompound}.
     * @param output the stream to write to.
     * @throws IOException if any I/O error occurs.
     */
    public void toStream(@NonNull NbtCompound compound, @NonNull DataOutput output) throws IOException {
        output.writeByte(NbtType.COMPOUND.getId());

        if (compound.getName() == null) {
            output.writeUTF("");
        } else {
            output.writeUTF(compound.getName());
        }

        compound.write(output, 0, this.typeRegistry);
    }

    /**
     * Returns the {@link NbtTypeRegistry} currently in use by this writer.
     *
     * @return the {@link NbtTypeRegistry} currently in use by this writer.
     */
    public NbtTypeRegistry getTypeRegistry() {
        return typeRegistry;
    }

    /**
     * Sets the {@link NbtTypeRegistry} currently in use by this writer. Used to utilise custom-made tag types.
     *
     * @param typeRegistry the new {@link NbtTypeRegistry} to be set.
     */
    public void setTypeRegistry(@NonNull NbtTypeRegistry typeRegistry) {
        this.typeRegistry = typeRegistry;
    }
}
