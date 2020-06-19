package validator;

import domain.Vanzare;

import java.util.ArrayList;
import java.util.List;

public class VanzareValidator implements IValidator<Vanzare> {
    @Override
    public void validate(Vanzare entity) throws ValidationException {
        if (entity == null)
            throw new ValidationException("Echipa null!");
        List<String> errors = new ArrayList<>();

        if(entity.getMeci() == null)
            errors.add( "Meci null!\n");
        else if (entity.getBileteCumparate() >  entity.getMeci().getNrBilete())
            errors.add( "Numarul de bilete cumparate e prea mare!\n");
        if (entity.getBileteCumparate() < 0)
            errors.add( "Numarul de bilete e negativ!\n");
        if (entity.getNumeClient() == null)
            errors.add( "Numele clientului e null!\n");

        if (errors.isEmpty())
            return;

        throw new ValidationException(errors);
    }
}
