package lab3_index;

import java.util.*;

public class lab3_index {

    private int diskSize;
    private Object[] disk; // null = free, List<Integer> = index block, String = data block
    private Map<String, Integer> directory; // filename -> index block address
    private Set<Integer> freeBlocks;
    private int fileCounter;
    private Random random;

    public lab3_index(int diskSize) {
        this.diskSize = diskSize;
        this.disk = new Object[diskSize];
        this.directory = new LinkedHashMap<>();
        this.freeBlocks = new HashSet<>();
        this.fileCounter = 1;
        this.random = new Random();

        for (int i = 0; i < diskSize; i++) {
            freeBlocks.add(i);
        }
    }

    // -------------------------------------------------------
    // Helper: Randomly allocate 'count' free blocks
    // -------------------------------------------------------
    private List<Integer> getFreeBlocks(int count) {
        if (freeBlocks.size() < count)
            return null;

        List<Integer> available = new ArrayList<>(freeBlocks);
        Collections.shuffle(available, random);
        List<Integer> allocated = available.subList(0, count);

        // Make a copy since subList is a view
        List<Integer> result = new ArrayList<>(allocated);
        freeBlocks.removeAll(result);
        return result;
    }

    // -------------------------------------------------------
    // Option 1: Create a file
    // -------------------------------------------------------
    public void createFile(int numBlocks) {
        int totalNeeded = numBlocks + 1; // 1 index block + N data blocks
        List<Integer> blocks = getFreeBlocks(totalNeeded);

        if (blocks == null) {
            System.out.println("\n[!] Disk full. Need " + totalNeeded +
                    " blocks (1 index + " + numBlocks + " data).");
            return;
        }

        String filename = "file" + fileCounter++;

        int indexAddr = blocks.get(0);
        List<Integer> dataAddrs = new ArrayList<>(blocks.subList(1, blocks.size()));

        // Store index block: contains list of data block addresses
        disk[indexAddr] = dataAddrs;

        // Store data in each data block
        for (int addr : dataAddrs) {
            disk[addr] = "Data of '" + filename + "'";
        }

        // Register in directory
        directory.put(filename, indexAddr);

        System.out.println("\n    '" + filename + "' successfully created.");
        System.out.println("    Index Block : Block " + indexAddr);
        System.out.print("    Data Blocks : [");
        for (int i = 0; i < dataAddrs.size(); i++) {
            System.out.print(dataAddrs.get(i));
            if (i < dataAddrs.size() - 1)
                System.out.print(", ");
        }
        System.out.println("]");
    }

    // -------------------------------------------------------
    // Option 2: Read an index block
    // -------------------------------------------------------
    @SuppressWarnings("unchecked")
    public void readIndexBlock(Scanner sc) {
        int indexAddr;

        // Validate index block address input
        while (true) {
            System.out.print("Enter index block to be read: ");
            try {
                indexAddr = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("    [!] Invalid input. Enter a number.");
                continue;
            }

            if (indexAddr < 0 || indexAddr >= diskSize) {
                System.out.println("    [!] Invalid index block. Must be between 0 and " +
                        (diskSize - 1) + ". Try again.");
                continue;
            }

            if (!(disk[indexAddr] instanceof List)) {
                System.out.println("    [!] Block " + indexAddr +
                        " is not an index block. Try again.");
                continue;
            }

            break;
        }

        List<Integer> indexBlock = (List<Integer>) disk[indexAddr];
        int numDataBlocks = indexBlock.size();

        System.out.print("    Index Block " + indexAddr + " (array of pointers): [");
        for (int i = 0; i < indexBlock.size(); i++) {
            System.out.print(indexBlock.get(i));
            if (i < indexBlock.size() - 1)
                System.out.print(", ");
        }
        System.out.println("]");

        // Validate position input
        int position;
        while (true) {
            System.out.print("Enter block position to be read (1-" + numDataBlocks + "): ");
            try {
                position = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("    [!] Invalid input. Enter a number.");
                continue;
            }

            if (position < 1 || position > numDataBlocks) {
                System.out.println("    [!] Invalid position. Must be between 1 and " +
                        numDataBlocks + ". Try again.");
                continue;
            }

            break;
        }

        int targetAddr = indexBlock.get(position - 1);
        System.out.printf("%n    Entry %d -> Block %02d: %s%n", position, targetAddr, disk[targetAddr]);
    }

    // -------------------------------------------------------
    // Option 3: Show disk map
    // -------------------------------------------------------
    public void showDiskMap() {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("DISK MAP  -  " + freeBlocks.size() + "/" + diskSize + " blocks free");
        System.out.println("=".repeat(40));

        // Build display labels for each block
        String[] display = new String[diskSize];
        for (int i = 0; i < diskSize; i++) {
            if (disk[i] == null) {
                display[i] = "[ . ]";
            } else if (disk[i] instanceof List) {
                display[i] = String.format("[I:%02d]", i);
            } else {
                display[i] = "[ D ]";
            }
        }

        // Print in rows of 5
        for (int start = 0; start < diskSize; start += 5) {
            int end = Math.min(start + 5, diskSize);

            // Print addresses
            System.out.print("  Addr : ");
            for (int i = start; i < end; i++) {
                System.out.printf("%02d", i);
                if (i < end - 1)
                    System.out.print("  ");
            }
            System.out.println();

            // Print block statuses
            System.out.print("  Disk : ");
            for (int i = start; i < end; i++) {
                System.out.print(display[i]);
                if (i < end - 1)
                    System.out.print(" ");
            }
            System.out.println();
            System.out.println("  " + "-".repeat(36));
        }
    }

    // -------------------------------------------------------
    // Main
    // -------------------------------------------------------
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("=".repeat(39));
        System.out.println("   Indexed File Allocation Simulator");
        System.out.println("=".repeat(39));

        int dSize;
        System.out.print("Enter disk size (number of blocks): ");
        try {
            dSize = Integer.parseInt(sc.nextLine().trim());
            if (dSize < 2)
                throw new NumberFormatException();
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Defaulting to 20 blocks.");
            dSize = 20;
        }

        lab3_index sim = new lab3_index(dSize);

        while (true) {
            System.out.println("\n--- MENU ---");
            System.out.println("1. Create File");
            System.out.println("2. Read Index Block");
            System.out.println("3. Disk Map");
            System.out.println("4. Exit");
            System.out.print("\nEnter choice: ");

            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1":
                    System.out.print("Enter number of data blocks: ");
                    try {
                        int size = Integer.parseInt(sc.nextLine().trim());
                        if (size < 1)
                            throw new NumberFormatException();
                        sim.createFile(size);
                    } catch (NumberFormatException e) {
                        System.out.println("  [!] Invalid block count.");
                    }
                    break;

                case "2":
                    sim.readIndexBlock(sc);
                    break;

                case "3":
                    sim.showDiskMap();
                    break;

                case "4":
                    System.out.println("\nShutting down simulator. Goodbye!");
                    sc.close();
                    return;

                default:
                    System.out.println("  [!] Invalid option. Please choose 1-4.");
            }
        }
    }
}