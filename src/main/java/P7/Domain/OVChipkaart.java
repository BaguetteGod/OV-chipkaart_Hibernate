package P7.Domain;

import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ov_chipkaart")
public class OVChipkaart {

    @Id
    @Column(name = "kaart_nummer")
    private int id;
    private Date geldig_tot;
    private int klasse;
    private Double saldo;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "reiziger_id")
    private Reiziger reiziger;

    @ManyToMany(mappedBy = "ovchipkaartList", cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    private List<Product> productList = new ArrayList<>();

    public OVChipkaart(int id, Date geldig_tot, int klasse, Double saldo, Reiziger reiziger) {
        this.id = id;
        this.geldig_tot = geldig_tot;
        this.klasse = klasse;
        this.saldo = saldo;
        this.reiziger = reiziger;
    }

    public OVChipkaart(int id, Date geldig_tot, int klasse, Double saldo, Reiziger reiziger, List<Product> productList) {
        this.id = id;
        this.geldig_tot = geldig_tot;
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
        this.productList.add(product);
        product.addChipkaart(this);
    }

    public void removeProduct(Product product) {
        this.productList.remove(product);
        product.removeChipkaart(this);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getGeldig_tot() {
        return geldig_tot;
    }

    public void setGeldig_tot(Date geldig_tot) {
        this.geldig_tot = geldig_tot;
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
        return String.format("OVChipkaart {%s, Geldig tot: %s, Klasse: %s, €%s, %s}", id, geldig_tot, klasse, saldo, producten);
    }
}
