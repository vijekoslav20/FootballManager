package entitet;

import java.io.Serializable;
import java.time.LocalDate;

public class Trener extends Osoba implements Serializable {

    private String drzavljanstvo, omiljenaFormacija;

    public Trener(Long id, String ime, String prezime, LocalDate datumRodenja, String drzavljanstvo, String omiljenaFormacija) {
        super(id, ime, prezime, datumRodenja);
        this.drzavljanstvo = drzavljanstvo;
        this.omiljenaFormacija = omiljenaFormacija;
    }

    public String getDrzavljanstvo() {
        return drzavljanstvo;
    }

    public String getOmiljenaFormacija() {
        return omiljenaFormacija;
    }

    public static class TrenerBuilder {
        private Long id;
        private String ime, prezime;
        private LocalDate datumRodenja;
        private String drzavljanstvo;
        private String omiljenaFormacija;

        public TrenerBuilder(Long id) {
            this.id = id;
        }

        public TrenerBuilder setIme(String ime) {
            this.ime = ime;
            return this;
        }

        public TrenerBuilder setPrezime(String prezime) {
            this.prezime = prezime;
            return this;
        }

        public TrenerBuilder setDatumRodenja(LocalDate datumRodenja) {
            this.datumRodenja = datumRodenja;
            return this;
        }

        public TrenerBuilder setDrzavljanstvo(String drzavljanstvo) {
            this.drzavljanstvo = drzavljanstvo;
            return this;
        }

        public TrenerBuilder setOmiljenaFormacija(String omiljenaFormacija) {
            this.omiljenaFormacija = omiljenaFormacija;
            return this;
        }

        public Trener createTrener() {
            return new Trener(id, ime, prezime, datumRodenja, drzavljanstvo, omiljenaFormacija);
        }
    }
}
