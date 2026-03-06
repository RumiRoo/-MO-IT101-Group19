/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.motorph;

/**
 *
 * @author Alyssa P
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Employee {

    static final String EMP_DB_FILE = "EmployeeDatabase.csv";
    static final String ATTENDANCE_FILE = "Attendance.csv";

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.println("MotorPH SIGN-IN PORTAL");
        System.out.print("Enter Username: ");
        String username = input.nextLine();
        System.out.print("Enter Password: ");
        String password = input.nextLine();

        if ((username.equals("employee") || username.equals("payroll_staff")) && password.equals("12345")) {
            System.out.println("\nLogin Successful! Welcome, user!");
            if (username.equals("employee")) {
                employeeMenu(input);
            } else {
                payrollStaffMenu(input);
            }
        } else {
            System.out.println("Incorrect username and/or password");
            System.exit(0);
        }
    }

    // Menu section

    public static void employeeMenu(Scanner input) {
        while (true) {
            System.out.println("\nEMPLOYEE MENU");
            System.out.println("1. Enter your employee number");
            System.out.println("2. Exit the program\n");
            System.out.print("Select an option: ");
            String choice = input.next();

            if (choice.equals("1")) {
                boolean found = false;
                while (!found) {
                    System.out.print("Enter Employee Number: ");
                    String searchId = input.next();
                    found = displayProfileOnly(searchId);
                    if (!found) System.out.println("Employee number does not exist");
                }
            } else if (choice.equals("2")) {
                System.exit(0);
            }
        }
    }

    public static void payrollStaffMenu(Scanner input) {
        while (true) {
            System.out.println("\nPAYROLL STAFF MENU");
            System.out.println("1. Process Payroll");
            System.out.println("2. Exit the program");
            System.out.print("Select an option: ");
            String choice = input.next();

            if (choice.equals("1")) {
                processPayrollMenu(input);
            } else if (choice.equals("2")) {
                System.exit(0);
            }
        }
    }

    public static void processPayrollMenu(Scanner input) {
        while (true) {
            System.out.println("\nProcess Payroll (Note: Do not include allowances)");
            System.out.println("1. One employee");
            System.out.println("2. All employees");
            System.out.println("3. Exit the program");
            System.out.print("Select an option: ");
            String choice = input.next();

            if (choice.equals("1")) {
                System.out.print("Enter Employee Number: ");
                String searchId = input.next();
                if (!processPayrollLogic(searchId, false)) {
                    System.out.println("Employee number does not exist.");
                }
            } else if (choice.equals("2")) {
                processPayrollLogic("", true);
            } else if (choice.equals("3")) {
                System.exit(0);
            }
        }
    }

    /*
    Data Search Section:
    "displayProfileOnly" cans the Employee Database csv using try-catch,
    returns employee name, number, and birthday
    */
    public static boolean displayProfileOnly(String searchId) {
        try {
            java.io.File file = new java.io.File("EmployeeDatabase.csv");
            java.util.Scanner fileScanner = new java.util.Scanner(file);
            fileScanner.nextLine(); // Skip header

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                if (line.trim().isEmpty()) continue; 

                // Splits line every comma but ignores commas inside quotation marks
                String[] columns = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                
                // Safety check for the 19-column file
                if (columns.length < 19) continue;

                String idInFile = columns[0].replace("\"", "").trim();
                
                if (idInFile.equals(searchId)) {
                    String lastName = columns[1].replace("\"", "").trim();
                    String firstName = columns[2].replace("\"", "").trim();
                    String birthday = columns[3].replace("\"", "").trim();

                    System.out.println("\nEmployee Number: " + idInFile);
                    System.out.println("Employee Name: " + firstName + " " + lastName);
                    System.out.println("Birthday: " + birthday);
                    
                    fileScanner.close();
                    return true;
                }
            }
            fileScanner.close();
        } catch (Exception e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return false;
    }

    public static boolean processPayrollLogic(String searchId, boolean processAll) {
        boolean foundAtLeastOne = false;
        
        try {
            java.io.File file = new java.io.File("EmployeeDatabase.csv");
            java.util.Scanner fileScanner = new java.util.Scanner(file);
            fileScanner.nextLine(); // Skip header

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                if (line.trim().isEmpty()) continue;

                String[] columns = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                if (columns.length < 19) continue; // Must have at least 19 columns

                String idInFile = columns[0].replace("\"", "").trim();
                
                if (processAll == true || idInFile.equals(searchId)) {
                    foundAtLeastOne = true;
                    
                    String lastName = columns[1].replace("\"", "").trim();
                    String firstName = columns[2].replace("\"", "").trim();
                    String birthday = columns[3].replace("\"", "").trim();
                    
                    // Column S (Index 18) is the Hourly Rate
                    String rateString = columns[18].replace("\"", "").replace(",", "").trim();
                    double hourlyRate = Double.parseDouble(rateString);
                    
                    calculateSalary(idInFile, firstName + " " + lastName, birthday, hourlyRate);
                    
                    if (processAll == false) break; 
                }
            }
            fileScanner.close();
        } catch (Exception e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return foundAtLeastOne;
    }

    // Salary Computation
    
    public static void calculateSalary(String id, String name, String bday, double rate) {
        String[] months = {"", "", "", "", "", "", "June", "July", "August", "September", "October", "November", "December"};
        int[] days = {0, 0, 0, 0, 0, 0, 30, 31, 31, 30, 31, 30, 31};

        System.out.println("\nEmployee #: " + id);
        System.out.println("Employee Name: " + name);
        System.out.println("Birthday: " + bday);

        for (int m = 6; m <= 12; m++) {
            double h1Raw = getHours(id, m, 1);
            double h2Raw = getHours(id, m, 2);
            
            if (h1Raw == 0 && h2Raw == 0) continue;

            // TRUNCATION LOGIC: Shift decimal, drop remainder, shift back
            double h1 = ((long)(h1Raw * 10)) / 10.0;
            double h2 = ((long)(h2Raw * 10)) / 10.0;

            double g1 = h1 * rate;
            double g2 = h2 * rate;
            double mGross = g1 + g2;
            
            // Deductions (SSS, PhilHealth, Pag-IBIG, Tax logic remains the same)
            double sss = 1125.0; 
            double ph = ((mGross >= 60000 ? 1800.0 : mGross * 0.03) * 0.5);
            double pi = Math.min(mGross * 0.02, 100.0);
            double taxBase = mGross - (sss + ph + pi);
            double tax = (taxBase >= 66667) ? (10833.0 + (0.30 * (taxBase - 66667))) : 0;
            double totalD = sss + ph + pi + tax;

            // Display
            System.out.println("\nCutoff Date: " + months[m] + " 1 to " + months[m] + " 15");
            System.out.println("Total Hours Worked: " + h1); // Displays 1 decimal place
            System.out.println("Gross Salary: " + formatPHP(g1));
            System.out.println("Net Salary: " + formatPHP(g1));

            System.out.println("\nCutoff Date: " + months[m] + " 16 to " + months[m] + " " + days[m]);
            System.out.println("Total Hours Worked: " + h2); // Displays 1 decimal place
            System.out.println("Gross Salary: " + formatPHP(g2));
            System.out.println("Deductions:");
            System.out.println("  SSS: " + formatPHP(sss));
            System.out.println("  PhilHealth: " + formatPHP(ph));
            System.out.println("  Pag-IBIG: " + formatPHP(pi));
            System.out.println("  Withholding Tax: " + formatPHP(tax));
            System.out.println("------------------------------");
            System.out.println("Total Deductions: \n" + formatPHP(totalD));
            System.out.println("NET SALARY: " + formatPHP(g2 - totalD));
        }
    }

    // Helper method to format currency consistently; not rounding off amounts but displaying only 2 decimal    
    public static String formatPHP(double amount) {
        return String.format("PHP %,.2f", amount);
    }

    public static double getHours(String searchId, int month, int cutoff) {
        double totalHours = 0;
        try {
            java.io.File file = new java.io.File("Attendance.csv");
            java.util.Scanner fileScanner = new java.util.Scanner(file);
            fileScanner.nextLine(); // Skip header

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                if (line.trim().isEmpty()) continue;
                
                // Attendance usually doesn't have commas in dates/times, standard split is safe here
                String[] columns = line.split(",");
                if (columns.length < 6) continue; // Safety check for 6 columns

                String idInFile = columns[0].replace("\"", "").trim();
                
                if (idInFile.equals(searchId)) {
                    // Column D (Index 3) is Date
                    String dateString = columns[3].replace("\"", "").trim();
                    String[] dateParts = dateString.split("/");
                    int m = Integer.parseInt(dateParts[0]);
                    int d = Integer.parseInt(dateParts[1]);

                    boolean isFirstCutoff = (cutoff == 1 && d <= 15);
                    boolean isSecondCutoff = (cutoff == 2 && d > 15);

                    if (m == month && (isFirstCutoff || isSecondCutoff)) {
                        // Column E (Index 4) is Log In, Column F (Index 5) is Log Out
                        String timeInStr = columns[4].replace("\"", "").trim();
                        String timeOutStr = columns[5].replace("\"", "").trim();

                        String[] inParts = timeInStr.split(":");
                        double timeIn = Double.parseDouble(inParts[0]) + (Double.parseDouble(inParts[1]) / 60.0);
                        
                        String[] outParts = timeOutStr.split(":");
                        double timeOut = Double.parseDouble(outParts[0]) + (Double.parseDouble(outParts[1]) / 60.0);

                        if (timeIn <= 8.0833) timeIn = 8.0; 
                        if (timeOut > 17.0) timeOut = 17.0;

                        double dailyHours = (timeOut - timeIn) - 1.0; 
                        if (dailyHours > 0) {
                            totalHours = totalHours + dailyHours;
                        }
                    }
                }
            }
            fileScanner.close();
        } catch (Exception e) {}
        return totalHours;
    }

    public static double parseT(String t) {
        String[] p = t.split(":");
        return Double.parseDouble(p[0]) + (Double.parseDouble(p[1]) / 60.0);
    }

    public static String clean(String s) {
        if (s == null) return "";
        String res = s.trim().replace("\"", "");
        if (res.startsWith("\uFEFF")) res = res.substring(1);
        return res;
    }
}