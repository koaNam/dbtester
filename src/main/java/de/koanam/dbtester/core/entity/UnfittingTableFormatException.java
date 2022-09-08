package de.koanam.dbtester.core.entity;

public class UnfittingTableFormatException extends RuntimeException {

    private int actualTableWidth;
    private int expectedTableWidth;

    public UnfittingTableFormatException(String message, int actualTableWidth, int expectedTableWidth) {
        super(message);
        this.actualTableWidth = actualTableWidth;
        this.expectedTableWidth = expectedTableWidth;
    }

    public int getActualTableWidth() {
        return actualTableWidth;
    }

    public int getExpectedTableWidth() {
        return expectedTableWidth;
    }
}
