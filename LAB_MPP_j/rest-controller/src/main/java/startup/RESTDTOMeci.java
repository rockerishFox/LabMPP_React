package startup;

import domain.Meci;

public class RESTDTOMeci {
    private Meci meci;
    private int nrBileteRamase;
    public RESTDTOMeci(){
    }

    public RESTDTOMeci(Meci meci, int nrBileteRamase) {
        this.meci = meci;
        this.nrBileteRamase = meci.getNrBilete() - nrBileteRamase;
    }

    public Meci getMeci() {
        return meci;
    }

    public void setMeci(Meci meci) {
        this.meci = meci;
    }

    public int getNrBileteRamase() {
        return nrBileteRamase;
    }

    public void setNrBileteRamase(int nrBileteRamase) {
        this.nrBileteRamase = nrBileteRamase;
    }
}
