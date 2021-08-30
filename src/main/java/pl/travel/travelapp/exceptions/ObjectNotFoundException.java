package pl.travel.travelapp.exceptions;

public class ObjectNotFoundException extends Exception{

    private String message = "Object does not exist";

    public ObjectNotFoundException() {}

    @Override
    public String toString() {
        return this.message;
    }

}
