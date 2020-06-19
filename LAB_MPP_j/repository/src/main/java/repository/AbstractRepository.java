package repository;

import domain.Entity;
import validator.IValidator;
import validator.ValidationException;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractRepository<ID, T extends Entity<ID>> implements CRUDRepository<ID, T> {
    protected Map<ID, T> entities;
    protected IValidator<T> validator;

    public AbstractRepository(IValidator<T> valid ){
        entities=new HashMap<>();
        validator=valid;
    }

    @Override
    public int size() {
        return entities.size();
    }

    @Override
    public void update(ID id, T entity) {
        try {
            validator.validate(entity);
        }catch(ValidationException ex){
            System.err.println("Entitatea " +entity+ " nu este valida");
            throw ex;
        }

        if (!(entities.get(id)==null)) {
            if (!id.equals(entity.getId()))
                if (entities.get(entity.getId())!=null)
                    throw new RepositoryException("Id-ul "+entity.getId()+" deja axista!!");
            entities.put(entity.getId(), entity);
            System.out.println("Entitate modificata " + entity);
        }else
            throw new RepositoryException("Id-ul "+id +" nu exista!");
    }

    @Override
    public void save(T entity) {
        try {
            validator.validate(entity);
        }catch(ValidationException ex){
            System.err.println("Entitatea " +entity+ " nu este valida");
            throw ex;
        }

        ID id = entity.getId();
        if (entities.get(id)==null) {
            entities.put(id, entity);
        }else
            throw new RepositoryException("Id-ul " + id + " deja exista!");

    }

    @Override
    public void delete(ID id) {
        entities.remove(id);
        System.out.println("Entitatea cu id-ul "+ id +" s-a sters cu succes!");
    }

    @Override
    public T findOne(ID id) {
        T res=entities.get(id);
        if (res!=null)
            return res;
        throw new RepositoryException("Nu s-a gasit id-ul "+id);
    }

    @Override
    public Iterable<T> findAll() {
        return entities.values();
    }

}

