package com.machine;

import java.util.Scanner;

public class CoffeeMachine {
    private static int machineWater;
    private static int machineMilk;
    private static int machineBeans;
    private static int machineCups;
    private static int machineMoney;

    private static boolean machineInUse;
    private static MachineState state;
    private static FillState fill;

    private enum MachineState {
        MAIN,
        BUY,
        FILL
    }

    private enum FillState {
        WATER("ml of water"),
        MILK("ml of milk"),
        BEANS("grams of coffee beans"),
        CUPS("disposable cups of coffee");

        String menu;

        FillState(String item) {
            this.menu = "Write how many " + item + " do you want to add:";
        }

        public String getMenu() {
            return menu;
        }
    }

    public static void main(String[] args) {
        setupMachine();
        inputLoop();
    }

    private static void setupMachine() {
        machineWater = 400;
        machineMilk  = 540;
        machineBeans = 120;
        machineCups  = 9;
        machineMoney = 550;

        machineInUse = true;
        state = MachineState.MAIN;
        fill = FillState.WATER;
    }

    private static void inputLoop() {
        Scanner input = new Scanner(System.in);
        String command;

        while(machineInUse) {
            printMenu(state);
            command = input.next().toUpperCase();
            switch (state) {
                case MAIN:
                    mainMenu(command);
                    break;
                case BUY:
                    buyDrink(command);
                    break;
                case FILL:
                    fillMachine(command);
                    break;
                default:
                    break;
            }
        }
    }

    private static void printMenu(MachineState state) {
        switch (state) {
            case MAIN:
                System.out.println("Write action (buy, fill, take, remaining, exit):");
                break;
            case BUY:
                System.out.println("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu:");
                break;
            case FILL:
                System.out.println(fill.getMenu());
                break;
            default:
                break;
        }
    }

    private static void mainMenu(String command) {
        System.out.println();
        switch (command) {
            case "BUY":
                state = MachineState.BUY;
                break;
            case "FILL":
                state = MachineState.FILL;
                break;
            case "TAKE":
                takeMoney();
                break;
            case "REMAINING":
                printMachineStatus();
                break;
            case "EXIT":
                machineInUse = false;
                break;
            default:
                System.out.println("Unknown input.");
                break;
        }
    }

    private static void printMachineStatus() {
        System.out.println("The coffee machine has:");
        System.out.println(machineWater + " of water");
        System.out.println(machineMilk + " of milk");
        System.out.println(machineBeans + " of coffee beans");
        System.out.println(machineCups + " of disposable cups");
        System.out.println("$" + machineMoney + " of money");
        System.out.println();
    }

    private static void takeMoney() {
        System.out.println("I gave you $" + machineMoney);
        System.out.println();
        machineMoney = 0;
    }

    private static void fillMachine(String command) {
        int amount;

        try {
            amount = Integer.parseInt(command);
        } catch (NumberFormatException ex) {
            System.out.println("Invalid amount.");
            return;
        }

        switch (fill) {
            case WATER:
                machineWater += amount;
                fill = FillState.MILK;
                break;
            case MILK:
                machineMilk += amount;
                fill = FillState.BEANS;
                break;
            case BEANS:
                machineBeans += amount;
                fill = FillState.CUPS;
                break;
            case CUPS:
                machineCups += amount;
                fill = FillState.WATER;
                state = MachineState.MAIN;
                System.out.println();
                break;
            default:
                break;
        }
    }

    private static void buyDrink(String input) {
        state = MachineState.MAIN;
        switch (input) {
            case "1":
                new Drink.Espresso().makeDrink();
                break;
            case "2":
                new Drink.Latte().makeDrink();
                break;
            case "3":
                new Drink.Cappuccino().makeDrink();
                break;
            case "BACK":
                break;
            default:
                System.out.println("Input error! Try again.");
                state = MachineState.BUY;
                break;
        }
    }

    private static class Drink {
        private static int water;
        private static int milk;
        private static int beans;
        private static int cost;

        public void makeDrink() {
            if (checkIngredients()) {
                System.out.println("I have enough resources, making you a coffee!");
                machineWater -= water;
                machineMilk  -= milk;
                machineBeans -= beans;
                machineMoney += cost;
                machineCups--;
            }

            System.out.println();
        }

        private boolean checkIngredients() {
            boolean made = true;

            if (water > machineWater) {
                System.out.println("Sorry, not enough water!");
                made = false;
            }

            if (milk > machineMilk) {
                System.out.println("Sorry, not enough milk!");
                made = false;
            }

            if (beans > machineBeans) {
                System.out.println("Sorry, not enough coffee beans!");
                made = false;
            }

            if (machineCups == 0) {
                System.out.println("Sorry, not enough cups!");
                made = false;
            }

            return made;
        }

        private static class Espresso extends Drink {
            Espresso() {
                water = 250;
                milk  = 0;
                beans = 16;
                cost  = 4;
            }
        }

        private static class Latte extends Drink {
            Latte() {
                water = 350;
                milk  = 75;
                beans = 20;
                cost  = 7;
            }
        }

        private static class Cappuccino extends Drink {
            Cappuccino() {
                water = 200;
                milk  = 100;
                beans = 12;
                cost  = 6;
            }
        }
    }
}