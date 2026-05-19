package lab7_fifo;

import java.util.Scanner;

public class fifo_page {

    @SuppressWarnings("ConvertToTryWithResources")
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // input the capacity of frames
        int capacity = 0;
        while (true) {
            System.out.print("Enter number of frames (capacity): ");
            if (!sc.hasNextInt()) {
                System.out.println("[!] Invalid input. Please enter a whole number.");
                sc.next(); //error handling for non integer input
                continue;
            }
            capacity = sc.nextInt();
            if (capacity <= 0) {
                System.out.println("[!] Number of frames must be greater than 0.");
                continue; //error handling for negative or zero input 
            }
            break; // valid, exit loop
        }

        // input the num of pages 
        int n = 0;
        while (true) {
            System.out.print("Enter number of pages in reference string: ");
            if (!sc.hasNextInt()) {
                System.out.println("[!] Invalid input. Please enter a whole number.");
                sc.next();
                continue;
            }
            n = sc.nextInt();
            if (n <= 0) {
                System.out.println("[!] Number of pages must be greater than 0.");
                continue; //error handling for negative or zero input
            }
            break;
        }

        // input the pages 
        int[] reference_string = new int[n];
        System.out.println("Enter the page reference string (" + n + " pages, must be >= 0):");
        for (int i = 0; i < n; i++) {
            while (true) {
                System.out.print("Page " + (i + 1) + ": ");
                if (!sc.hasNextInt()) {
                    System.out.println("[!] Invalid input. Please enter a whole number.");
                    sc.next();
                    continue;
                }
                int page = sc.nextInt();
                if (page < 0) {
                    System.out.println("[!] Page number cannot be negative.");
                    continue;
                }
                reference_string[i] = page;
                break;
            }
        }

        // LINE 3: DECLARE frames[] , how many slots
        int[] frames = new int[capacity];

        // LINE 4: DECLARE page_faults = 0 , to count how many page faults occur 
        int page_faults = 0;

        // LINE 5: DECLARE pointer = 0 , to keep track of the next frame to replace
        int pointer = 0;

        // LINE 6: INITIALIZE frames with -1 , to show all frames are empty
        for (int i = 0; i < capacity; i++) {
            frames[i] = -1;
        }

        System.out.println("\nFIFO Page Replacement");
        System.out.println("---------------------");

        // LINE 7: FOR each page IN reference_string
        for (int page : reference_string) {

            // LINE 8: SET page_found = false
            boolean page_found = false;

            // LINE 9: FOR i = 0 TO capacity - 1
            for (int i = 0; i < capacity; i++) {

                // LINE 10: IF frames[i] == page THEN
                if (frames[i] == page) {

                    // LINE 11: SET page_found = true
                    page_found = true;

                    break;

                } 

            } 

            // LINE 15: IF page_found == false THEN
            if (page_found == false) {

                // LINE 16: SET frames[pointer] = page
                frames[pointer] = page;

                // LINE 17: SET pointer = (pointer + 1) % capacity
                pointer = (pointer + 1) % capacity;

                // LINE 18: SET page_faults = page_faults + 1
                page_faults = page_faults + 1;

                // LINE 19: PRINT "Page Fault occurred"
                System.out.print("Page " + page + " --> Frames: ");
                for (int i = 0; i < capacity; i++) {
                    if (frames[i] == -1)
                        System.out.print("[ - ] ");
                    else
                        System.out.print("[ " + frames[i] + " ] ");
                }
                System.out.println("<-- Page Fault");

            } else {

                // LINE 21: PRINT "Page Hit"
                System.out.print("Page " + page + " --> Frames: ");
                for (int i = 0; i < capacity; i++) {
                    if (frames[i] == -1)
                        System.out.print("[ - ] ");
                    else
                        System.out.print("[ " + frames[i] + " ] ");
                }
                System.out.println("<-- Page Hit");

            } 

        } 

        // LINE 24: PRINT Total Page Faults
        System.out.println("---------------------");
        System.out.println("Total Page Faults : " + page_faults);

        // LINE 25: PRINT Total Page Hits
        System.out.println("Total Page Hits   : " + (n - page_faults));

        sc.close();
    }
}