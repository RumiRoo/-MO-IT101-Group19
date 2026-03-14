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
    static final String SSS_TABLE = "SSSTable.csv";

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
            System.out.println("2. Exit the program");
            System.out.print("Select an option: ");
            String choice = input.next();

            if (choice.equals("1")) {
                boolean found = false;
                while (!found) {
                    System.out.print("Enter Employee Number: ");
                    String searchId = input.next();
                    
                    // This makes sure that the searchId string contains only digits 0 -9
                    if (!searchId.matches("\\d+")) { // handles invalid inputs like non-digits.
                    System.out.println("\nNot a valid employee number! Please use numbers only.");
                    continue; // Goes back to the "Enter..." line
                    }
                    
                    found = displayProfileOnly(searchId);
                    if (!found) System.out.println("\nEmployee number not found. Please try again.");
                }
            } else if (choice.equals("2")) {
                System.exit(0);
            } else {
                System.out.println("Invalid input. Please choose between 1 or 2.");
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
            } else { // Error handling: if user inputs neither 1 nor 2
                System.out.println("Invalid input. Please choose between 1 or 2.");
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
                boolean found = false;
                while (!found) {
                    System.out.print("Enter employee number: ");
                    String searchId = input.next();
                    
                    // Error handling: when input contains non-numbers
                    if (!searchId.matches("\\d+")) {
                        System.out.println("Invalid input. Employee number must contain only numbers.");
                        continue; // restarts the loop
                    }
                    
                    found = processPayrollLogic(searchId, false);
                    
                    if (!found) {
                        System.out.println("\nEmployee number not found. Please try again.");
                    }
                }    
            } else if (choice.equals("2")) {
                    processPayrollLogic("", true);
            } else if (choice.equals("3")) {
                    System.exit(0);
            } else {
                System.out.println("Invalid input. Please choose between 1, 2, and 3.");
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
        } catch (java.io.FileNotFoundException e) {
            System.out.println("EmployeeDatabase.csv was not found in the project folder.");
        } catch (Exception e) {
            System.out.println("Error in reading EmployeeDatabase.csv: " + e.getMessage());
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

    // DEDUCTIONS AREA
    
    // SSS Contribution Bracket
    public static double sssContribution(double mGross) {
        double sss = 0;
        try {
            // Use full paths to avoid import issues
            java.io.File sssFile = new java.io.File("SSSTable.csv");
            java.util.Scanner sssScanner = new java.util.Scanner(sssFile);

            if (sssScanner.hasNextLine()) sssScanner.nextLine();

            while (sssScanner.hasNextLine()) {
                String line = sssScanner.nextLine();
                if (line.trim().isEmpty()) continue;

                // Using the same splitting logic as before for consistency
                String[] columns = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

                // columns[0] = Lower Bound, columns[2] = Contribution Amount
                double lowerBound = Double.parseDouble(clean(columns[0]).replace(",", ""));
                double amount = Double.parseDouble(clean(columns[2]).replace(",", ""));

                if (mGross >= lowerBound) {
                    sss = amount;
                }
            }
            sssScanner.close();
        } catch (java.io.FileNotFoundException e) {
            System.out.println("SSSTable csv file not found.");
        } catch (Exception e) {
            System.out.println("System failed to read SSSTable csv file: " + e.getMessage());
        }
        
        return sss;
    }
    
    // Philhealth Contribution
    public static double philhealthContribution(double mGross) {
        double ph = 0;
        if (mGross <= 10000) {
            ph = 300.0 * 0.5;
        } else if (mGross >= 60000) {
            ph = 1800 * 0.5;
        } else {
            ph = (mGross * 0.03) * 0.5;
        }
        return ph;
    }
    
    // Pag-IBIG Contribution
    public static double pagIbig(double mGross) {
        double pi = 0;
        if (mGross <= 1500) {
            pi = mGross * 0.02;
        } else {
            pi = mGross * 0.01;
        }
        
        if (pi > 100.0) {
            pi = 100.0; // Pag-IBIG can't exceed 100
        }
        return pi;
    }
    
    // Withholding Tax
    public static double withholdingTax(double taxableIncome) {
        double tax = 0;
        if (taxableIncome >= 666667) {
            tax = 200833.33 + (0.35 * (taxableIncome - 666667));
        } else if (taxableIncome >= 166667) {
            // 166,667 to 666,666
            tax = 40833.33 + (0.32 * (taxableIncome - 166667));
        } else if (taxableIncome >= 66667) {
            // 66,667 to 166,666
            tax = 10833.33 + (0.30 * (taxableIncome - 66667));
        } else if (taxableIncome >= 33333) {
            // 33,333 to 66,666
            tax = 2500.00 + (0.25 * (taxableIncome - 33333));
        } else if (taxableIncome >= 20833) {
            // 20,833 to 33,332
            tax = 0 + (0.20 * (taxableIncome - 20833));
        } else {
            // 20,832 and below
            tax = 0;
        }
        return tax;
    }
    
    // Salary Computation
    
    public static void calculateSalary(String id, String name, String bday, double rate) {
        String[] months = {"", "", "", "", "", "", "June", "July", "August", "September", "October", "November", "December"};
        int[] days = {0, 0, 0, 0, 0, 0, 30, 31, 31, 30, 31, 30, 31}; // the zeros represent months January to May

        System.out.println("\nEmployee #: " + id);
        System.out.println("Employee Name: " + name);
        System.out.println("Birthday: " + bday);

        for (int m = 6; m <= 12; m++) {
            double h1 = getHours(id, m, 1);
            double h2 = getHours(id, m, 2);
            
            if (h1 == 0 && h2 == 0) continue; // for avoiding the months January to May

            double g1 = h1 * rate; // 1st cut-off
            double g2 = h2 * rate; // 2nd cut-off
            double mGross = g1 + g2; // Gross Monthly Salary
            
            // Deductions extracted from their methods
            double sss = sssContribution(mGross);
            double ph = philhealthContribution(mGross);
            double pi = pagIbig(mGross);
            
            double taxableIncome = mGross - (sss + ph + pi);
            double tax = withholdingTax(taxableIncome);
            
            double totalDeductions = sss + ph + pi + tax;
            double netPaySecond = g2 - totalDeductions;

            // Display
            System.out.println("\nCutoff Date: " + months[m] + " 1 to " + months[m] + " 15");
            System.out.println("Total Hours Worked: " + h1); // Displays 1 decimal place
            System.out.println("Gross Salary: " + g1);
            System.out.println("Net Salary: " + g1);

            System.out.println("\nCutoff Date: " + months[m] + " 16 to " + months[m] + " " + days[m]);
            System.out.println("Total Hours Worked: " + h2); // Displays 1 decimal place
            System.out.println("Gross Salary: " + g2);
            System.out.println("Deductions:");
            System.out.println("  SSS: " + sss);
            System.out.println("  PhilHealth: " + ph);
            System.out.println("  Pag-IBIG: " + pi);
            System.out.println("  Withholding Tax: " + tax);
            System.out.println("Total Deductions: " + totalDeductions);
            System.out.printf("NET SALARY: " + netPaySecond + "\n");
        }
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

                String idInFile = clean(columns[0]);
                
                if (idInFile.equals(searchId)) {
                    // Column D (Index 3) is Date
                    String dateString = columns[3].replace("\"", "").trim();
                    String[] dateParts = dateString.split("/");
                    int d = Integer.parseInt(dateParts[0]); // ensures that the date column is read correctly
                    int m = Integer.parseInt(dateParts[1]); // DD/MM/YYYY

                    boolean isFirstCutoff = (cutoff == 1 && d <= 15);
                    boolean isSecondCutoff = (cutoff == 2 && d > 15);

                    if (m == month && (isFirstCutoff || isSecondCutoff)) {
                        // Column E (Index 4) is Log In, Column F (Index 5) is Log Out
                        String timeInStr = columns[4].replace("\"", "").trim();
                        String timeOutStr = columns[5].replace("\"", "").trim();

                        String[] inParts = timeInStr.split(":");
                        double timeIn = Double.parseDouble(inParts[0]) + (Double.parseDouble(inParts[1]) / 60.0); // for converting times to decimal
                        
                        String[] outParts = timeOutStr.split(":");
                        double timeOut = Double.parseDouble(outParts[0]) + (Double.parseDouble(outParts[1]) / 60.0);

                        if (timeIn <= 8.0833) timeIn = 8.0;
                        if (timeOut > 17.0) timeOut = 17.0; // no extra hours (5:00PM cap)

                        double dailyHours = (timeOut - timeIn) - 1.0; // lunch break still holds
                        if (dailyHours > 0) {
                            totalHours = totalHours + dailyHours; // Total hours worked
                        }
                    }
                }
            }
            fileScanner.close();
        } catch (java.io.FileNotFoundException e) {
            System.out.println("Error: Attendance.csv is missing. Cannot calculate hours.");
        } catch (Exception e) {
            System.out.println("Problem reading Attendance.csv: " + e.getMessage());
        }
        return totalHours;
    }

    // For clean calculation of hours worked (e.g., converting 8:30 AM to 8.5)
    public static double parseT(String t) {
        String[] p = t.split(":");
        return Double.parseDouble(p[0]) + (Double.parseDouble(p[1]) / 60.0); // conversion of HH:mm to raw decimal
    }

    public static String clean(String s) {
        if (s == null) return "";
        String res = s.trim().replace("\"", "");
        if (res.startsWith("\uFEFF")) res = res.substring(1);
        return res;
    }
}