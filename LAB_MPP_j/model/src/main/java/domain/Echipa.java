package domain;

public class Echipa extends Entity<Integer>{

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

    public Echipa(){
        super(0);
    }

    public Echipa(Integer integer, String nume, String oras, Integer nrJucatori) {
        super(integer);
        this.nume = nume;
        this.oras = oras;
        this.nrJucatori = nrJucatori;
    }

    @Override
    public String toString() {
        return this.nume;
    }
}
