package baza;

import entitet.*;
import iznimke.BazaPodatakaException;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class BazaPodataka {

    public static Connection spajanjeNaBazu() throws SQLException, IOException {

        Properties configuration = new Properties();
        configuration.load(new FileReader("dat/properties.txt"));

        String databaseURL = configuration.getProperty("databaseURL");
        String databaseUsername = configuration.getProperty("databaseUsername");
        String databasePassword = configuration.getProperty("databasePassword");

        Connection connection = DriverManager
                .getConnection(databaseURL, databaseUsername, databasePassword);
        return connection;
    }

    public static List<Igrac> dohvatiIgrace() throws BazaPodatakaException {

        List<Igrac> listaIgraca = new ArrayList<>();
        try (Connection veza = spajanjeNaBazu()) {

            Statement upit = veza.createStatement();
            ResultSet resultSet = upit.executeQuery("SELECT * FROM igrac");

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String ime = resultSet.getString("ime");
                String prezime = resultSet.getString("prezime");
                LocalDate datumRodenja = resultSet.getTimestamp("datum_rodenja")
                        .toInstant().atZone(ZoneId.systemDefault())
                        .toLocalDate();
                Integer vrijednost = resultSet.getInt("vrijednost");
                String pozicija = resultSet.getString("pozicija");
                String drzavljanstvo = resultSet.getString("drzavljanstvo");
                Long klubId = resultSet.getLong("klub_id");

                Igrac noviIgrac = new Igrac(id, ime, prezime, datumRodenja, vrijednost, pozicija, drzavljanstvo, klubId);
                listaIgraca.add(noviIgrac);
            }
        } catch (SQLException | IOException ex) {
            String poruka = "Došlo je do pogreške u radu s bazom podataka";
            throw new BazaPodatakaException(poruka, ex);
        }
        return listaIgraca;
    }

    public static List<Trener> dohvatiTrenere() throws BazaPodatakaException {

        List<Trener> listaTrenera = new ArrayList<>();
        try (Connection veza = spajanjeNaBazu()) {

            Statement upit = veza.createStatement();
            ResultSet resultSet = upit.executeQuery("SELECT * FROM trener");

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String ime = resultSet.getString("ime");
                String prezime = resultSet.getString("prezime");
                LocalDate datumRodenja = resultSet.getTimestamp("datum_rodenja")
                        .toInstant().atZone(ZoneId.systemDefault())
                        .toLocalDate();
                String drzavljanstvo = resultSet.getString("drzavljanstvo");
                String omiljenaFormacija = resultSet.getString("omiljena_formacija");

                Trener noviTrener = new Trener(id, ime, prezime, datumRodenja, drzavljanstvo, omiljenaFormacija);
                listaTrenera.add(noviTrener);
            }
        } catch (SQLException | IOException ex) {
            String poruka = "Došlo je do pogreške u radu s bazom podataka";
            throw new BazaPodatakaException(poruka, ex);
        }
        return listaTrenera;
    }

    public static List<Stadion> dohvatiStadione() throws BazaPodatakaException {

        List<Stadion> listaStadiona = new ArrayList<>();
        try (Connection veza = spajanjeNaBazu()) {

            Statement upit = veza.createStatement();
            ResultSet resultSet = upit.executeQuery("SELECT * FROM stadion");

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String nazivStadiona = resultSet.getString("naziv_stadiona");
                Integer kapacitet = resultSet.getInt("kapacitet");

                Stadion noviStadion = new Stadion(id, nazivStadiona, kapacitet);
                listaStadiona.add(noviStadion);
            }
        } catch (SQLException | IOException ex) {
            String poruka = "Došlo je do pogreške u radu s bazom podataka";
            throw new BazaPodatakaException(poruka, ex);
        }
        return listaStadiona;
    }

    public static List<Klub> dohvatiKlubove() throws BazaPodatakaException {

        List<Klub> listaKlubova = new ArrayList<>();
        try (Connection veza = spajanjeNaBazu()) {

            Statement upit = veza.createStatement();
            ResultSet resultSet = upit.executeQuery("SELECT * FROM klub");

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String nazivKluba = resultSet.getString("naziv_kluba");
                String drzava = resultSet.getString("drzava");
                Long trenerId = resultSet.getLong("trener_id");
                Long stadionId = resultSet.getLong("stadion_id");

                Klub noviKlub = new Klub(id, nazivKluba, drzava, odaberiTreneraZaPretraguKluba(veza, trenerId), odaberiStadionZaPretraguKluba(veza, stadionId), new ArrayList<Igrac>());
                listaKlubova.add(noviKlub);
            }
        } catch (SQLException | IOException ex) {
            String poruka = "Došlo je do pogreške u radu s bazom podataka";
            throw new BazaPodatakaException(poruka, ex);
        }
        return listaKlubova;
    }

    private static Trener odaberiTreneraZaPretraguKluba(Connection veza, Long idTrener) throws SQLException {
        Statement upitTrener = veza.createStatement();
        ResultSet resultSetTrener = upitTrener.executeQuery("SELECT * FROM trener WHERE id=" + idTrener);
        Trener trener = null;
        while (resultSetTrener.next()){
            if(idTrener.equals(resultSetTrener.getLong("id"))){
                trener = new Trener(
                        resultSetTrener.getLong("id"),
                        resultSetTrener.getString("ime"),
                        resultSetTrener.getString("prezime"),
                        resultSetTrener.getTimestamp("datum_rodenja")
                                .toInstant().atZone(ZoneId.systemDefault())
                                .toLocalDate(),
                        resultSetTrener.getString("drzavljanstvo"),
                        resultSetTrener.getString("omiljena_formacija")
                );
            }
        }
        return trener;
    }

    private static Stadion odaberiStadionZaPretraguKluba(Connection veza, Long idStadion) throws SQLException {
        Statement upitStadion = veza.createStatement();
        ResultSet resultSetStadion = upitStadion.executeQuery("SELECT * FROM stadion WHERE id=" + idStadion);
        Stadion stadion = null;
        while (resultSetStadion.next()){
            if(idStadion.equals(resultSetStadion.getLong("id"))){
                stadion = new Stadion(
                        resultSetStadion.getLong("id"),
                        resultSetStadion.getString("naziv_stadiona"),
                        resultSetStadion.getInt("kapacitet")
                );
            }
        }
        return stadion;
    }

    public static void spremiNovogIgraca(Igrac igrac) throws BazaPodatakaException {
        try (Connection veza = spajanjeNaBazu()) {
            PreparedStatement preparedStatement = veza
                    .prepareStatement("INSERT INTO IGRAC(ime, prezime, datum_rodenja, vrijednost, pozicija, drzavljanstvo, klub_id) VALUES (?, ?, ?, ?, ?, ?, ?)");
            preparedStatement.setString(1, igrac.getIme());
            preparedStatement.setString(2, igrac.getPrezime());
            preparedStatement.setDate(3, Date.valueOf(igrac.getDatumRodenja()));
            preparedStatement.setInt(4, igrac.getVrijednost());
            preparedStatement.setString(5, igrac.getPozicija());
            preparedStatement.setString(6, igrac.getDrzavljanstvo());
            preparedStatement.setLong(7, igrac.getKlubId());
            preparedStatement.executeUpdate();
        } catch (SQLException | IOException ex) {
            String poruka = "Došlo je do pogreške u radu s bazom podataka - Spremanje igrača";
            throw new BazaPodatakaException(poruka, ex);
        }
    }

    public static void spremiNovogTrenera(Trener trener) throws BazaPodatakaException {
        try (Connection veza = spajanjeNaBazu()) {
            PreparedStatement preparedStatement = veza
                    .prepareStatement("INSERT INTO TRENER(ime, prezime, datum_rodenja, drzavljanstvo, omiljena_formacija) VALUES (?, ?, ?, ?, ?)");
            preparedStatement.setString(1, trener.getIme());
            preparedStatement.setString(2, trener.getPrezime());
            preparedStatement.setDate(3, Date.valueOf(trener.getDatumRodenja()));
            preparedStatement.setString(4, trener.getDrzavljanstvo());
            preparedStatement.setString(5, trener.getOmiljenaFormacija());
            preparedStatement.executeUpdate();
        } catch (SQLException | IOException ex) {
            String poruka = "Došlo je do pogreške u radu s bazom podataka - Spremanje trenera";
            throw new BazaPodatakaException(poruka, ex);
        }
    }

    public static void spremiNoviStadion(Stadion stadion) throws BazaPodatakaException {
        try (Connection veza = spajanjeNaBazu()) {
            PreparedStatement preparedStatement = veza
                    .prepareStatement("INSERT INTO STADION(naziv_stadiona, kapacitet) VALUES (?, ?)");
            preparedStatement.setString(1, stadion.nazivStadiona());
            preparedStatement.setInt(2, stadion.kapacitet());
            preparedStatement.executeUpdate();
        } catch (SQLException | IOException ex) {
            String poruka = "Došlo je do pogreške u radu s bazom podataka - Spremanje stadiona";
            throw new BazaPodatakaException(poruka, ex);
        }
    }

    public static void spremiNoviKlub(Klub klub) throws BazaPodatakaException {
        try (Connection veza = spajanjeNaBazu()) {
            PreparedStatement preparedStatement = veza
                    .prepareStatement("INSERT INTO KLUB(naziv_kluba, drzava, trener_id, stadion_id) VALUES (?, ?, ?, ?)");
            preparedStatement.setString(1, klub.getNazivKluba());
            preparedStatement.setString(2, klub.getDrzava());
            preparedStatement.setLong(3, klub.getTrener().getId());
            preparedStatement.setLong(4, klub.getStadion().id());
            preparedStatement.executeUpdate();
        } catch (SQLException | IOException ex) {
            String poruka = "Došlo je do pogreške u radu s bazom podataka - Spremanje kluba";
            throw new BazaPodatakaException(poruka, ex);
        }
    }

    public static void obrisiIgraca(Igrac igrac) throws BazaPodatakaException {
        try (Connection veza = spajanjeNaBazu()) {

            PreparedStatement deleteStmt = veza
                    .prepareStatement("DELETE FROM igrac WHERE id =" + igrac.getId());

            deleteStmt.executeUpdate();
        } catch (SQLException | IOException ex) {
            String poruka = "Pogreška pri brisanju igrača!";
            throw new BazaPodatakaException(poruka, ex);
        }
    }

    public static void obrisiTrenera(Trener trener) throws BazaPodatakaException {
        try (Connection veza = spajanjeNaBazu()) {

            PreparedStatement deleteStmt = veza
                    .prepareStatement("DELETE FROM trener WHERE id =" + trener.getId());

            deleteStmt.executeUpdate();
        } catch (SQLException | IOException ex) {
            String poruka = "Pogreška pri brisanju trenera!";
            throw new BazaPodatakaException(poruka, ex);
        }
    }

    public static void obrisiStadion(Stadion stadion) throws BazaPodatakaException {
        try (Connection veza = spajanjeNaBazu()) {

            PreparedStatement deleteStmt = veza
                    .prepareStatement("DELETE FROM stadion WHERE id =" + stadion.id());

            deleteStmt.executeUpdate();
        } catch (SQLException | IOException ex) {
            String poruka = "Pogreška pri brisanju stadiona!";
            throw new BazaPodatakaException(poruka, ex);
        }
    }

    public static void obrisiKlub(Klub klub) throws BazaPodatakaException {
        try (Connection veza = spajanjeNaBazu()) {

            PreparedStatement deleteStmt = veza
                    .prepareStatement("DELETE FROM klub WHERE id =" + klub.getId());

            deleteStmt.executeUpdate();
        } catch (SQLException | IOException ex) {
            String poruka = "Pogreška pri brisanju kluba!";
            throw new BazaPodatakaException(poruka, ex);
        }
    }

    public static void azurirajIgraca(Igrac igrac, String ime, String prezime, LocalDate datumRodenja, String vrijednost, String pozicija, String drzavljanstvo, String imeKluba) throws BazaPodatakaException {
        try (Connection veza = spajanjeNaBazu()) {

            Long idKlub = getKlubWithNaziv(imeKluba).getId();

            PreparedStatement updateStmt = veza
                    .prepareStatement("UPDATE igrac SET ime = '" + ime + "', prezime = '" + prezime + "', datum_rodenja = '" +
                            datumRodenja + "', vrijednost =  + '" + vrijednost + "', pozicija = '" + pozicija + "', drzavljanstvo = '" +
                            drzavljanstvo + "', klub_id = '" + idKlub + "' WHERE id =" + igrac.getId());

            updateStmt.executeUpdate();
        } catch (SQLException | IOException ex) {
            String poruka = "Pogreška pri ažuriranju igrača!";
            throw new BazaPodatakaException(poruka, ex);
        }
    }

    public static Klub getKlubWithNaziv(String nazivKluba) throws BazaPodatakaException {
        try (Connection veza = spajanjeNaBazu()) {
            Statement upitKlub = veza.createStatement();
            ResultSet resultSetKlub = upitKlub.executeQuery("SELECT * FROM klub WHERE naziv_kluba LIKE '" + nazivKluba + "'");
            Klub klub = null;
            while (resultSetKlub.next()){
                if(nazivKluba.equals(resultSetKlub.getString("naziv_kluba"))){
                    klub = new Klub(
                            resultSetKlub.getLong("id"),
                            resultSetKlub.getString("naziv_kluba"),
                            resultSetKlub.getString("drzava"),
                            getTrener(resultSetKlub.getLong("trener_id")),
                            getStadion(resultSetKlub.getLong("stadion_id")),
                            new ArrayList<Igrac>()
                    );
                }
            }
            return klub;
        } catch (SQLException | IOException ex) {
            String poruka = "Pogreška pri ažuriranju kluba!";
            throw new BazaPodatakaException(poruka, ex);
        }
    }

    public static void azurirajTrenera(Trener trener, String ime, String prezime, LocalDate datumRodenja, String drzavljanstvo, String omiljenaFormacija) throws BazaPodatakaException {
        try (Connection veza = spajanjeNaBazu()) {

            PreparedStatement updateStmt = veza
                    .prepareStatement("UPDATE trener SET ime = '" + ime + "', prezime = '" + prezime + "', datum_rodenja = '" + datumRodenja + "', drzavljanstvo =  + '" +
                            drzavljanstvo + "', omiljena_formacija = '" + omiljenaFormacija + "' WHERE id =" + trener.getId());

            updateStmt.executeUpdate();
        } catch (SQLException | IOException ex) {
            String poruka = "Pogreška pri ažuriranju trenera!";
            throw new BazaPodatakaException(poruka, ex);
        }
    }

    public static void azurirajStadion(Stadion stadion, String nazivStadiona, Integer kapacitet) throws BazaPodatakaException {
        try (Connection veza = spajanjeNaBazu()) {

            PreparedStatement updateStmt = veza
                    .prepareStatement("UPDATE stadion SET naziv_stadiona = '" + nazivStadiona + "', kapacitet = '" + kapacitet + "' WHERE id =" + stadion.id());

            updateStmt.executeUpdate();
        } catch (SQLException | IOException ex) {
            String poruka = "Pogreška pri ažuriranju stadiona!";
            throw new BazaPodatakaException(poruka, ex);
        }
    }

    public static void azurirajKlub(Klub klub, String nazivKluba, String drzava, Long trenerId, Long stadionId) throws BazaPodatakaException {
        try (Connection veza = spajanjeNaBazu()) {

            PreparedStatement updateStmt = veza
                    .prepareStatement("UPDATE klub SET naziv_kluba = '" + nazivKluba + "', drzava = '" + drzava + "', trener_id = '" +
                            trenerId + "', stadion_id =  + '" + stadionId + "' WHERE id =" + klub.getId());

            updateStmt.executeUpdate();
        } catch (SQLException | IOException ex) {
            String poruka = "Pogreška pri ažuriranju kluba!";
            throw new BazaPodatakaException(poruka, ex);
        }
    }

    public static Klub getKlub(Long idKlub) throws BazaPodatakaException {
        try (Connection veza = spajanjeNaBazu()) {
            Statement upitKlub = veza.createStatement();
            ResultSet resultSetKlub = upitKlub.executeQuery("SELECT * FROM klub WHERE id=" + idKlub);
            Klub klub = null;
            while (resultSetKlub.next()){
                if(idKlub.equals(resultSetKlub.getLong("id"))){
                    klub = new Klub(
                            resultSetKlub.getLong("id"),
                            resultSetKlub.getString("naziv_kluba"),
                            resultSetKlub.getString("drzava"),
                            getTrener(resultSetKlub.getLong("trener_id")),
                            getStadion(resultSetKlub.getLong("stadion_id")),
                            new ArrayList<Igrac>()
                    );
                }
            }
            return klub;
        } catch (SQLException | IOException ex) {
            String poruka = "Pogreška pri ažuriranju kluba!";
            throw new BazaPodatakaException(poruka, ex);
        }
    }

    private static Trener getTrener(Long idTrener) throws BazaPodatakaException {
        try (Connection veza = spajanjeNaBazu()) {
            Statement upitTrener = veza.createStatement();
            ResultSet resultSetTrener = upitTrener.executeQuery("SELECT * FROM trener WHERE id=" + idTrener);
            Trener trener = null;
            while (resultSetTrener.next()){
                if(idTrener.equals(resultSetTrener.getLong("id"))){
                    trener = new Trener(
                            resultSetTrener.getLong("id"),
                            resultSetTrener.getString("ime"),
                            resultSetTrener.getString("prezime"),
                            resultSetTrener.getTimestamp("datum_rodenja")
                            .toInstant().atZone(ZoneId.systemDefault())
                            .toLocalDate(),
                            resultSetTrener.getString("drzavljanstvo"),
                            resultSetTrener.getString("omiljena_formacija")
                    );
                }
            }
            return trener;
        } catch (SQLException | IOException ex) {
            String poruka = "Pogreška pri ažuriranju kluba!";
            throw new BazaPodatakaException(poruka, ex);
        }
    }

    private static Stadion getStadion(Long idStadion) throws BazaPodatakaException {
        try (Connection veza = spajanjeNaBazu()) {
            Statement upitStadion = veza.createStatement();
            ResultSet resultSetStadion = upitStadion.executeQuery("SELECT * FROM stadion WHERE id=" + idStadion);
            Stadion stadion = null;
            while (resultSetStadion.next()){
                if(idStadion.equals(resultSetStadion.getLong("id"))){
                    stadion = new Stadion(
                            resultSetStadion.getLong("id"),
                            resultSetStadion.getString("naziv_stadiona"),
                            resultSetStadion.getInt("kapacitet")
                    );
                }
            }
            return stadion;
        } catch (SQLException | IOException ex) {
            String poruka = "Pogreška pri ažuriranju kluba!";
            throw new BazaPodatakaException(poruka, ex);
        }
    }

    public static List<Liga> dohvatiLige() throws BazaPodatakaException {

        List<Liga> listaKlubovaLige = new ArrayList<>();
        try (Connection veza = spajanjeNaBazu()) {

            Statement upit = veza.createStatement();
            ResultSet resultSet = upit.executeQuery("SELECT * FROM liga");

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                Long klubId = resultSet.getLong("klub_id");
                Integer bodovi = resultSet.getInt("bodovi");
                Integer odigrano = resultSet.getInt("odigrano");
                Integer zabijeno = resultSet.getInt("zabijeno");
                Integer primljeno = resultSet.getInt("primljeno");


                Liga novaLigaPozicija = new Liga(id, getKlub(klubId), bodovi, odigrano, zabijeno, primljeno);
                listaKlubovaLige.add(novaLigaPozicija);
            }
        } catch (SQLException | IOException ex) {
            String poruka = "Došlo je do pogreške u radu s bazom podataka";
            throw new BazaPodatakaException(poruka, ex);
        }
        return listaKlubovaLige;
    }

    public static void azurirajLigu(Long idKlub, Integer bodovi, Integer odigrano, Integer zabijeno, Integer primljeno) throws BazaPodatakaException {
        try (Connection veza = spajanjeNaBazu()) {

            PreparedStatement updateStmt = veza
                    .prepareStatement("UPDATE liga SET bodovi = bodovi + '" + bodovi
                            + "', odigrano = odigrano + '" + odigrano
                            + "', zabijeno = zabijeno + '" + zabijeno
                            + "', primljeno = primljeno + '" + primljeno
                            + "' WHERE klub_id =" + idKlub);

            updateStmt.executeUpdate();
        } catch (SQLException | IOException ex) {
            String poruka = "Pogreška pri ažuriranju kluba!";
            throw new BazaPodatakaException(poruka, ex);
        }
    }
}
