package DTO;

import java.io.Serializable;

public class MeciDTOS extends EntityDTOS<Integer> implements Serializable {
    private Double pret;
    private Integer nrBilete;
    private String scor;

    private EchipaDTOS[] teams;


    public MeciDTOS(Integer integer, Double pret, Integer nrBilete, String scor, EchipaDTOS[] teams) {
        super(integer);
        this.pret = pret;
        this.nrBilete = nrBilete;
        this.scor = scor;
        this.teams = teams;

    }

    public MeciDTOS(Integer id, Double pret, Integer nrBilete, String scor, EchipaDTOS team1, EchipaDTOS team2) {
        super(id);
        this.pret = pret;
        this.nrBilete = nrBilete;
        this.scor = scor;
        this.teams = new EchipaDTOS[]{team1, team2};
    }

    public EchipaDTOS getEchipa1() {
        return teams[0];
    }

    public EchipaDTOS getEchipa2() {
        return teams[1];
    }

    public void setEchipa1(EchipaDTOS echipa1) {
        this.teams[0] = echipa1;
    }

    public void setEchipa2(EchipaDTOS echipa2) {
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

    public EchipaDTOS[] getTeams() {
        return teams;
    }

    public void setTeams(EchipaDTOS[] teams) {
        this.teams = teams;
    }

}