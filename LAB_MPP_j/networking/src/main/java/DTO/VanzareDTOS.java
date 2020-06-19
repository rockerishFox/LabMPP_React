package DTO;

import java.io.Serializable;

public class VanzareDTOS extends EntityDTOS<Integer> implements Serializable {
    private String numeClient;
    private MeciDTOS meci;
    private int bileteCumparate;

    public VanzareDTOS(Integer integer, String numeClient, MeciDTOS meci, int bileteCumparate) {
        super(integer);
        this.numeClient = numeClient;
        this.meci = meci;
        this.bileteCumparate = bileteCumparate;
    }

    public String getNumeClient() {
        return numeClient;
    }

    public void setNumeClient(String numeClient) {
        this.numeClient = numeClient;
    }

    public MeciDTOS getMeci() {
        return meci;
    }

    public void setMeci(MeciDTOS meci) {
        this.meci = meci;
    }

    public int getBileteCumparate() {
        return bileteCumparate;
    }

    public void setBileteCumparate(int bileteCumparate) {
        this.bileteCumparate = bileteCumparate;
    }
}
