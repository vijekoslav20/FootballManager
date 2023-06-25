package entitet;

import java.io.Serializable;
import java.time.LocalDate;

public class Igrac extends Osoba implements Serializable {

    private Integer vrijednost;
    private String pozicija, drzavljanstvo;
    private Long klubId;

    public Igrac(Long id, String ime, String prezime, LocalDate datumRodenja, Integer vrijednost, String pozicija, String drzavljanstvo, Long klubId) {
        super(id, ime, prezime, datumRodenja);
        this.vrijednost = vrijednost;
        this.pozicija = pozicija;
        this.drzavljanstvo = drzavljanstvo;
        this.klubId = klubId;
    }

    public Integer getVrijednost() {
        return vrijednost;
    }

    public void setVrijednost(Integer vrijednost) {
        this.vrijednost = vrijednost;
    }

    public String getPozicija() {
        return pozicija;
    }

    public void setPozicija(String pozicija) {
        this.pozicija = pozicija;
    }

    public String getDrzavljanstvo() {
        return drzavljanstvo;
    }

    public void setDrzavljanstvo(String drzavljanstvo) {
        this.drzavljanstvo = drzavljanstvo;
    }

    public Long getKlubId() {
        return klubId;
    }

    public void setKlubId(Long klubId) {
        this.klubId = klubId;
    }
}
