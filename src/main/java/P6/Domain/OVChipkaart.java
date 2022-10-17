package P6.Domain;


import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class OVChipkaart {
    private int id;
    private Date geldigTot;
    private int klasse;
    private Double saldo;
    private Reiziger reiziger;
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

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public void addProduct(Product product) {
        this.productList.add(product);
        product.addOvChipkaart(this.id);
    }

    public void removeProduct(Product product) {
        this.productList.remove(product);
        product.removeChipkaart(this.id);
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
        return String.format("OVChipkaart {%s, Geldig tot: %s, Klasse: %s, €%s, %s}", id, geldigTot, klasse, saldo, productList);
    }
}
