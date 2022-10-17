package P6.Domain;


import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ov_chipkaart")
public class OVChipkaart {
    @SequenceGenerator(
            name = "ovchipkaart_sequence",
            sequenceName = "ovchipkaart_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "ovchipkaart_sequence"
    )
    @Id
    @Column(name = "kaart_nummer")
    private int id;
    @Column(name = "geldig_tot")
    private Date geldigTot;
    private int klasse;
    private Double saldo;
    @ManyToOne
    @JoinColumn(name = "reiziger_id")
    private Reiziger reiziger;
    @ManyToMany(mappedBy = "ovChipkaartList")
    private List<Product> productList = new ArrayList<>();

    public OVChipkaart(int id, Date geldigTot, int klasse, Double saldo, Reiziger reiziger) {
        this.id = id;
        this.geldigTot = geldigTot;
        this.klasse = klasse;
        this.saldo = saldo;
        this.reiziger = reiziger;
    }

    public OVChipkaart(int id, Date geldigTot, int klasse, Double saldo, Reiziger reiziger, List<Product> productList) {
        this.id = id;
        this.geldigTot = geldigTot;
        this.klasse = klasse;
        this.saldo = saldo;
        this.reiziger = reiziger;
        this.productList = productList;
    }

    public OVChipkaart() {
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public void addProduct(Product product) {
        if (productList == null || !productList.contains(product)) {
            this.productList = new ArrayList<>();
        }
        this.productList.add(product);
    }

    public void removeProduct(Product product) {
        this.productList.remove(product);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getGeldigTot() {
        return geldigTot;
    }

    public void setGeldigTot(Date geldigTot) {
        this.geldigTot = geldigTot;
    }

    public int getKlasse() {
        return klasse;
    }

    public void setKlasse(int klasse) {
        this.klasse = klasse;
    }

    public Double getSaldo() {
        return saldo;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }

    public Reiziger getReiziger() {
        return reiziger;
    }

    public void setReiziger(Reiziger reiziger) {
        this.reiziger = reiziger;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        if (productList != null) {
            for (Product p : productList) {
                stringBuilder.append(p);
                stringBuilder.append(", ");
            }
        }
        String producten = stringBuilder.toString();
        return String.format("OVChipkaart {%s, Geldig tot: %s, Klasse: %s, €%s, %s}", id, geldigTot, klasse, saldo, producten);
    }
}
