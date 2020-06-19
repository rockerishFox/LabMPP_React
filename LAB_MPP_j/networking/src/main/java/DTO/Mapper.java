package DTO;

import domain.Echipa;
import domain.Meci;
import domain.Utilizator;
import domain.Vanzare;

import java.util.ArrayList;

public class Mapper {

    //  GET DTOS
    public static UtilizatorDTOS toUtilizatorDTOS(Utilizator user) {
        return new UtilizatorDTOS(user.getId(), user.getUsername(), user.getPassword());
    }

    public static EchipaDTOS toEchipaDTOS(Echipa echipa) {
        return new EchipaDTOS(echipa.getId(), echipa.getNume(), echipa.getOras(), echipa.getNrJucatori());
    }

    public static MeciDTOS toMeciDTOS(Meci meci) {
        EchipaDTOS e1 = toEchipaDTOS(meci.getEchipa1());
        EchipaDTOS e2 = toEchipaDTOS(meci.getEchipa2());
        return new MeciDTOS(meci.getId(), meci.getPret(), meci.getNrBilete(), meci.getScor(), e1, e2);
    }
    public static Iterable<MeciDTOS> toMeciDTOS(Iterable<Meci> meci) {
        ArrayList<MeciDTOS> meciuri = new ArrayList<>();
        for(Meci m : meci){
            EchipaDTOS e1 = toEchipaDTOS(m.getEchipa1());
            EchipaDTOS e2 = toEchipaDTOS(m.getEchipa2());
            meciuri.add(new MeciDTOS(m.getId(), m.getPret(), m.getNrBilete(), m.getScor(), e1, e2));
        }
        return meciuri;
    }

    public static VanzareDTOS toVanzaareDTOS(Vanzare v) {
        MeciDTOS e1 = toMeciDTOS(v.getMeci());
        return new VanzareDTOS(v.getId(), v.getNumeClient(), e1, v.getBileteCumparate());
    }


    //  GET ENTITIES FROM DTOS
    public static Utilizator fromUtilizatorDTOS(UtilizatorDTOS user) {
        return new Utilizator(user.getId(), user.getUsername(), user.getPassword());
    }

    public static Echipa fromEchipaDTOS(EchipaDTOS ehcipa) {
        return new Echipa(ehcipa.getId(), ehcipa.getNume(), ehcipa.getOras(), ehcipa.getNrJucatori());
    }

    public static Meci fromMeciDTOS(MeciDTOS meci) {
        Echipa e1 = fromEchipaDTOS(meci.getEchipa1());
        Echipa e2 = fromEchipaDTOS(meci.getEchipa2());
        return new Meci(meci.getId(), meci.getPret(), meci.getNrBilete(), meci.getScor(), e1, e2);
    }

    public static Iterable<Meci> fromMeciDTOS(Iterable<MeciDTOS> meci) {
        ArrayList<Meci> meciuri = new ArrayList<>();
        for(MeciDTOS m : meci){
            Echipa e1 = fromEchipaDTOS(m.getEchipa1());
            Echipa e2 = fromEchipaDTOS(m.getEchipa2());
            meciuri.add(new Meci(m.getId(), m.getPret(), m.getNrBilete(), m.getScor(), e1, e2));
        }
        return meciuri;
    }
    public static Vanzare fromVanzareDTOS(VanzareDTOS v) {
        Meci m = fromMeciDTOS(v.getMeci());
        return new Vanzare(v.getId(), v.getNumeClient(), m, v.getBileteCumparate());
    }
}
