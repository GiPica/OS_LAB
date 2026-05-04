import java.util.Scanner;

public class firstfit_allocation {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // line 1 : we will ask user to input number of memory blocks and processes
        System.out.print("Enter number of memory blocks: ");
        int blocks = scanner.nextInt();

        System.out.print("Enter number of processes: ");
        int processes = scanner.nextInt();

        // line 2 : we will declare how much memory does a memory block has 
        int[] block_size = new int[blocks];

        // line 3 : we will declare how much memory does a process needs
        int[] process_size = new int[processes];

        // line 4 : we will declare the allocation array
        // line 5 : we will initialize the allocation array with -1 (empty)
        int[] allocation = new int[processes];
        for (int i = 0; i < processes; i++) {
            allocation[i] = -1; // -1 means not allocated yet
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

        // First Fit Allocation Algorithm 

        // line 6 : outer loop that goes thru each process 
        for (int i = 0; i < processes; i++) {

            // line 7 : inner loop that scans each block to find the first fit for the current process
            for (int j = 0; j < blocks; j++) {

                // line 8 : IF block_size[j] >= process_size[i] 
                if (block_size[j] >= process_size[i]) {

                    // line 9 : SET allocation[i] = j (successfully allocated the process to the block)
                    allocation[i] = j;

                    // line 10: SET block_size[j] = block_size[j] - process_size[i] (remaining memory in block)
                    block_size[j] = block_size[j] - process_size[i];

                    // line 11: BREAK
                    break;

                } 
            } 
        } 

        // line 15 : print results table

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

        // showcase remaining block space after allocation
        
        System.out.println("\n========== MEMORY BLOCK STATUS ==========");
        System.out.printf("%-15s %-15s%n", "Block No.", "Remaining (KB)");
        System.out.println("------------------------------");
        for (int i = 0; i < blocks; i++) {
            System.out.printf("%-15d %-15d%n", i + 1, block_size[i]);
        }

        scanner.close();
    }
}