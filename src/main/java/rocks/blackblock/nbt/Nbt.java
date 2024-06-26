package rocks.blackblock.nbt;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import rocks.blackblock.nbt.api.registry.NbtTypeRegistry;
import rocks.blackblock.nbt.api.snbt.SnbtConfig;
import rocks.blackblock.nbt.io.CompressionType;
import rocks.blackblock.nbt.io.NbtReader;
import rocks.blackblock.nbt.io.NbtWriter;
import rocks.blackblock.nbt.elements.collection.NbtCompound;
import lombok.Cleanup;
import lombok.NonNull;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.InflaterInputStream;

/**
 * Standard interface for reading and writing NBT data structures.
 *
 * @author dewy
 */
public class Nbt {
    private @NonNull Gson gson;
    private @NonNull NbtTypeRegistry typeRegistry;
    private @NonNull SnbtConfig snbtConfig;

    private final @NonNull NbtWriter writer;
    private final @NonNull NbtReader reader;

    /**
     * Constructs an instance of this class using a default {@link NbtTypeRegistry} (supporting the standard 12 tag types).
     */
    public Nbt() {
        this(new NbtTypeRegistry());
    }

    /**
     * Constructs an instance of this class using a given {@link NbtTypeRegistry}, with a default GSON instance.
     *
     * @param typeRegistry the tag type registry to be used, typically containing custom tag entries.
     */
    public Nbt(@NonNull NbtTypeRegistry typeRegistry) {
        this(typeRegistry, new Gson());
    }

    /**
     * Constructs an instance of this class using a given {@link NbtTypeRegistry}, with a default {@link SnbtConfig} instance.
     *
     * @param typeRegistry the tag type registry to be used, typically containing custom tag entries.
     * @param gson the GSON instance to be used.
     */
    public Nbt(@NonNull NbtTypeRegistry typeRegistry, @NonNull Gson gson) {
        this(typeRegistry, gson, new SnbtConfig());
    }

    /**
     * Constructs an instance of this class using a given {@link NbtTypeRegistry}, {@code Gson} and an {@link SnbtConfig}.
     *
     * @param typeRegistry the tag type registry to be used, typically containing custom tag entries.
     * @param gson the GSON instance to be used.
     * @param snbtConfig the SNBT config object to be used.
     */
    public Nbt(@NonNull NbtTypeRegistry typeRegistry, @NonNull Gson gson, @NonNull SnbtConfig snbtConfig) {
        this.typeRegistry = typeRegistry;
        this.gson = gson;
        this.snbtConfig = snbtConfig;

        this.writer = new NbtWriter(typeRegistry);
        this.reader = new NbtReader(typeRegistry);
    }

    /**
     * Writes the given root {@link NbtCompound} to a provided {@link DataOutput} stream.
     *
     * @param compound the NBT structure to write, contained within a {@link NbtCompound}.
     * @param output the stream to write to.
     * @throws IOException if any I/O error occurs.
     */
    public void toStream(@NonNull NbtCompound compound, @NonNull DataOutput output) throws IOException {
        this.writer.toStream(compound, output);
    }

    /**
     * Writes the given root {@link NbtCompound} to a {@link File} with no compression.
     *
     * @param compound the NBT structure to write, contained within a {@link NbtCompound}.
     * @param file the file to write to.
     * @throws IOException if any I/O error occurs.
     */
    public void toFile(@NonNull NbtCompound compound, @NonNull File file) throws IOException {
        this.toFile(compound, file, CompressionType.NONE);
    }

    /**
     * Writes the given root {@link NbtCompound} to a {@link File} using a certain {@link CompressionType}.
     *
     * @param compound the NBT structure to write, contained within a {@link NbtCompound}.
     * @param file the file to write to.
     * @param compression the compression to be applied.
     * @throws IOException if any I/O error occurs.
     */
    public void toFile(@NonNull NbtCompound compound, @NonNull File file, @NonNull CompressionType compression) throws IOException {
        @Cleanup BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
        @Cleanup DataOutputStream dos = null;

        switch (compression) {
            case NONE:
                dos = new DataOutputStream(bos);
                break;
            case GZIP:
                dos = new DataOutputStream(new GZIPOutputStream(bos));
                break;
            case ZLIB:
                dos = new DataOutputStream(new DeflaterOutputStream(bos));
        }

        this.toStream(compound, dos);
    }

    /**
     * Serializes the given root {@link NbtCompound} to a SNBT (Stringified NBT).
     *
     * @param compound the NBT structure to serialize to SNBT, contained within a {@link NbtCompound}.
     * @return the serialized SNBT string.
     */
    public String toSnbt(@NonNull NbtCompound compound) {
        return compound.toSnbt(0, this.typeRegistry, this.snbtConfig);
    }

    /**
     * Serializes the given root {@link NbtCompound} to a JSON {@link File}.
     *
     * @param compound the NBT structure to serialize to JSON, contained within a {@link NbtCompound}.
     * @param file the JSON file to write to.
     * @throws IOException if any I/O error occurs.
     */
    public void toJson(@NonNull NbtCompound compound, @NonNull File file) throws IOException {
        @Cleanup FileWriter writer = new FileWriter(file);

        gson.toJson(compound.toJson(0, this.typeRegistry), writer);
    }

