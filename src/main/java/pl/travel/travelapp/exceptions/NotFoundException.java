package pl.travel.travelapp.exceptions;

public class NotFoundException extends Exception{

    public NotFoundException() {};

    @Override
    public String toString() {
        return "User doesn't exist - NotFoundException";
    }
}
