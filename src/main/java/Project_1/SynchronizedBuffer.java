/* Name: Joshua Samontanez
Course: CNT 4714 Summer 2022
Assignment title: Project 1 – Synchronized, Cooperating Threads Under Locking
Due Date: June 5, 2022
*/

package Project_1;

import java.io.*;
import java.text.NumberFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SynchronizedBuffer implements Buffer{
    // Lock to control mutually exclusive access to the buffer
    private final Lock accessLock = new ReentrantLock();

    // Conditions to control the depositing and withdrawing and have a synchronized access between threads
    private final Condition canDeposit = accessLock.newCondition();
    private final Condition canWithdraw = accessLock.newCondition();

    private int balance = 0;    // Starting balance is zero
    private boolean occupied = false;   // whether buffer is occupied - contents same as last access

    @Override
    public void deposit(String thread, int depositValue, BufferedWriter transactionLogs, BufferedWriter flaggedTransactionLogs) {
        accessLock.lock();
        try{
            // If the buffer is occupied, place the thread in waiting state until the buffer is empty
            while (occupied){
                canDeposit.await();
            }
            // Tracks the balance
            balance += depositValue;
            occupied = true;

            // Print out the transaction
            displayState(thread + " deposits $" + depositValue, false, false, transactionLogs);

            // If the costumer deposit a value greater than $350, flag and log the transaction
            if (depositValue > 350) {
                flaggedTransaction(false, thread, depositValue, transactionLogs, flaggedTransactionLogs);
            }

            // signal thread waiting to read from buffer
            canWithdraw.signal();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        } finally {
            // Unlock this object
            accessLock.unlock();
        }
    }

    @Override
    public void withdraw(String thread, int withdrawalValue, BufferedWriter transactionLogs, BufferedWriter flaggedTransactionLogs) {
        // Lock this object
        accessLock.lock();
        try{
            // While no data to read, place thread in waiting state
            while (!occupied) {
                canWithdraw.await();
            }
            // If the current balance is smaller than the withdrawal value, block the transaction
            if (balance - withdrawalValue < 0) {
                displayState(thread + " withdraws $" + withdrawalValue, true, false, transactionLogs);
                occupied = false;
            }
            // Else, complete the withdrawal transaction
            else {
                // Keep track of the balance and show the transaction
                balance -= withdrawalValue;
                displayState(thread + " withdraws $" + withdrawalValue, true, true, transactionLogs);

                // If the withdrawal value is greater than $75, flag and log the transaction
                if (withdrawalValue > 75) {
                    flaggedTransaction(true, thread, withdrawalValue, transactionLogs, flaggedTransactionLogs);
                }
            }
            // Signal thread waiting for buffer to be empty
            canDeposit.signal();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            // Unlock this object
            accessLock.unlock();
        }
    }

    public void flaggedTransaction (boolean isWithdraw, String thread, int value, BufferedWriter transactionLogs, BufferedWriter flaggedTransactionLogs) throws IOException {
        // Get the timestamp of the transaction
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a");
        ZonedDateTime now  = ZonedDateTime.now();

        // Format the value into the dollars
        NumberFormat currency = NumberFormat.getCurrencyInstance();
        String money = currency.format(value);

        String log;
        String flagMessage;
        String timestamp;

        // Show the flagged deposited transaction and add it into the logger
        if (!isWithdraw) {
            flagMessage = "\n* * * Flagged Transaction - Depositor " + thread + " Made a Deposit In Excess of $350.00 USD - See Flagged Transaction Log.\n";
            //log = "\nDepositor " + thread + " issued deposit of $" + value + " at " + now.format(formatter) + " " + TimeZone.getDefault().getDisplayName(true, TimeZone.SHORT) + "\n";
            timestamp = now.format(formatter) + " " + TimeZone.getDefault().getDisplayName(true, TimeZone.SHORT);

            log = String.format("%-20s %-20s %-10s\t\t %-10s %n", "Deposit", thread, money, timestamp);
        }
        // Show the flagged withdrawal transaction and add it into the logger
        else {
            flagMessage = "\n* * * Flagged Transaction - Withdrawal " + thread + " Made a Withdrawal In Excess of $75.00 USD - See Flagged Transaction Log.\n";
            //log = "\n\tWithdrawal " + thread + " issued withdrawal of $" + value + " at " + now.format(formatter) + " " + TimeZone.getDefault().getDisplayName(true, TimeZone.SHORT) + "\n";
            timestamp = now.format(formatter) + " " + TimeZone.getDefault().getDisplayName(true, TimeZone.SHORT);
            log = String.format("%-20s %-20s %-10s\t\t %-10s %n", "Withdraw", thread, money, timestamp);
        }

        flaggedTransactionLogs.append(log);
        transactionLogs.append(flagMessage + "\n");
        System.out.println(flagMessage);
    }

    // Display the current transaction
    public void displayState(String operation, boolean isWithdraw, boolean canWithdraw, BufferedWriter transactionLogs) throws IOException {
        // Text format
        String format = "%-40s%-30s%s%n";

        // Format the value into the dollars
        NumberFormat currency = NumberFormat.getCurrencyInstance();
        String money = currency.format(balance);

        String message;

        // If the costumer is depositing money, show the deposit transaction
        if (!isWithdraw) {
            message = String.format(format, operation, "", "(+) Current Balance is " + money);
        }
        // If the costumer is withdrawing money, show the withdrawal transaction
        else {
            // If the costumer tries to withdraw more than what they have, block the transaction
            if(!canWithdraw) {
                message = String.format(format, "", operation, "(***) WITHDRAWAL BLOCKED - INSUFFICIENT FUNDS!!!");
            }
            // Show that the transaction is completed
            else {
                message = String.format(format, "", operation, "(-) Current Balance is " + money);
            }
        }
        System.out.print(message);
        transactionLogs.append(message);
    }
}
