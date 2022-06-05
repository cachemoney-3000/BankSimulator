/* Name: Joshua Samontanez
Course: CNT 4714 Summer 2022
Assignment title: Project 1 – Synchronized, Cooperating Threads Under Locking
Due Date: June 5, 2022
*/

package Project_1;

import java.io.BufferedWriter;
import java.util.Random;

public class Depositor implements Runnable{
    private static final Random rand = new Random();
    private final Buffer currentBalance; // Reference to shared object
    private final String threadName;
    BufferedWriter transactionLogs;
    BufferedWriter flaggedTransactionLogs;

    // Constructor
    public Depositor (String threadName, Buffer balance, BufferedWriter transactionLogs, BufferedWriter flaggedTransactionLogs){
        currentBalance = balance;
        this.threadName = threadName;
        this.transactionLogs = transactionLogs;
        this.flaggedTransactionLogs = flaggedTransactionLogs;
    }

    @Override
    public void run() {
        while (true) {
            try {
                // Sleep the thread for a few milliseconds
                Thread.sleep(rand.nextInt(150));
                // Do a deposit transaction, range is between $1 to $500 inclusive
                currentBalance.deposit(threadName, rand.nextInt(500) + 1, transactionLogs, flaggedTransactionLogs);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
