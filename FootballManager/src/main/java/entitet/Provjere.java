package entitet;

import iznimke.ZauzetiTrenerException;

public interface Provjere {

    void provjeraTrenera(Trener trener) throws ZauzetiTrenerException;

//    void provjeraTreneraAzuriranje(Trener trener, Klub odabraniKlub) throws ZauzetiTrenerException;

}
