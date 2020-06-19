package domain;

public class MeciDTO extends Entity<Integer>{
    private Double pret;
    private Integer nrBilete;
    private String scor;
    private String descriere;
    private String nrBileteRamase;

    private Echipa echipa1;
    private Echipa echipa2;
    private Meci meci;

    public MeciDTO(Meci m, int bileteCumparate) {
        super(m.getId());
        this.meci = m;
        this.pret = m.getPret();
        this.nrBilete = m.getNrBilete();
        this.scor = m.getScor();
        this.descriere = m.getEchipa1().getNume() + " versus " + m.getEchipa2().getNume();
        this.echipa1 = m.getEchipa1();
        this.echipa2 = m.getEchipa2();
        int rez = m.getNrBilete() - bileteCumparate;
        if (rez > 0){
            this.nrBileteRamase = String.valueOf(rez);
        }
        else this.nrBileteRamase = "SOLD OUT!";
    }

    public Double getPret() {
        return pret;
    }

    public Integer getNrBilete() {
        return nrBilete;
    }

    public String getScor() {
        return scor;
    }

    public String getDescriere() {
        return descriere;
    }

    public String getNrBileteRamase() {
        return nrBileteRamase;
    }

    public Echipa getEchipa1() {
        return echipa1;
    }

    public Echipa getEchipa2() {
        return echipa2;
    }

    public Meci getMeci() {
        return meci;
    }
}
