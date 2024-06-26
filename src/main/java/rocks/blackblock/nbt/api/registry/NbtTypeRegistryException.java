package rocks.blackblock.nbt.api.registry;

/**
 * Checked exception thrown when any issue arises relating to the {@link NbtTypeRegistry}.
 *
 * @author dewy
 */
public class NbtTypeRegistryException extends Exception {
    /**
     * Constructs a new {@link NbtTypeRegistryException} with the specified detail message. The cause is not initialized, and may subsequently be initialized by a call to initCause.
     *
     * @param message the detail message. The detail message is saved for later retrieval by the getMessage() method.
     */
    public NbtTypeRegistryException(String message) {
        super(message);
    }

    /**
     * Constructs a new {@link NbtTypeRegistryException} with the specified detail message and cause.
     * Note that the detail message associated with cause is not automatically incorporated in this exception's detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the getMessage() method).
     * @param cause the cause (which is saved for later retrieval by the getCause() method). (A null value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public NbtTypeRegistryException(String message, Throwable cause) {
        super(message, cause);
    }
}
