/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.motorph;

/*
 * Group 19:
 * @authors Cerzo, Matthew David
 *          De Leon, Linkin May
 *          Gacula, Timothy Justin
 *          Jamboy, Niño Rey
 *          Ochoa, Stephanie Maye
 *          Posilero, Alyssa
 */
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Employee {

    static final String EMP_DB_FILE = "EmployeeDatabase.csv";
    static final String ATTENDANCE_FILE = "Attendance.csv";
    static final String SSS_TABLE = "SSSTable.csv";

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.println("MotorPH Sign-in Portal");
        System.out.print("Enter Username: ");
        String username = input.nextLine();
        System.out.print("Enter Password: ");
        String password = input.nextLine();

        if ((username.equals("employee") || username.equals("payroll_staff")) && password.equals("12345")) {
            System.out.println("\nLogin successful! Welcome, user!");
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

    // Sub-menus

    public static void employeeMenu(Scanner input) {
        while (true) {
            System.out.println("\nEmployee Menu");
            System.out.println("1. View Employee Detail");
            System.out.println("2. Exit the program");
            System.out.print("Select an option: ");
            String choice = input.next();

            if (choice.equals("1")) {
                boolean found = false;
                while (!found) {
                    System.out.print("Enter Employee Number: ");
                    String searchId = input.next();
                    if (!searchId.matches("\\d+")) { 
                        System.out.println("\nInvalid format. Numbers only.");
                        continue; 
                    }
                    found = displayProfileOnly(searchId);
                    if (!found) System.out.println("\nEmployee not found.");
                }
            } else if (choice.equals("2")) {
                System.exit(0);
            }
        }
    }

    public static void payrollStaffMenu(Scanner input) {
        while (true) {
            System.out.println("\nPayroll Staff Menu");
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
            System.out.println("\nProcess Payroll (Allowances not included)");
            System.out.println("1. One employee");
            System.out.println("2. All employees");
            System.out.println("3. Exit the program");
            System.out.print("Select an option: ");
            String choice = input.next();

            if (choice.equals("1")) {
                System.out.print("Enter employee number: ");
                String searchId = input.next();
                if (searchId.matches("\\d+")) { // trigger for bulleted view of "One Employee"
                    processPayrollLogic(searchId, false);
                }
            } else if (choice.equals("2")) { // trigger for tabular view of "All Employees"
                processPayrollLogic("", true);
            } else if (choice.equals("3")) {
                System.exit(0);
            }
        }
    }

    // Data Handling

    public static boolean displayProfileOnly(String searchId) {
        try {
            Scanner fileScanner = new Scanner(new File(EMP_DB_FILE));
            fileScanner.nextLine(); 
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] col = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                if (col.length < 19) continue;
                if (clean(col[0]).equals(searchId)) {
                    System.out.println("\nEmployee #: " + clean(col[0]));
                    System.out.println("Employee Name: " + clean(col[2]) + " " + clean(col[1]));
                    System.out.println("Birthday: " + clean(col[3]));
                    fileScanner.close();
                    return true;
                }
            }
            fileScanner.close();
        } catch (Exception e) { }
        return false;
    }

    public static boolean processPayrollLogic(String searchId, boolean processAll) {
        boolean foundAtLeastOne = false;
        ArrayList<String[]> attendanceList = new ArrayList<>();
        // Load the entire attendance file into ArrayList for singular reading
        // Prevents program from opening file 14 times per employee
        
        try {
            Scanner attScanner = new Scanner(new File(ATTENDANCE_FILE));
            if (attScanner.hasNextLine()) attScanner.nextLine();
            while (attScanner.hasNextLine()) {
                String line = attScanner.nextLine();
                if (!line.trim().isEmpty()) attendanceList.add(line.split(","));
            }
            attScanner.close(); // Attendance data should be stored in attendanceList so file is closed
        } catch (Exception e) {
            System.out.println("Attendance Load Error.");
        }
        
        try {
            Scanner fileScanner = new Scanner(new File(EMP_DB_FILE));
            String[] headers = fileScanner.hasNextLine() ? fileScanner.nextLine().split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)") : null;
            
            // Dynamic Mapping: if columns are changed/added,
            // program can find Hourly Rate by name instead of position
            int rateIndex = 18; 
            if (headers != null) {
                for (int i = 0; i < headers.length; i++) {
                    if (clean(headers[i]).equalsIgnoreCase("Hourly Rate")) { rateIndex = i; break; }
                }
            }

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] col = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                if (col.length <= rateIndex) continue;
                String idInFile = clean(col[0]);
                
                if (processAll || idInFile.equals(searchId)) {
                    foundAtLeastOne = true;
                    String name = clean(col[2]) + " " + clean(col[1]);
                    String bday = clean(col[3]);
                    double rate = Double.parseDouble(clean(col[rateIndex]).replace(",", ""));
                    
                    if (processAll) {
                        System.out.println("\n----------------------------------------------------------------------------------------------------------------------------------------------------------------");
                        System.out.println("PAYROLL SUMMARY FOR: " + name + " (" + idInFile + ")");
                        System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------------");
                        
                        // Table Header for "All Employees"
                        String head = pad("MONTH", 12)
                                + pad("GROSS SALARY", 22)
                                + pad("SSS", 18)
                                + pad("PHILHEALTH", 18)
                                + pad("PAG-IBIG", 18)
                                + pad("TAX", 20)
                                + pad("TOT. DEDUCTIONS", 22)
                                + "NET PAY";
                        System.out.println(head);
                    } else {
                        // Static profile data printed to prevent repeating inside 7-loop month calc.
                        System.out.println("\n Employee #: " + idInFile);
                        System.out.println(" Employee Name: " + name);
                        System.out.println(" Birthday: " + bday);
                    }
                    
                    // pre-loaded attendance list
                    calculateSalary(idInFile, name, rate, attendanceList, processAll);
                    if (!processAll) break;
                }
            }
            fileScanner.close();
        } catch (Exception e) { }
        return foundAtLeastOne;
    }

    // Math & Printing

    public static void calculateSalary(String id, String name, double rate, ArrayList<String[]> attList, boolean processAll) {
        String[] mo = {"", "", "", "", "", "", "June", "July", "August", "September", "October", "November", "December"};
        int[] dy = {0, 0, 0, 0, 0, 0, 30, 31, 31, 30, 31, 30, 31}; 
        
        for (int m = 6; m <= 12; m++) {
            double h1 = getHours(id, m, 1, attList);
            double h2 = getHours(id, m, 2, attList);
            if (h1 == 0 && h2 == 0) continue;

            double g1 = h1 * rate; 
            double g2 = h2 * rate; 
            double mGross = g1 + g2; 
            
            double sss = sssContribution(mGross);
            double ph = philhealthContribution(mGross);
            double pi = pagIbig(mGross);
            double tax = withholdingTax(mGross - (sss + ph + pi));
            double totalDed = sss + ph + pi + tax;
            double mNet = mGross - totalDed;

            if (processAll) {
                // Tabular Month Row for "All Employees"
                // Instead of printing every information for 7 months per 34 employees
                // Decluttering
                String row = pad(mo[m], 12)
                        + pad("" + mGross, 22)
                        + pad("" + sss, 18)
                        + pad("" + ph, 18)
                        + pad("" + pi, 18)
                        + pad("" + tax, 20)
                        + pad("" + totalDed, 22)
                        + ("" + mNet);
                System.out.println(row);
            } else {
                // Bulleted View for "One Employee" option
                System.out.println("\n Cutoff Date: " + mo[m] + " 1 to " + mo[m] + " 15");
                System.out.println(" Total Hours Worked: " + h1);
                System.out.println(" Gross Salary: " + g1);
                System.out.println(" Net Salary: " + g1);

                System.out.println("\n Cutoff Date: " + mo[m] + " 16 to " + mo[m] + " " + dy[m] + " (Second payout includes all deductions)");
                System.out.println(" Total Hours Worked: " + h2);
                System.out.println(" Gross Salary: " + g2);
                System.out.println("  Each Deduction:");
                System.out.println("    SSS: " + sss);
                System.out.println("    PhilHealth: " + ph);
                System.out.println("    Pag-IBIG: " + pi);
                System.out.println("    Tax: " + tax);
                System.out.println(" Total Deductions: " + totalDed);
                System.out.println(" Net Salary: " + (g2 - totalDed));
            }
        }
    }

    public static double getHours(String id, int m, int c, ArrayList<String[]> list) {
        double total = 0;
        for (String[] col : list) {
            // Quick filtering to skip unneeded info before computation
            if (col.length < 6 || !clean(col[0]).equals(id)) continue;
            String[] dp = clean(col[3]).split("/");
            if (Integer.parseInt(dp[0]) == m) {
                int day = Integer.parseInt(dp[1]);
                if ((c == 1 && day <= 15) || (c == 2 && day > 15)) {                    
                    // Time parsing delegation to stop redundant parsing
                    double in = parseT(col[4]), out = parseT(col[5]);
                    
                    // MotorPH Rules: Grace period and cap logic for time-in and time-out
                    if (in <= 8.0833) in = 8.0; 
                    if (out > 17.0) out = 17.0; 
                    double daily = (out - in) - 1.0;
                    if (daily > 0) total += daily;
                }
            }
        }
        return total;
    }

    // Deduction Methods

    public static double sssContribution(double mGross) {
        double sss = 0;
        try {
            Scanner sScanner = new Scanner(new File(SSS_TABLE));
            sScanner.nextLine();
            while (sScanner.hasNextLine()) {
                String[] col = sScanner.nextLine().split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                if (mGross >= Double.parseDouble(clean(col[0]).replace(",", ""))) 
                    sss = Double.parseDouble(clean(col[2]).replace(",", ""));
            }
            sScanner.close();
        } catch (Exception e) { }
        return sss;
    }

    public static double philhealthContribution(double mGross) {
        if (mGross <= 10000) return 150.0;
        if (mGross >= 60000) return 900.0;
        return (mGross * 0.03) * 0.5;
    }

    public static double pagIbig(double mGross) {
        double pi = (mGross <= 1500) ? mGross * 0.02 : mGross * 0.01;
        return (pi > 100.0) ? 100.0 : pi;
    }

    public static double withholdingTax(double taxableIncome) {
        if (taxableIncome >= 666667) return 200833.33 + (0.35 * (taxableIncome - 666667));
        if (taxableIncome >= 166667) return 40833.33 + (0.32 * (taxableIncome - 166667));
        if (taxableIncome >= 66667) return 10833.33 + (0.30 * (taxableIncome - 66667));
        if (taxableIncome >= 33333) return 2500.00 + (0.25 * (taxableIncome - 33333));
        if (taxableIncome >= 20833) return (0.20 * (taxableIncome - 20833));
        return 0;
    }

    // Others
    
    // Centralized utility for converting HH:MM string formats into decimal numbers for math operations
    public static double parseT(String t) {
        String[] p = t.replace("\"", "").trim().split(":");
        return Double.parseDouble(p[0]) + (Double.parseDouble(p[1]) / 60.0); 
    }

    public static String clean(String s) {
        return (s == null) ? "" : s.trim().replace("\"", "").replace("\uFEFF", "");
    }

    public static String pad(String s, int n) {
    String res = (s == null) ? "" : s;
    while (res.length() < n) {
        res = res + " ";
    }
    return res;
    }
}