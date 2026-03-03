/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.motorph;

/**
 *
 * @author PC
 */
public class CalculateHoursWorked {
    public static void main(String[] args) {
        // Variables and initialization; decimal types for time for accuracy
        // Employee details are declared for both clarity and sample presentation
        String employeeName = "Manuel Garcia III";
        
        // 24-hour format to represent AM/PM time
        // Example: 1:00 PM should be represented by 13.0 in hours
        double timeIn = 8.0; // for 8:00 AM
        double timeOut = 17.0; // for 5:00 PM
        double breakTime = 1.0; // for the standard 1-hour unpaid lunch break
        
        /*
        Calculation: difference of timeIN and timeOut then remove unpaid break time
        (timeOut - timeIn) = calculates the gross duration of an employee's work day
        breakTime is subtracted as it is an unpaid rest period
        */
        double totalHoursWorked = (timeOut - timeIn) - breakTime;
        
        // Printing Details
        System.out.println("Employee Name: " + employeeName);
        System.out.println("Time-In: " + timeIn);
        System.out.println("Time-Out: " + timeOut);
        System.out.println("Break Time: " + breakTime);
        
        // Total Hours Worked Result
        System.out.print("Total Hours Worked: ");
        System.out.printf("%.2f hours%n", totalHoursWorked);
        
        //TESTING; TimeIn = 8AM, TimeOut = 5PM, BreakTime = 1HR; 8 hours of work
        // Setting an expected value for the unit testing
        String headerName = "TESTING AND VERIFICATION";
        double expectedHours = 8.0;
        double actualHoursWorked = (timeOut - timeIn) - breakTime;
        // End of shift subtracted to start, then break is subtracted
        
        System.out.println("");
        System.out.println(headerName);
        System.out.println("Calculated Hours Worked: " + actualHoursWorked);
        System.out.println("Expected Hours Worked: " + expectedHours);
        
        //VERIFICATION; if-else statement for maintaining accuracy
        if (actualHoursWorked == expectedHours) {
            System.out.println("TEST PASSED! Computation is correct.");
        } else {
            System.out.println("TEST FAILED! Check your operators and data types.");
        }
    }
}
