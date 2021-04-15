package pl.travel.travelapp.exceptions;

public class UnAuthException extends Exception{
    public UnAuthException() {}

    @Override
    public String toString() {
        return "UnAuthException: auth failed";
    }
}
