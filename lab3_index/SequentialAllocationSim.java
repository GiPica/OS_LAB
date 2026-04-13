package lab3_index;

import java.util.*;

public class SequentialAllocationSim {

    private int diskSize;
    private String[] disk;
    private boolean[] freeBlocks;
    private Map<String, int[]> directory; // filename -> [start, length]
    private int fileCounter;

    public SequentialAllocationSim(int diskSize) {
        this.diskSize = diskSize;
        this.disk = new String[diskSize];
        this.freeBlocks = new boolean[diskSize];
        this.directory = new LinkedHashMap<>();
        this.fileCounter = 1;

        // All blocks are free at the start
        Arrays.fill(freeBlocks, true);
    }

    // Find a contiguous gap big enough for the file
    private int findContiguousSpace(int length) {
        for (int i = 0; i <= diskSize - length; i++) {
            boolean fits = true;
            for (int j = 0; j < length; j++) {
                if (!freeBlocks[i + j]) {
                    fits = false;
                    break;
                }
            }
            if (fits) return i;
        }
        return -1; // No space found
    }

    // Create a new file
    public void createFile(int length) {
        int startBlock = findContiguousSpace(length);

        if (startBlock == -1) {
            System.out.println("\n[!] Error: No contiguous gap of " + length + " blocks available.");
            return;
        }

        String filename = "file" + fileCounter++;

        // Allocate blocks on disk
        for (int i = startBlock; i < startBlock + length; i++) {
            disk[i] = "Data of '" + filename + "' (Block " + (i - startBlock) + ")";
            freeBlocks[i] = false;
        }

        // Store only start and length in directory
        directory.put(filename, new int[]{startBlock, length});

        System.out.println("\n[SUCCESS] '" + filename + "' allocated sequentially.");
        System.out.println("   Start Block : " + startBlock);
        System.out.println("   Length      : " + length);
        System.out.print("   Blocks Used : ");
        for (int i = startBlock; i < startBlock + length; i++) {
            System.out.print(i + " ");
        }
        System.out.println();
    }

    // Delete a file and free its blocks
    public void deleteFile(String filename) {
        if (!directory.containsKey(filename)) {
            System.out.println("\n[!] Error: File '" + filename + "' not found.");
            return;
        }

        int[] info = directory.remove(filename);
        int start = info[0];
        int length = info[1];

        for (int i = start; i < start + length; i++) {
            disk[i] = null;
            freeBlocks[i] = true;
        }

        System.out.println("\n[DELETED] '" + filename + "' removed. Blocks " 
            + start + " to " + (start + length - 1) + " are now free.");
    }

    // Read a specific block from a file (direct access)
    public void readFileDirectly(Scanner scanner) {
        if (directory.isEmpty()) {
            System.out.println("\n[!] No files exist.");
            return;
        }

        // Show directory table
        System.out.println("\n--- Directory (Sequential) ---");
        System.out.printf("%-12s | %-6s | %-6s%n", "Filename", "Start", "Length");
        System.out.println("-------------------------------");
        for (Map.Entry<String, int[]> entry : directory.entrySet()) {
            System.out.printf("%-12s | %-6d | %-6d%n",
                entry.getKey(), entry.getValue()[0], entry.getValue()[1]);
        }

        System.out.print("\nEnter filename to read: ");
        String fname = scanner.nextLine().trim();

        if (!directory.containsKey(fname)) {
            System.out.println("[!] File not found.");
            return;
        }

        int[] info = directory.get(fname);
        int start = info[0];
        int length = info[1];

        System.out.print("Enter relative block to read (0 to " + (length - 1) + "): ");
        try {
            int offset = Integer.parseInt(scanner.nextLine().trim());
            if (offset >= 0 && offset < length) {
                int actualAddr = start + offset;
                System.out.println("\n[READ] Accessing Block " + actualAddr 
                    + " (Start " + start + " + Offset " + offset + ")");
                System.out.println("Data of Block " + offset + " of " + fname 
                    + " is stored at Physical Block: " + actualAddr);
            } else {
                System.out.println("[!] Offset out of bounds.");
            }
        } catch (NumberFormatException e) {
            System.out.println("[!] Invalid input.");
        }
    }

    // Show visual disk map
    public void showDiskMap() {
        int freeCount = 0;
        for (boolean b : freeBlocks) if (b) freeCount++;

        System.out.println("\n" + "=".repeat(40));
        System.out.println("DISK MAP (Sequential) — " + freeCount + "/" + diskSize + " free");
        System.out.println("=".repeat(40));

        for (int i = 0; i < diskSize; i += 5) {
            int end = Math.min(i + 5, diskSize);

            // Print addresses
            System.out.print("  Addr : ");
            for (int j = i; j < end; j++) {
                System.out.printf("%02d  ", j);
            }
            System.out.println();

            // Print block status
            System.out.print("  Disk : ");
            for (int j = i; j < end; j++) {
                System.out.print(freeBlocks[j] ? "[ . ] " : "[ D ] ");
            }
            System.out.println();
            System.out.println("  " + "-".repeat(35));
        }
        System.out.println("  (. = Free,  D = Occupied)");
    }

    // Main menu
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SequentialAllocationSim sim = new SequentialAllocationSim(20);

        System.out.println("\nSequential File Allocation Simulator");
        System.out.println("Disk Size of 20 is initialized.");

        while (true) {
            System.out.println("\n1. Create File");
            System.out.println("2. Read File (Direct Access)");
            System.out.println("3. Show Disk Map");
            System.out.println("4. Delete File");
            System.out.println("5. Exit");
            System.out.print("Choice: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    System.out.print("File length (number of blocks): ");
                    try {
                        int length = Integer.parseInt(scanner.nextLine().trim());
                        sim.createFile(length);
                    } catch (NumberFormatException e) {
                        System.out.println("[!] Invalid input.");
                    }
                    break;

                case "2":
                    sim.readFileDirectly(scanner);
                    break;

                case "3":
                    sim.showDiskMap();
                    break;

                case "4":
                    System.out.print("Enter filename to delete (e.g., file1): ");
                    String fname = scanner.nextLine().trim();
                    sim.deleteFile(fname);
                    break;

                case "5":
                    System.out.println("Exiting...");
                    scanner.close();
                    return;

                default:
                    System.out.println("[!] Invalid choice. Please enter 1-5.");
            }
        }
    }
}