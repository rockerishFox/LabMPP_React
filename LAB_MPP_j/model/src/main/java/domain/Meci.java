package domain;

import java.util.Arrays;

public class Meci extends Entity<Integer> {
    private Double pret;
    private Integer nrBilete;
    private String scor;

    private Echipa[] teams;

    public Meci(){
        super(0);
    }

    public Meci(Integer integer, Double pret, Integer nrBilete, String scor, Echipa[] teams) {
        super(integer);
        this.pret = pret;
        this.nrBilete = nrBilete;
        this.scor = scor;
        this.teams = teams;

    }

    public Meci(Integer id, Double pret, Integer nrBilete, String scor, Echipa team1, Echipa team2) {
        super(id);
        this.pret = pret;
        this.nrBilete = nrBilete;
        this.scor = scor;
        this.teams = new Echipa[] {team1,team2};
    }

    public Echipa getEchipa1(){
        return teams[0];
    }

    public Echipa getEchipa2(){
        return teams[1];
    }

    public void setEchipa1(Echipa echipa1){
        this.teams[0] = echipa1;
    }

    public void setEchipa2(Echipa echipa2){
        this.teams[0] = echipa2;
    }


    public Double getPret() {
        return pret;
    }

    public void setPret(Double pret) {
        this.pret = pret;
    }

    public Integer getNrBilete() {
        return nrBilete;
    }

    public void setNrBilete(Integer nrBilete) {
        this.nrBilete = nrBilete;
    }

    public String getScor() {
        return scor;
    }

    public void setScor(String scor) {
        this.scor = scor;
    }

    public Echipa[] getTeams() {
        return teams;
    }

    public void setTeams(Echipa[] teams) {
        this.teams = teams;
    }

    @Override
    public String toString() {
        return "Meci{" +
                "pret=" + pret +
                ", nrBilete=" + nrBilete +
                ", scor='" + scor + '\'' +
                ", teams=" + Arrays.toString(teams) +
                '}';
    }
}
