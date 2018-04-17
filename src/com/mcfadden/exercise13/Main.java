package com.mcfadden.exercise13;
//Ean McFadden
//Exercise 13
//April 17, 2018
import java.util.concurrent.Semaphore;
class CheckingAccount {
    private int balance;
    private Semaphore permits = new Semaphore(1, true);
    public CheckingAccount(int initialBalance)
    {
        balance = initialBalance;
    }

    public int synchronize(CheckingAccount account, int amount){
            try {
                permits.acquire();
            } catch (InterruptedException e) {

                return account.balance;
            }
            if (amount <= account.balance) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                }

                account.balance -= amount;
            }
            permits.release();
            return account.balance;
        }

    }

public class Main {
    public static void main(String[] args) {
        CheckingAccount account = new CheckingAccount(100);

        Runnable r = new Runnable() {
            @Override
            public void run() {
                String name = Thread.currentThread().getName();
                for (int i = 0; i < 10; i++) {
                    System.out.println(name + " tries to withdraw $10, balance: " +
                            account.synchronize(account, 10));
                }
            }
        };

        Thread thdHusband = new Thread(r);
        thdHusband.setName("Husband");

        Thread thdWife = new Thread(r);
        thdWife.setName("Wife");

        thdHusband.start();
        thdWife.start();
    }
}