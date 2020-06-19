package validator;


import java.util.List;

public class ValidationException extends RuntimeException {

    public ValidationException(String msg) {
        super("ValidationException: " + msg);
    }

    public ValidationException(List<String> msg){
        super("ValidationException: " + msg.get(0));
    }
}


