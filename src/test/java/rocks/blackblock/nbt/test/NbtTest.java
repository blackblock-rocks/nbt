package rocks.blackblock.nbt.test;

import rocks.blackblock.nbt.Nbt;
import rocks.blackblock.nbt.io.CompressionType;
import rocks.blackblock.nbt.elements.array.NbtByteArray;
import rocks.blackblock.nbt.elements.array.NbtIntArray;
import rocks.blackblock.nbt.elements.array.NbtLongArray;
import rocks.blackblock.nbt.elements.collection.NbtCompound;
import rocks.blackblock.nbt.elements.collection.NbtList;
import rocks.blackblock.nbt.elements.primitive.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * A simple demonstration of how the NBT library may be used.
 *
 * @author dewy
 */
public class NbtTest {
    // paths for the sample files
    private static final String SAMPLES_PATH = "samples/";
    private static final File STANDARD_SAMPLE = new File(SAMPLES_PATH + "sample.nbt");
    private static final File GZIP_SAMPLE = new File(SAMPLES_PATH + "samplegzip.nbt");
    private static final File ZLIB_SAMPLE = new File(SAMPLES_PATH + "samplezlib.nbt");
    private static final File JSON_SAMPLE = new File(SAMPLES_PATH + "sample.json");

    // instance of the Nbt class, globally used. use setTypeRegistry() to use custom-made tag types.
    private static final Nbt NBT = new Nbt();

    public static void main(String[] args) throws IOException {
        // creation of a root compound (think of it like a JSONObject in GSON)
        NbtCompound root = new NbtCompound("root");

        // primitive NBT tags (tags contained inside compounds MUST have unique names)
        root.put(new NbtByte("byte", 45));
        root.put(new NbtShort("short", 345));
        root.put(new NbtInt("int", -981735));
        root.put(new NbtLong("long", -398423290489L));

        // more primitives, using the specialized put methods
        root.putFloat("float", 12.5F);
        root.putDouble("double", -19040912.1235);

        // putting a previously unnamed tag.
        root.put("string", new NbtString("https://dewy.dev"));

        // array NBT tags
        root.put(new NbtByteArray("bytes", new byte[] {0, -124, 13, -6, Byte.MAX_VALUE}));
        root.put(new NbtIntArray("ints", new int[] {0, -1348193, 817519, Integer.MIN_VALUE, 4}));

        // constructing array tags with List<> objects
        List<Long> longList = new ArrayList<>();
        longList.add(12490812L);
        longList.add(903814091904L);
        longList.add(-3L);
        longList.add(Long.MIN_VALUE);
        longList.add(Long.MAX_VALUE);
        longList.add(0L);

        root.put(new NbtLongArray("longs", longList));

        // compound and list tags
        NbtCompound subCompound = new NbtCompound("sub");
        NbtList<NbtCompound> doubles = new NbtList<>("listmoment");

        for (int i = 0; i < 1776; i++) {
            NbtCompound tmp = new NbtCompound("tmp" + i);

            tmp.put(new NbtDouble("i", i));
            tmp.put(new NbtDouble("n", i / 1348.1));

            doubles.add(tmp);
        }

        subCompound.put(doubles);
        root.put(subCompound);

        // compound containing an empty compound
        NbtList<NbtCompound> compounds = new NbtList<>("compounds");
        compounds.add(new NbtCompound());
        root.put(compounds);

        // list containing an empty list of ints
        NbtList<NbtList<NbtInt>> listsOfInts = new NbtList<>("listofints");
        listsOfInts.add(new NbtList<>());
        root.putList("listofints", listsOfInts.getValue());

        // writing to file (no compression type provided for no compression)
        NBT.toFile(root, STANDARD_SAMPLE);
        NBT.toFile(root, GZIP_SAMPLE, CompressionType.GZIP);
        NBT.toFile(root, ZLIB_SAMPLE, CompressionType.ZLIB);

        // displaying a Base64 representation
        System.out.println(NBT.toBase64(root));

        // reading from file
        NbtCompound clone = NBT.fromFile(ZLIB_SAMPLE);
        System.out.println(clone.equals(root));

        // retrieving data from the read compound
        System.out.println(clone.getName());
        System.out.println("Be sure to visit " + clone.getString("string").getValue() + " c:");

        // nbt to json and back: see readme for NBT JSON format documentation
        jsonTest();

        // displaying as SNBT
        System.out.println(root);
    }

    private static void jsonTest() throws IOException {
        NbtCompound root = new NbtCompound("root");

        root.putInt("primitive", 3);
        root.putIntArray("array", new int[]{0, 1, 2, 3});

        List<NbtString> list = new LinkedList<>();
        list.add(new NbtString("duck"));
        list.add(new NbtString("goose"));

        root.putList("list", list);
        root.put("compound", new NbtCompound());

        NBT.toJson(root, JSON_SAMPLE);
        System.out.println(NBT.fromJson(JSON_SAMPLE).equals(root));
    }
}
