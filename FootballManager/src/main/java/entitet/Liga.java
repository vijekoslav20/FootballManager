package entitet;

public class Liga extends Entitet{

    private Klub klub;
    private Integer brojBodova;
    private Integer odigranoUtakmica;
    private Integer zabijenoGolova;
    private Integer primljenoGolova;

    public Liga(Long id, Klub klub, Integer brojBodova, Integer odigranoUtakmica, Integer zabijenoGolova, Integer primljenoGolova) {
        super(id);
        this.klub = klub;
        this.brojBodova = brojBodova;
        this.odigranoUtakmica = odigranoUtakmica;
        this.zabijenoGolova = zabijenoGolova;
        this.primljenoGolova = primljenoGolova;
    }

    public Klub getKlub() {
        return klub;
    }

    public void setKlub(Klub klub) {
        this.klub = klub;
    }

    public Integer getBrojBodova() {
        return brojBodova;
    }

    public void setBrojBodova(Integer brojBodova) {
        this.brojBodova = brojBodova;
    }

    public Integer getOdigranoUtakmica() {
        return odigranoUtakmica;
    }

    public void setOdigranoUtakmica(Integer odigranoUtakmica) {
        this.odigranoUtakmica = odigranoUtakmica;
    }

    public Integer getZabijenoGolova() {
        return zabijenoGolova;
    }

    public void setZabijenoGolova(Integer zabijenoGolova) {
        this.zabijenoGolova = zabijenoGolova;
    }

    public Integer getPrimljenoGolova() {
        return primljenoGolova;
    }

    public void setPrimljenoGolova(Integer primljenoGolova) {
        this.primljenoGolova = primljenoGolova;
    }
}
