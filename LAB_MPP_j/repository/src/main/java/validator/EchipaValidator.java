package validator;

import domain.Echipa;

import java.util.ArrayList;
import java.util.List;

public class EchipaValidator implements IValidator<Echipa> {
    @Override
    public void validate(Echipa entity) throws ValidationException {
        if (entity == null)
            throw new ValidationException("Echipa null!");
        List<String> errors = new ArrayList<>();

        if (entity.getNrJucatori() < 0)
            errors.add( "Numarul de jucatori e negativ!\n");
        if (entity.getNrJucatori() >  30)
            errors.add( "Numarul de jucatori e prea mare!\n");
        if (entity.getOras() == null)
            errors.add( "Oras null!\n");
        if(entity.getNume() == null)
            errors.add( "Nume de echipa null!\n");

        if (errors.isEmpty())
            return;

        throw new ValidationException(errors);
    }
}
