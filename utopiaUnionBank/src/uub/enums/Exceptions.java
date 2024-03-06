package uub.enums;

public enum Exceptions {

    INVALID_INPUT("Invalid input. Please provide valid data."),
    ACCOUNT_NOT_FOUND("Account not found."),
    USER_NOT_FOUND("User not found."),
    DATABASE_CONNECTION_ERROR("Error connecting to the database."),
    UNAUTHORIZED_ACCESS("Unauthorized access. Please log in."),
    TRANSACTION_ERROR("Transaction Failed"),
    
    BALANCE_INSUFFICIENT("Insufficient balance !"),
    UNKNOWN_ERROR("An unknown error occurred.");

    private final String errorMessage;

    Exceptions(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return errorMessage;
    }
}
