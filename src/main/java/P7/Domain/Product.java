package P7.Domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Product {

    @SequenceGenerator(
            name = "product_sequence",
            sequenceName = "product_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "product_sequence"
    )

    @Id
    @Column(name = "product_nummer")
    private int id;
    private String naam;
    private String beschrijving;
    private Double prijs;

    @ManyToMany(cascade = { CascadeType.DETACH, CascadeType.REFRESH })
    @JoinTable(
            name = "ov_chipkaart_product",
            joinColumns = @JoinColumn(name = "product_nummer"),
            inverseJoinColumns = @JoinColumn( name = "kaart_nummer")
    )
    private List<OVChipkaart> ovchipkaartList = new ArrayList<>();

    public Product(int id, String naam, String beschrijving, Double prijs) {
        this.id = id;
        this.naam = naam;
        this.beschrijving = beschrijving;
        this.prijs = prijs;
    }

    public Product(int id, String naam, String beschrijving, Double prijs, List<OVChipkaart> ovChipkaartList) {
        this.id = id;
        this.naam = naam;
        this.beschrijving = beschrijving;
        this.prijs = prijs;
        this.ovchipkaartList = ovChipkaartList;
    }

    public Product() {
    }

    public List<OVChipkaart> getOvchipkaartList() {
        return ovchipkaartList;
    }

    public void setOvchipkaartList(List<OVChipkaart> ovChipkaartList) {
        this.ovchipkaartList = ovChipkaartList;
    }

    public void addChipkaart(OVChipkaart ovChipkaart) {
        this.ovchipkaartList.add(ovChipkaart);
    }

    public void removeChipkaart(OVChipkaart ovChipkaart) {
        this.ovchipkaartList.remove(ovChipkaart);
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
    public String toString() {;
        return String.format("Product {%s, %s, %s, €%s}", id, naam, beschrijving, prijs);
    }
}