    /**
     * Converts the given root {@link NbtCompound} to a {@code byte[]} array.
     *
     * @param compound the NBT structure to write, contained within a {@link NbtCompound}.
     * @return the resulting {@code byte[]} array.
     * @throws IOException if any I/O error occurs.
     */
    public byte[] toByteArray(@NonNull NbtCompound compound) throws IOException {
        @Cleanup ByteArrayOutputStream baos = new ByteArrayOutputStream();
        @Cleanup DataOutputStream w = new DataOutputStream(baos);

        this.toStream(compound, w);

        return baos.toByteArray();
    }

    /**
     * Converts the given root {@link NbtCompound} to a Base64 encoded string.
     *
     * @param compound the NBT structure to write, contained within a {@link NbtCompound}.
     * @return the resulting Base64 encoded string.
     * @throws IOException if any I/O error occurs.
     */
    public String toBase64(@NonNull NbtCompound compound) throws IOException {
        return new String(Base64.getEncoder().encode(this.toByteArray(compound)), StandardCharsets.UTF_8);
    }

    /**
     * Reads an NBT data structure (root {@link NbtCompound}) from a {@link DataInput} stream.
     *
     * @param input the stream to read from.
     * @return the root {@link NbtCompound} read from the stream.
     * @throws IOException if any I/O error occurs.
     */
    public NbtCompound fromStream(@NonNull DataInput input) throws IOException {
        return this.reader.fromStream(input);
    }

    /**
     * Reads an NBT data structure (root {@link NbtCompound}) from a {@link File}.
     *
     * @param file the file to read from.
     * @return the root {@link NbtCompound} read from the stream.
     * @throws IOException if any I/O error occurs.
     */
    public NbtCompound fromFile(@NonNull File file) throws IOException {
        @Cleanup BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        @Cleanup DataInputStream in = null;

        @Cleanup FileInputStream fis = new FileInputStream(file);
        switch (CompressionType.getCompression(fis)) {
            case NONE:
                in = new DataInputStream(bis);
                break;
            case GZIP:
                in = new DataInputStream(new GZIPInputStream(bis));
                break;
            case ZLIB:
                in = new DataInputStream(new InflaterInputStream(bis));
                break;
            default:
                throw new IllegalStateException("Illegal compression type. This should never happen.");
        }

        return this.fromStream(in);
    }

    /**
     * Deserializes an NBT data structure (root {@link NbtCompound}) from a JSON {@link File}.
     *
     * @param file the JSON file to read from.
     * @return the root {@link NbtCompound} deserialized from the JSON file.
     * @throws IOException if any I/O error occurs.
     */
    public NbtCompound fromJson(@NonNull File file) throws IOException {
        @Cleanup FileReader reader = new FileReader(file);

        return new NbtCompound().fromJson(gson.fromJson(reader, JsonObject.class), 0, this.typeRegistry);
    }

    /**
     * Reads an NBT data structure (root {@link NbtCompound}) from a {@code byte[]} array.
     *
     * @param bytes the {@code byte[]} array to read from.
     * @return the root {@link NbtCompound} read from the stream.
     * @throws IOException if any I/O error occurs.
     */
    public NbtCompound fromByteArray(@NonNull byte[] bytes) throws IOException {
        @Cleanup DataInputStream bais = new DataInputStream(new BufferedInputStream(new ByteArrayInputStream(bytes)));

        return fromStream(bais);
    }

    /**
     * Decodes an NBT data structure (root {@link NbtCompound}) from a Base64 encoded string.
     *
     * @param encoded the encoded Base64 string to decode.
     * @return the decoded root {@link NbtCompound}.
     * @throws IOException if any I/O error occurs.
     */
    public NbtCompound fromBase64(@NonNull String encoded) throws IOException {
        return fromByteArray(Base64.getDecoder().decode(encoded));
    }

    /**
     * Returns the {@link NbtTypeRegistry} currently in use by this instance.
     *
     * @return the {@link NbtTypeRegistry} currently in use by this instance.
     */
    public NbtTypeRegistry getTypeRegistry() {
        return typeRegistry;
    }

    /**
     * Sets the {@link NbtTypeRegistry} currently in use by this instance. Used to utilise custom-made tag types.
     *
     * @param typeRegistry the new {@link NbtTypeRegistry} to be set.
     */
    public void setTypeRegistry(@NonNull NbtTypeRegistry typeRegistry) {
        this.typeRegistry = typeRegistry;

        this.writer.setTypeRegistry(typeRegistry);
        this.reader.setTypeRegistry(typeRegistry);
    }

    /**
     * Returns the {@code Gson} currently in use by this instance.
     *
     * @return the {@code Gson} currently in use by this instance.
     */
    public Gson getGson() {
        return gson;
    }

    /**
     * Sets the {@code Gson} currently in use by this instance.
     *
     * @param gson the new {@code Gson} to be set.
     */
    public void setGson(@NonNull Gson gson) {
        this.gson = gson;
    }

    /**
     * Returns the {@link SnbtConfig} currently in use by this instance.
     *
     * @return the {@link SnbtConfig} currently in use by this instance.
     */
    public SnbtConfig getSnbtConfig() {
        return snbtConfig;
    }

    /**
     * Sets the {@link SnbtConfig} currently in use by this instance.
     *
     * @param snbtConfig the new {@link SnbtConfig} to be set.
     */
    public void setSnbtConfig(@NonNull SnbtConfig snbtConfig) {
        this.snbtConfig = snbtConfig;
    }
}
