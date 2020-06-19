package DTO;

import java.io.Serializable;

public class EchipaDTOS extends EntityDTOS<Integer> implements Serializable{

    private String nume;
    private String oras;
    private Integer nrJucatori;

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getOras() {
        return oras;
    }

    public void setOras(String oras) {
        this.oras = oras;
    }

    public Integer getNrJucatori() {
        return nrJucatori;
    }

    public void setNrJucatori(Integer nrJucatori) {
        this.nrJucatori = nrJucatori;
    }

    public EchipaDTOS(Integer integer, String nume, String oras, Integer nrJucatori) {
        super(integer);
        this.nume = nume;
        this.oras = oras;
        this.nrJucatori = nrJucatori;
    }
}
