package validator;

import domain.Utilizator;

import java.util.ArrayList;
import java.util.List;

public class UtilizatorValidator implements IValidator<Utilizator> {
    @Override
    public void validate(Utilizator entity) throws ValidationException {
        if (entity == null)
            throw new ValidationException("User null!");
        List<String> errors = new ArrayList<>();

        if(entity.getPassword() == null)
            errors.add( "Parola e null!\n");
        if (entity.getUsername() == null)
            errors.add( "Username null!\n");

        if (errors.isEmpty())
            return;

        throw new ValidationException(errors);
    }
}
