package entitet;

import iznimke.StadionKoristenDvaputException;

public sealed interface SealedProvjere permits Klub{

    void provjeraStadiona(Stadion stadion) throws StadionKoristenDvaputException;

//    void provjeraStadionaAzuriranje(Stadion stadion, Klub odabraniKlub) throws StadionKoristenDvaputException;

}