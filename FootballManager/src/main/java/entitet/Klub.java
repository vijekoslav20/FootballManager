package entitet;

import baza.BazaPodataka;
import iznimke.StadionKoristenDvaputException;
import iznimke.ZauzetiTrenerException;
import javafx.scene.control.Alert;

import java.io.Serializable;
import java.util.List;

public final class Klub extends Entitet implements Serializable, Provjere, SealedProvjere {

    private String nazivKluba, drzava;
    private Trener trener;
    private Stadion stadion;
    private List<Igrac> igraci;

    public Klub(Long id, String nazivKluba, String drzava, Trener trener, Stadion stadion, List<Igrac> igraci) {
        super(id);
        this.nazivKluba = nazivKluba;
        this.drzava = drzava;
        this.trener = trener;
        this.stadion = stadion;
        this.igraci = igraci;
    }

    public String getNazivKluba() {
        return nazivKluba;
    }

    public void setNazivKluba(String nazivKluba) {
        this.nazivKluba = nazivKluba;
    }

    public String getDrzava() {
        return drzava;
    }

    public void setDrzava(String drzava) {
        this.drzava = drzava;
    }

    public Trener getTrener() {
        return trener;
    }

    public void setTrener(Trener trener) {
        this.trener = trener;
    }

    public Stadion getStadion() {
        return stadion;
    }

    public void setStadion(Stadion stadion) {
        this.stadion = stadion;
    }

    public List<Igrac> getIgraci() {
        return igraci;
    }

    public void setIgraci(List<Igrac> igraci) {
        this.igraci = igraci;
    }

    @Override
    public String toString() {
        return nazivKluba;
    }

    @Override
    public void provjeraTrenera(Trener trener) throws ZauzetiTrenerException {

        List<Klub> klubovi = BazaPodataka.dohvatiKlubove();

        for(Klub klub : klubovi){
            if(klub.getTrener().getId().equals(trener.getId())){
                throw new ZauzetiTrenerException("Trener " + klub.getTrener().toString() + " već trenira " + klub.getNazivKluba());
            }
        }
    }

//    @Override
//    public void provjeraTreneraAzuriranje(Trener trener, Klub odabraniKlub) throws ZauzetiTrenerException {
//        List<Klub> klubovi = BazaPodataka.dohvatiKlubove();
//
//        int brojacTrenera = 0;
//
//        klubovi.remove(odabraniKlub);
//
//        for(Klub klub : klubovi){
//            if(klub.getTrener().getId().equals(trener.getId())){
//                brojacTrenera++;
//            }
//        }
//
//        if(brojacTrenera >= 1){
//            throw new ZauzetiTrenerException("Trener " + trener.toString() + " već trenira drugi klub!");
//        }
//    }

    @Override
    public void provjeraStadiona(Stadion stadion) throws StadionKoristenDvaputException {
        List<Klub> klubovi = BazaPodataka.dohvatiKlubove();

        Long brojKlubova = klubovi.stream().filter(k -> k.getStadion().id().equals(stadion.id())).count();

        if(brojKlubova >= 2){
            throw new StadionKoristenDvaputException("Stadion " + stadion.nazivStadiona() + " već koristi dva kluba!");
        }
    }

//    @Override
//    public void provjeraStadionaAzuriranje(Stadion stadion, Klub odabraniKlub) throws StadionKoristenDvaputException {
//        List<Klub> klubovi = BazaPodataka.dohvatiKlubove();
//
//        int brojacStadiona = 0;
//
//        klubovi.remove(odabraniKlub);
//
//        for(Klub klub : klubovi){
//            if(klub.getStadion().id().equals(stadion.id())){
//                brojacStadiona++;
//            }
//        }
//
//        if(brojacStadiona >= 2){
//            throw new StadionKoristenDvaputException("Stadion " + stadion.nazivStadiona() + " već koristi dva kluba!");
//        }
//    }
}
