/* Name: Joshua Samontanez
Course: CNT 4714 Summer 2022
Assignment title: Project 1 – Synchronized, Cooperating Threads Under Locking
Due Date: June 5, 2022
*/

package Project_1;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BankSimulator {
    public static void main(String[] args) throws IOException {
        // Create new thread pool with 15 threads
        ExecutorService application = Executors.newFixedThreadPool(15);
        // Create an un-synchronized buffer to store the balance
        Buffer sharedBalance = new SynchronizedBuffer();

        BankSimulator sim = new BankSimulator();

        // Make a file for all the transactions and flagged transactions
        BufferedWriter transactionLogs = new BufferedWriter(new FileWriter("src/main/java/Project_1/text_outputs/transaction_logs.txt"));
        BufferedWriter flaggedTransactionLogs = new BufferedWriter(new FileWriter("src/main/java/Project_1/text_outputs/flaggedTransaction_logs.txt"));

        try {
            // Print the header for the two files
            sim.printHeader(transactionLogs, flaggedTransactionLogs);
            // Execute the threads
            sim.executeThreads(application, sharedBalance, transactionLogs, flaggedTransactionLogs);
            // terminate application when threads end
            application.shutdown();
        } catch (IOException f) {
            f.printStackTrace();
        }
    }

    private void printHeader(BufferedWriter transactionLogs, BufferedWriter flaggedTransactionLogs) throws IOException {
        // Header for transactionLogs
        String header = String.format("%-40s%s\t\t\t%s%n%-40s%s%n",
                "Deposit Agents", "Withdrawal Agents", "      Balance",
                "--------------", "---------------\t\t\t\t   ------------");
        System.out.print(header);
        // Clear the stuff already written in the text file
        transactionLogs.flush();
        // Prints out the header for transactionLogs
        transactionLogs.append(header);

        // Header for flagged transaction logs
        String format = "%-20s %-20s %-10s\t\t %-10s %n";
        String header1 = String.format(format,
                "Actions", "Agent", "Flagged Value", "Timestamp");
        String lines = String.format(format,
                "-----------------", "---------------", "----------", "-----------------");
        // Clear the stuff written in the text file
        flaggedTransactionLogs.flush();
        // Print out the header for the flagged transaction logs
        flaggedTransactionLogs.append("Flagged Transactions: [Withdrawal of more than $75.00 or Deposit of more than $350.00]\n");
        flaggedTransactionLogs.append(header1);
        flaggedTransactionLogs.append(lines);
    }

    private void executeThreads(ExecutorService application, Buffer sharedBalance, BufferedWriter transactionLogs, BufferedWriter flaggedTransactionLogs) {
        // Five depositor agents
        Depositor thread1 = new Depositor("Agent DT1", sharedBalance, transactionLogs, flaggedTransactionLogs);
        Depositor thread2 = new Depositor("Agent DT2", sharedBalance, transactionLogs, flaggedTransactionLogs);
        Depositor thread3 = new Depositor("Agent DT3", sharedBalance, transactionLogs, flaggedTransactionLogs);
        Depositor thread4 = new Depositor("Agent DT4", sharedBalance, transactionLogs, flaggedTransactionLogs);
        Depositor thread5 = new Depositor("Agent DT5", sharedBalance, transactionLogs, flaggedTransactionLogs);
        // Ten withdrawal agents
        Withdrawal thread6 = new Withdrawal("Agent WT1", sharedBalance, transactionLogs, flaggedTransactionLogs);
        Withdrawal thread7 = new Withdrawal("Agent WT2", sharedBalance, transactionLogs, flaggedTransactionLogs);
        Withdrawal thread8 = new Withdrawal("Agent WT3", sharedBalance, transactionLogs, flaggedTransactionLogs);
        Withdrawal thread9 = new Withdrawal("Agent WT4", sharedBalance, transactionLogs, flaggedTransactionLogs);
        Withdrawal thread10 = new Withdrawal("Agent WT5", sharedBalance, transactionLogs, flaggedTransactionLogs);
        Withdrawal thread11 = new Withdrawal("Agent WT6", sharedBalance, transactionLogs, flaggedTransactionLogs);
        Withdrawal thread12 = new Withdrawal("Agent WT7", sharedBalance, transactionLogs, flaggedTransactionLogs);
        Withdrawal thread13 = new Withdrawal("Agent WT8", sharedBalance, transactionLogs, flaggedTransactionLogs);
        Withdrawal thread14 = new Withdrawal("Agent WT9", sharedBalance, transactionLogs, flaggedTransactionLogs);
        Withdrawal thread15 = new Withdrawal("Agent WT10", sharedBalance, transactionLogs, flaggedTransactionLogs);

        // Try to start the threads
        try {
            application.execute(thread1);
            application.execute(thread2);
            application.execute(thread3);
            application.execute(thread4);
            application.execute(thread5);
            application.execute(thread6);
            application.execute(thread7);
            application.execute(thread8);
            application.execute(thread9);
            application.execute(thread10);
            application.execute(thread11);
            application.execute(thread12);
            application.execute(thread13);
            application.execute(thread14);
            application.execute(thread15);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
