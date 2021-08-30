package pl.travel.travelapp.exceptions;

public class AccessForbiddenException extends Exception {

    private final String message = "You do not have permission to access this resource";

    public AccessForbiddenException() {
    }

    @Override
    public String toString() {
        return this.message;
    }

}
