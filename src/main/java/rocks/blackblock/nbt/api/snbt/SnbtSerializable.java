package rocks.blackblock.nbt.api.snbt;

import rocks.blackblock.nbt.api.registry.TagTypeRegistry;

/**
 * Interface for SNBT serialization. Must be implemented if your tag will be SNBT serializable. Reading is not yet supported.
 *
 * @author dewy
 */
public interface SnbtSerializable {
    String toSnbt(int depth, TagTypeRegistry registry, SnbtConfig config);
}
