package DTO;

import java.io.Serializable;

public class EntityDTOS<ID> implements Serializable {
    private ID id;

    public EntityDTOS(ID id) {
        this.id=id;
    }

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }

}
