package com.banddreea.tracker.dto;

import java.math.BigDecimal;

public class MonthlySummary {

    private int year;
    private int month;
    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
    private String monthName;

    public MonthlySummary(int year, int month, BigDecimal totalIncome, BigDecimal totalExpense) {

        this.year = year;
        this.month = month;
        this.totalIncome = totalIncome;
        this.totalExpense = totalExpense;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public BigDecimal getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(BigDecimal totalIncome) {
        this.totalIncome = totalIncome;
    }

    public BigDecimal getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(BigDecimal totalExpense) {
        this.totalExpense = totalExpense;
    }

    public BigDecimal getNet() {
        return totalIncome.subtract(totalExpense);
    }

    public String getMonthName() {
        return monthName;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }
}

