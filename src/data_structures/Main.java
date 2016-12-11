package data_structures;

import java.util.Random;

/**
 * This is the main program of the Concurrency and Multithreading programming
 * assignment.
 */
public class Main {

    static final String CGL = "cgl";
    static final String CGT = "cgt";
    static final String FGL = "fgl";
    static final String FGT = "fgt";
    static final String LFL = "lfl";
    static final String LFT = "lft";

    /**
     * Deterministically computes a seed from the parameters.
     *
     * @param params
     *            the integer parameters from which to compute the seed
     * @return the seed
     */
    static long computeSeed(int... params) {
        long result = 0;
        for (int p : params) {
            result <<= 16;
            result |= p;
        }
        return result;
    }

    /**
     * Permutes an array in a predictable manner, based on a specific seed.
     *
     * @param array
     *            the array to permute.
     * @param seed
     *            the seed to use for the random number generator.
     */
    private static void permute(Integer[] array, long seed) {
        Random random = new Random(seed);

        for (int i = 0; i < array.length; i++) {
            int r = random.nextInt(array.length);
            int swapped = array[i];
            array[i] = array[r];
            array[r] = swapped;
        }
    }

    /**
     * Fills the specified <code>itemsToAdd</code> and
     * <code>itemsToRemove</code> arrays with pseudo-random numbers, based on
     * the specified seed. There resulting arrays will not contain duplicates.
     *
     * @param itemsToAdd
     *            array to be initialized with items to add
     * @param itemsToRemove
     *            array to be initialized with items to remove (the same items
     *            as <code>ItemsToAdd</code>, but in a different order)
     * @param seed
     *            the seed
     */
    private static void createWorkDataWithoutDuplicates(Integer[] itemsToAdd,
            Integer[] itemsToRemove, long seed) {
        for (int i = 0; i < itemsToAdd.length; i++) {
            itemsToAdd[i] = i;
            itemsToRemove[i] = i;
        }

        permute(itemsToAdd, seed);
        permute(itemsToRemove, seed + 1);
    }

    /**
     * Fills the specified <code>itemsToAdd</code> and
     * <code>itemsToRemove</code> arrays with pseudo-random numbers, based on
     * the specified seed. There resulting arrays may contain duplicates.
     *
     * @param itemsToAdd
     *            array to be initialized with items to add
     * @param itemsToRemove
     *            array to be initialized with items to remove (the same items
     *            as <code>ItemsToAdd</code>, but in a different order)
     * @param seed
     *            the seed
     */
    private static void createWorkDataWithDuplicates(Integer[] itemsToAdd,
            Integer[] itemsToRemove, long seed) {
        Random random = new Random(seed);

        for (int i = 0; i < itemsToAdd.length; i++) {
            int nextRandom = random.nextInt();
            itemsToAdd[i] = nextRandom;
            itemsToRemove[i] = nextRandom;
        }

        permute(itemsToRemove, seed + 1);
    }

    /**
     * Fills the specified <code>itemsToAdd</code> and
     * <code>itemsToRemove</code> arrays with pseudo-random numbers, based on
     * the specified seed. There resulting arrays may or may not contain
     * duplicates, depending on the <code>mayHaveDuplicates</code> parameter.
     *
     * @param itemsToAdd
     *            array to be initialized with items to add
     * @param itemsToRemove
     *            array to be initialized with items to remove (the same items
     *            as <code>ItemsToAdd</code>, but in a different order)
     * @param seed
     *            the seed
     * @param mayHaveDuplicates
     *            determines whether the results may contain duplicates
     *
     */
    private static void createWorkData(Integer[] itemsToAdd,
            Integer[] itemsToRemove, long seed, boolean mayHaveDuplicates) {
        if (mayHaveDuplicates) {
            createWorkDataWithDuplicates(itemsToAdd, itemsToRemove, seed);
        } else {
            createWorkDataWithoutDuplicates(itemsToAdd, itemsToRemove, seed);
        }
    }

    /**
     * This method is called when there was some error in the arguments, and
     * explains how the program should be invoked.
     */
    static void exitWithError() {
        System.out.println(
                "test_data_structures <data_structure> <nrThreads> <nrItems> <workTime> [debug]");
        System.out.println("  where:");
        System.out.printf("    <data_structure> in {%s, %s, %s, %s, %s, %s}\n",
                CGL, CGT, FGL, FGT, LFL, LFT);
        System.out.println("    <nrThreads> is a number > 0");
        System.out.println("    <nrItems> is a number > 0");
        System.out.println("    <workTime> is a number >= 0 (micro seconds)");
        // TODO: Remove before submitting - Testing purposes only
        System.out.println("    <data> either int for Integers or str for String");
        System.out.println("    <barrier> if false inter-operability." +
                "           of add() and remove() will be tested");
        System.out.println(
                "    [debug] can be omitted. If added as the last parameter,");
        System.out.println("            the data structure will be printed ");
        System.out.println(
                "            after adding and before removing the numbers.");
        System.exit(1);
    }

    public static void main(String[] args) {
        if (args.length < 5 || args.length > 7) {
            exitWithError();
        }

        String dataStructure = args[0];
        int nrThreads = Integer.parseInt(args[1]);
        if (nrThreads < 1) {
            exitWithError();
        }

        int nrItems = Integer.parseInt(args[2]);
        if (nrItems < 1) {
            exitWithError();
        }

        if (nrItems % nrThreads != 0) {
            System.out.println("nrItems should be divisible by nrThreads");
            System.exit(1);
        }

        int workTime = Integer.parseInt(args[3]);
        if (workTime < 0) {
            exitWithError();
        }
        // TODO: Remove before submitting - Testing purposes only
        String data = args[4];
        if (! "str".equals(data) && !"int".equals(data)) {
            exitWithError();
        }

        boolean debug = false;
        // TODO: Remove before submitting - Testing purposes only
        boolean barrier = true;
        if (args.length >= 6) {
            if (!"false".equals(args[5]) && !"true".equals(args[5]) ) {
                exitWithError();
            } else if (args[5].equalsIgnoreCase("false")) {
                barrier = false;
            }
        }
        // TODO: Remove before submitting - Testing purposes only
        if (args.length == 7) {
            if (args[6].equalsIgnoreCase("debug")) {
                debug = true;
            } else {
                System.out.println(
                        "last argument should be 'debug', or be omitted\n");
                System.exit(1);
            }
        }

        // Create the items to be added and deleted.
        long seed = computeSeed(nrThreads, nrItems, workTime);
        // TODO: Remove before submitting - Testing purposes only
        if (data.equals("int")) {
            Integer[] itemsToAdd = new Integer[nrItems];
            Integer[] itemsToRemove = new Integer[nrItems];

            RunData<Integer> run = new RunData<Integer>(dataStructure, nrItems,
                    nrThreads, itemsToAdd, itemsToRemove, workTime, debug, barrier);

            boolean mayHaveDuplicates = !dataStructure.equalsIgnoreCase(LFT);

            createWorkData(itemsToAdd, itemsToRemove, seed, mayHaveDuplicates);

            run.runDataStructure();
        } else {
            String[] itemsToAdd = new String[nrItems];
            String[] itemsToRemove = new String[nrItems];

            RunData<String> run = new RunData<String>(dataStructure, nrItems,
                    nrThreads, itemsToAdd, itemsToRemove, workTime, debug, barrier);

            boolean mayHaveDuplicates = !dataStructure.equalsIgnoreCase(LFT);

            Main2.createWorkData(itemsToAdd, itemsToRemove, seed, mayHaveDuplicates);

            run.runDataStructure();
        }

    }
}
