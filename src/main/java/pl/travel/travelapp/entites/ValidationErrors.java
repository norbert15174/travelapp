package pl.travel.travelapp.entites;

import java.util.HashMap;
import java.util.Map;

public class ValidationErrors {
    Map <String, Object> errors = new HashMap <>();

    public void addError(String value , Object object) {
        errors.put(value , object);
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    @Override
    public String toString() {
        return buildErrorMessage();
    }

    private String buildErrorMessage() {
        String errorMessage = new String();
        errorMessage += "Błąd walidacji {";
        for (Map.Entry <String, Object> entry : this.errors.entrySet()) {
            String v = entry.getKey();
            Object k = entry.getValue();
            errorMessage += "\n \t" + v + " : " + k;
        }
        errorMessage += "\n}";
        return errorMessage;
    }

}
