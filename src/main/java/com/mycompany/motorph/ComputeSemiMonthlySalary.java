/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.motorph;

/**
 *
 * @author PC
 */
/*
Program Description:
The program should calculate the semi-monthly salary for MotorPH employees.
It should also process total hours worked anmd hourly rates, validate inputs to
prevent financial errors, and generate a summarized payroll report for the cutoff.
*/

public class ComputeSemiMonthlySalary {
    public static void main(String[] args) {
        // I. DECLARE AND INITIALIZE VARIABLES
        // Using String for text and double for monetary precision
        String employeeName = "Manuel Garcia III";
        int employeeID = 10001;
        double totalHoursWorked = 84.5; // Total hours for the 2-week cutoff
        double hourlyRate = 535.71;    // Standard hourly rate in PHP
        double semiMonthlySalary = 0.0;

        // II. CONDITIONAL LOGIC
        // Check beforehand if the hours and rates are positive before calculating.
        // This will prevent "Negative Pay" errors or processing inactive employees.
        if (totalHoursWorked <= 0 || hourlyRate <= 0) {
            
            System.out.println("ERROR: Invalid payroll data for " + employeeName);
            
            if (totalHoursWorked < 0) {
                System.out.println("Error: Total hours cannot be negative (" + totalHoursWorked + ").");
            } else if (totalHoursWorked == 0) {
                System.out.println("Notice: Employee has zero hours. No salary generated.");
            }
            
            if (hourlyRate <= 0) {
                System.out.println("Error: Hourly rate must be greater than zero.");
            }
            
        } else {
            // III. ARITHMETIC COMPUTATIONS
            // The core formula for semi-monthly gross pay is: total working hours multiplied to employee's hourly rate
            semiMonthlySalary = totalHoursWorked * hourlyRate;

            //IV. DISPLAYING ALL OUTPUTS
            // Using formatted output to make sure that currency looks clean and used in real-life
            // comma to display for thousands (45,000 instead of 45000); ".2f" for 2 decimal places
            System.out.println("MotorPH Semi-Monthly Payroll\n");
            System.out.println("Employee ID: " + employeeID);
            System.out.println("Employee Name: " + employeeName);
            System.out.println("Total Hours: " + totalHoursWorked);
            System.out.println("Hourly Rate: PHP " + hourlyRate);
            System.out.println("");
            System.out.printf("Gross Salary: PHP %,.2f%n", semiMonthlySalary);
            System.out.println("Status: Computation successful.");
        }
    }
}
