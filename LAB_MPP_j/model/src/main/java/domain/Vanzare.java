package domain;

public class Vanzare extends Entity<Integer> {
    private String numeClient;
    private Meci meci;
    private int bileteCumparate;

    public Vanzare(){
        super(0);
    };

    public Vanzare(Integer integer, String numeClient, Meci meci, int bileteCumparate) {
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

    public Meci getMeci() {
        return meci;
    }

    public void setMeci(Meci meci) {
        this.meci = meci;
    }

    public int getBileteCumparate() {
        return bileteCumparate;
    }

    public void setBileteCumparate(int bileteCumparate) {
        this.bileteCumparate = bileteCumparate;
    }
}
