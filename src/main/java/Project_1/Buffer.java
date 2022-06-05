/* Name: Joshua Samontanez
Course: CNT 4714 Summer 2022
Assignment title: Project 1 – Synchronized, Cooperating Threads Under Locking
Due Date: June 5, 2022
*/

package Project_1;

import java.io.BufferedWriter;

//Buffer interface specifies methods called by Withdrawal and Depositor.
public interface Buffer {
    void deposit(String thread, int value, BufferedWriter transactionLogs, BufferedWriter flaggedTransactionLogs);
    void withdraw(String thread, int value, BufferedWriter transactionLogs, BufferedWriter flaggedTransactionLogs);
}
