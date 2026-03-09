/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.motorph;

/**
 *
 * @author PC
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class MotorPH_ReadFromFile {
    public static void main(String[] args) {
        String fileName = "MotorPH_ReadFromFile.txt";
        File file = new File(fileName);
        
        // Validation: for checking if the file actually exists before trying to open
        if (!file.exists()) {
            System.out.println("Error: The file '" + fileName + "' was not found in the folder.");
            return;
        }
        
        System.out.println("Reading MotorPH employee data from file: \n");
        
        // using FileReader and BufferedReader to open the file
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int count = 0;

            // Reading the file line by line until there are no more lines
            while ((line = reader.readLine()) != null) {
                // Skip empty lines
                if (line.trim().isEmpty()) {
                    continue;
                }

                // Parse the line: Split by the comma
                String[] parts = line.split(",");

                // Validation: Ensure the line has exactly two parts (Name and Salary)
                if (parts.length < 2) {
                    System.out.println("Skipping malformed line: " + line);
                    continue;
                }

                try {
                    String name = parts[0].trim();
                    // Remove any spaces or commas in the salary string before parsing
                    double grossSalary = Double.parseDouble(parts[1].trim());

                    // 6. Validation: Ensure salary is a positive number
                    if (grossSalary <= 0) {
                        System.out.println("Skipping " + name + ": Salary must be greater than zero.");
                        continue;
                    }

                    // 7. Perform Computations
                    processEmployeePayroll(name, grossSalary);
                    count++;

                } catch (NumberFormatException e) {
                    System.out.println("Error: Could not read salary for line: " + line);
                }
            }
            System.out.println("NOTE: Successfully processed " + count + " employees.");

        } catch (IOException e) {
            System.out.println("An error occurred while reading the file: " + e.getMessage());
        }
    }

    // Deduction logic for employee payroll processing
    public static void processEmployeePayroll(String name, double gross) {
        // SSS (Fixed)
        double sss = 1125.00;

        // PhilHealth (3% total, employee pays half)
        double ph = (gross * 0.03) * 0.5;
        if (gross >= 60000) ph = 1800.0 * 0.5;

        // Pag-IBIG (2%, max of 100)
        double pi = Math.min(gross * 0.02, 100.0);

        // Taxable Income
        double taxBase = gross - (sss + ph + pi);
        double tax = 0;
        if (taxBase >= 66667) {
            tax = 10833.0 + (0.30 * (taxBase - 66667));
        }

        double totalDeductions = sss + ph + pi + tax;
        double netPay = gross - totalDeductions;

        // Print Summary
        System.out.println("Employee: " + name);
        System.out.printf("  Gross Salary: PHP %,.2f\n", gross);
        System.out.println("  Deductions: ");
        System.out.printf("   SSS: PHP %,.2f\n", sss);
        System.out.printf("   PhilHealth: PHP %,.2f\n", ph);
        System.out.printf("   Pag-IBIG: PHP %,.2f\n", pi);
        System.out.printf("   Income Tax: PHP %,.2f\n", tax);
        System.out.printf("Total Deductions: PHP %,.2f\n", totalDeductions);
        System.out.printf("NET PAY: PHP %,.2f\n", netPay);
        System.out.println();
    }
}
