package ru.spbstu.icst;

/**
 * This program implements an application to visualize some reductions of search problems using JavaFX.
 * Reductions which implemented in this application are the following:
 * 1. SAT -> 3SAT
 * 2. IS -> VC
 *
 * @author Ivan Kuznetsov
 * @version 0.0.1
 * @since 2022-05-20
 */
public class Main {
    public static void main(String[] args) {
        // Generate and run start screen
        StartScreen startScreen = new StartScreen();
        startScreen.run();
    }
}