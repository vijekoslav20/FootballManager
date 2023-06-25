package entitet;

import java.io.Serializable;
import java.time.LocalDate;

public class Osoba extends Entitet implements Serializable {

    private String ime, prezime;
    private LocalDate datumRodenja;

    public Osoba(Long id, String ime, String prezime, LocalDate datumRodenja) {
        super(id);
        this.ime = ime;
        this.prezime = prezime;
        this.datumRodenja = datumRodenja;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public LocalDate getDatumRodenja() {
        return datumRodenja;
    }

    public void setDatumRodenja(LocalDate datumRodenja) {
        this.datumRodenja = datumRodenja;
    }

    @Override
    public String toString() {
        return ime + " " + prezime;
    }
}
