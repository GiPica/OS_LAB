import java.util.Scanner;

public class firstfit_allocation {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // LINE 1: INPUT blocks, processes
        System.out.print("Enter number of memory blocks: ");
        int blocks = scanner.nextInt();

        System.out.print("Enter number of processes: ");
        int processes = scanner.nextInt();

        // LINE 2: DECLARE block_size[blocks]
        int[] block_size = new int[blocks];

        // LINE 3: DECLARE process_size[processes]
        int[] process_size = new int[processes];

        // LINE 4: DECLARE allocation[processes]
        // LINE 5: INITIALIZE allocation with -1
        int[] allocation = new int[processes];
        for (int i = 0; i < processes; i++) {
            allocation[i] = -1; // -1 means Not Allocated
        }

        // Input block sizes
        System.out.println("\nEnter size of each memory block (KB):");
        for (int i = 0; i < blocks; i++) {
            System.out.print("Block " + (i + 1) + ": ");
            block_size[i] = scanner.nextInt();
        }

        // Input process sizes
        System.out.println("\nEnter size of each process (KB):");
        for (int i = 0; i < processes; i++) {
            System.out.print("Process " + (i + 1) + ": ");
            process_size[i] = scanner.nextInt();
        }

        // ============================================
        // FIRST FIT ALGORITHM (Lines 6 to 14)
        // ============================================

        // LINE 6: FOR i = 0 TO processes - 1
        for (int i = 0; i < processes; i++) {

            // LINE 7: FOR j = 0 TO blocks - 1
            for (int j = 0; j < blocks; j++) {

                // LINE 8: IF block_size[j] >= process_size[i] THEN
                if (block_size[j] >= process_size[i]) {

                    // LINE 9: SET allocation[i] = j
                    allocation[i] = j;

                    // LINE 10: SET block_size[j] = block_size[j] - process_size[i]
                    block_size[j] = block_size[j] - process_size[i];

                    // LINE 11: BREAK
                    break;

                } // LINE 12: END IF

            } // LINE 13: END FOR (inner)

        } // LINE 14: END FOR (outer)

        // ============================================
        // LINE 15: PRINT Results Table
        // ============================================
        System.out.println("\n========== FIRST FIT RESULTS ==========");
        System.out.printf("%-15s %-15s %-20s%n",
                "Process No.", "Size (KB)", "Block Assigned");
        System.out.println("------------------------------------------");

        for (int i = 0; i < processes; i++) {
            if (allocation[i] != -1) {
                System.out.printf("%-15d %-15d Block %-10d%n",
                        i + 1,
                        process_size[i],
                        allocation[i] + 1); // +1 for human-readable block number
            } else {
                System.out.printf("%-15d %-15d %-20s%n",
                        i + 1,
                        process_size[i],
                        "Not Allocated");
            }
        }

        // Bonus: Show remaining memory in each block
        System.out.println("\n========== MEMORY BLOCK STATUS ==========");
        System.out.printf("%-15s %-15s%n", "Block No.", "Remaining (KB)");
        System.out.println("------------------------------");
        for (int i = 0; i < blocks; i++) {
            System.out.printf("%-15d %-15d%n", i + 1, block_size[i]);
        }

        scanner.close();
    }
}