package P6.Domain;

import java.util.ArrayList;
import java.util.List;

public class Product {
    private int id;
    private String naam;
    private String beschrijving;
    private Double prijs;
    private List<Integer> ovChipkaartList = new ArrayList<>();

    public Product(int id, String naam, String beschrijving, Double prijs) {
        this.id = id;
        this.naam = naam;
        this.beschrijving = beschrijving;
        this.prijs = prijs;
    }

    public Product(int id, String naam, String beschrijving, Double prijs, List<Integer> ovChipkaartList) {
        this.id = id;
        this.naam = naam;
        this.beschrijving = beschrijving;
        this.prijs = prijs;
        this.ovChipkaartList = ovChipkaartList;
    }

    public List<Integer> getOvChipkaartList() {
        return ovChipkaartList;
    }

    public void setOvChipkaartList(List<Integer> ovChipkaartList) {
        this.ovChipkaartList = ovChipkaartList;
    }

    public void addOvChipkaart(int ovChipkaart) {
        this.ovChipkaartList.add(ovChipkaart);
    }

    public void removeChipkaart(int ovChipkaart) {
        this.ovChipkaartList.remove(ovChipkaart);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public String getBeschrijving() {
        return beschrijving;
    }

    public void setBeschrijving(String beschrijving) {
        this.beschrijving = beschrijving;
    }

    public Double getPrijs() {
        return prijs;
    }

    public void setPrijs(Double prijs) {
        this.prijs = prijs;
    }

    @Override
    public String toString() {
        return String.format("Product {%s, %s, %s, €%s, %s}", id, naam, beschrijving, prijs, ovChipkaartList);
    }
}
