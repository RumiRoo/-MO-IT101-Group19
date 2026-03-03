/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.motorph;

/**
 *
 * @author PC
 */
public class MotorPH_ApplyDeductions {
    public static void main(String[] args) {
        /* Revised MotorPH Payroll System using methods for each government deduction
        Using mock details of one specific employee "Manuel Garcia III"
        Goal: calculates semi-monthly net pay using specialized methods for SSS, PhilHealth, Pag-IBIG, and Withholding Tax.
        Because of the required govt deductions, the computations will include calculating for the monthly salary.
        */
        
        // Data Initialization
        String employeeName = "Manuel Garcia III";
        double semiMonthlyGross = 45000.00;
        double monthlyGross = semiMonthlyGross * 2; // Semi-monthly multiplied by 2 to get monthly salary.
        
        // Data Validation: need to ensure that the salary is a positive number before doing any calculations
        if (semiMonthlyGross <= 0) {
            System.out.println("Invalid salary rate. Gross Salary must be a positive number.");
            return; // This stops the program if validation fails
        }
        
        // Computing the final net pay using the required method
        double netPay = computeNetPay(semiMonthlyGross);
        
        // Printing the results
        System.out.println("MotorPH PAYROLL: MANUEL GARCIA\n");
        System.out.printf("SEMI-MONTHLY GROSS: PHP %,.2f%n\n", semiMonthlyGross);
        System.out.println("Semi-Monthly Deductions:");
        System.out.printf("SSS: PHP %,.2f%n", computeSSS(semiMonthlyGross));
        System.out.printf("PhilHealth: PHP %,.2f%n", computePhilHealth(semiMonthlyGross));
        System.out.printf("Pag-IBIG: PHP %,.2f%n", computePagIbig(semiMonthlyGross));
        System.out.printf("Withholding Tax: PHP %,.2f%n\n", computeIncomeTax(semiMonthlyGross));
        System.out.printf("Net Pay: PHP %,.2f%n", netPay);  
    }
    
    
    public static double computeSSS(double semiGross) {
        return 1125.00;
    }
    
    public static double computePhilHealth(double semiGross) {
        double monthlySalary = semiGross * 2;
        double monthlyPremium = 0;
        
        if (monthlySalary >= 60000) {
            monthlyPremium = 1800.00;
        } else {
            monthlyPremium = monthlySalary * 0.03; // For the lower brackets of Philhealth
        }
        
        return (monthlyPremium * 0.5) / 2; // 50% employee share and further halved for semi-monthly.
    }
    
    public static double computePagIbig(double semiGross) {
        return 100.00; // 2% rate but gov't rules dictate that max contribution is 100.00 so I'll return 100.00 for simplicity
    }
    
    public static double computeIncomeTax(double semiGross) {
        double monthlyGross = semiGross * 2;
        double monthlyDeductions = (computeSSS(semiGross) + computePhilHealth(semiGross) + computePagIbig(semiGross)) * 2;
        double taxableIncome = monthlyGross - monthlyDeductions;
        
        double monthlyTax = 0;
        
        // MotorPH Bracket rule for 66,667 to 166,667
        if (taxableIncome >= 66667 && taxableIncome < 166667) {
            monthlyTax = 10833.00 + (0.30 * (taxableIncome - 66667));
        }
        
        // Return semi-monthly portion by dividing by 2.
        return monthlyTax / 2;
    }
    
    public static double computeNetPay(double semiGross) {
        double totalDeductions = computeSSS(semiGross) + 
                                 computePhilHealth(semiGross) + 
                                 computePagIbig(semiGross) + 
                                 computeIncomeTax(semiGross);
        return semiGross - totalDeductions;
    }
}
