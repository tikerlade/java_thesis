package ru.spbstu.icst;

import ru.spbstu.icst.exceptions.InputException;
import ru.spbstu.icst.reductions.*;

import java.util.List;
import java.util.Scanner;


/**
 * Interaction using CLI is provided using this class.
 * Instance of class must be created first. After this {@link Solver#start()}
 * should be called to start interacting with user.
 */
public class Solver {
    // TODO provide gradle cli Task to run without GUI
    /**
     * Reductions which could be started will be stored here in constructor.
     */
    private final List<Reduction> reductions;

    /**
     * Util to interact with user through CLI.
     */
    private final Scanner scanner = new Scanner(System.in);

    public Solver() {
        reductions = List.of(new CnfTo3CnfReduction(), new IsToVc(), new IsToClique());
    }

    /**
     * Starts interacting with user. After reduction is done waits for continue decision
     * (Y - perform new reduction / N -  close program).
     * @throws Exception if user input wrong formatted data.
     */
    public void start() throws Exception {
        String userContinue = "Y";

        while (userContinue.equals("Y")) {
            // Intro messages
            System.out.println("Hello, this program will complete reduction of your problem to other NPC problem with your data.");
            System.out.println("Please select, which one of the following reductions you would like to complete:");

            // Print all reductions we have
            for (int i = 0; i < reductions.size(); i++) {
                Reduction reduction = reductions.get(i);
                System.out.println(i + ". " + reduction.toString());
            }

            // Read user data
            int userChoice = scanner.nextInt();

            // Check validaty of number just read
            if (userChoice < 0 || userChoice > reductions.size()) {
                throw new InputException("Your choice is out of bounds.");
            }

            Reduction reduction = reductions.get(userChoice);
            ProgramMode programMode = this.readProgramMode();

            // If input was correct - start reduction with given mode
            reduction.start(programMode);

            // Does user want to continue
            System.out.print("Do you want to continue (Y/N): ");
            userContinue = scanner.next();
        }
    }

    /**
     * Reads from user {@link ProgramMode} in which reduction will be performed.
     * Gives information which data from user will be required.
     * @return mode in which reduction will be performed.
     *
     * @see ProgramMode
     */
    public ProgramMode readProgramMode() {
        // Let's understand in which mode user want to run program
        System.out.println("\nBefore you input data for problem please choose mode in which you will run program:");

        for (int i = 0; i < ProgramMode.numModes; i++) {
            String formattedEntry = String.format("%d. %s", i, ProgramMode.values()[i]);
            System.out.println(formattedEntry);
        }

        int mode = scanner.nextInt();
        // Wrong input
        if (mode < 0 || mode > ProgramMode.numModes) {
            System.out.println("Wrong mode number");
        }

        // Convert user input from int to ProgramMode variable
        return ProgramMode.values()[mode];
    }
}