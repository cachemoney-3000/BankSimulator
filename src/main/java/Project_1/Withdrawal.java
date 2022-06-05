/* Name: Joshua Samontanez
Course: CNT 4714 Summer 2022
Assignment title: Project 1 – Synchronized, Cooperating Threads Under Locking
Due Date: June 5, 2022
*/

package Project_1;

import java.io.BufferedWriter;
import java.util.Random;

public class Withdrawal implements Runnable{
    private final Buffer sharedBalance; // Reference to shared object
    private static final Random rand = new Random();
    private final String threadName;
    BufferedWriter transactionLogs;
    BufferedWriter flaggedTransactionLogs;

    // Constructor
    public Withdrawal(String threadName, Buffer balance, BufferedWriter transactionLogs, BufferedWriter flaggedTransactionLogs) {
        sharedBalance = balance;
        this.threadName = threadName;
        this.transactionLogs = transactionLogs;
        this.flaggedTransactionLogs = flaggedTransactionLogs;
    }

    @Override
    public void run() {
        while (true) {
            try {
                // Sleep the thread for a few milliseconds
                Thread.sleep(rand.nextInt(50));
                // Do a withdrawal transaction, range is between $1 - $99 inclusive
                sharedBalance.withdraw(threadName, rand.nextInt(99) + 1, transactionLogs, flaggedTransactionLogs);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
