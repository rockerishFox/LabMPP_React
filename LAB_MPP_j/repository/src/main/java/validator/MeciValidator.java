package validator;

import domain.Meci;

import java.util.ArrayList;
import java.util.List;

public class MeciValidator implements IValidator<Meci> {
    @Override
    public void validate(Meci entity) throws ValidationException {
        if (entity == null)
            throw new ValidationException("Meci null!");
        List<String> errors = new ArrayList<>();

        if (entity.getEchipa1() == null)
            errors.add("Echipa 1 null!\n");
        if (entity.getEchipa2() == null)
            errors.add( "Echipa 2 null!\n");
        if (entity.getEchipa1() == entity.getEchipa2())
            errors.add("Echipe egale!\n");
        if (entity.getPret() < 0)
            errors.add( "Pret negativ!\n");
        if (entity.getNrBilete() < 0)
            errors.add( "Numar de bilete negativ!\n");
        if(entity.getScor() == null)
            errors.add( "Scor null!\n");
        if (!entity.getScor().matches(VlidatorUtils.MECI_SCOR_REGEX))
            errors.add( "Scor invalid!\n");
        if (entity.getTeams().length!=2)
            errors.add( "Meciul nu are doar 2 echipe!\n");


        if (errors.isEmpty())
            return;

        throw new ValidationException(errors);
    }
}
