package P7;

import P7.Data.*;
import P7.Domain.Adres;
import P7.Domain.OVChipkaart;
import P7.Domain.Product;
import P7.Domain.Reiziger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.sql.Date;
import java.util.List;

/**
 * Testklasse - deze klasse test alle andere klassen in deze package.
 *
 * System.out.println() is alleen in deze klasse toegestaan (behalve voor exceptions).
 *
 * @author tijmen.muller@hu.nl
 */
public class Main {
    // Creëer een factory voor Hibernate sessions.
    private static final SessionFactory factory;

    static {
        try {
            // Create a Hibernate session factory
            factory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Retouneer een Hibernate session.
     *
     * @return Hibernate session
     * @throws HibernateException
     */
    private static Session getSession() throws HibernateException {
        return factory.openSession();
    }


    private static void closeConnection() {
        factory.close();
    }

    private static void testReizigerDAO(ReizigerDAO rdao) {
        System.out.println("\n---------- Test ReizigerDAO -------------");

        // Haal alle reizigers op uit de database
        List<Reiziger> reizigers = rdao.findAll();
        System.out.println("[Test] ReizigerDAO.findAll() geeft de volgende reizigers:");
        for (Reiziger r : reizigers) {
            System.out.println(r);
        }
        System.out.println();

        // Vind een reiziger op id
        System.out.println("[Test] ReizigerDAO.findById() vindt de volgende reiziger:");
        Reiziger findSietske = rdao.findById(77);
        System.out.println(findSietske);
        System.out.println();

        // Vind een reiziger op geboortedatum
        System.out.println("[Test] ReizigerDAO.findByGbdatum() vindt de volgende reizigers:");
        List<Reiziger> listReizigers = rdao.findByGbdatum("2002-12-03");
        for (Reiziger r : listReizigers) {
            System.out.println(r);
        }
        System.out.println();
        // Verwijder een reiziger uit de database
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.delete() ");
        rdao.delete(findSietske);
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers\n");

        // Maak een nieuwe reiziger aan en persisteer deze in de database
        String gbdatum = "1981-03-14";
        Reiziger sietske = new Reiziger(77, "S", "", "Boers", Date.valueOf(gbdatum));
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.save() ");
        rdao.save(sietske);
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers\n");

        // Update een reiziger en persisteer deze in de database
        String gbdatumUpdate = "1982-03-14";
        Reiziger updateSietske = new Reiziger(77, "S", "", "Harms", Date.valueOf(gbdatumUpdate));
        System.out.print("[Test] Eerst was de data van de reiziger: \n" + rdao.findById(77));
        rdao.update(updateSietske);
        System.out.println("\nEn nu is de data van de reiziger geupdate naar: \n" + rdao.findById(77));
        System.out.println();
    }

    private static void testAdresDAO(AdresDAO adao, ReizigerDAO rdao) {
        System.out.println("\n---------- Test AdresDAO -------------");

        // Haal alle adressen op uit de database
        System.out.println("[Test] AdresDAO.findAll() geeft de volgende adressen:");
        List<Adres> adresList = adao.findAll();
        for (Adres a : adresList) {
            System.out.println(a);
        }
        System.out.println();

        // Vind een adres op basis van een reiziger
        Adres adres = adao.findByReiziger(rdao.findById(1));
        System.out.println("[Test] ReizigerDAO.findByReiziger() vindt het volgende adres: \n" + adres);
        System.out.println();

        // Maak een nieuw adres aan en persisteer deze in de database
        Adres nieuwAdres = new Adres(10, "1234AB", "12", "Jan Willemstraat", "Utrecht", rdao.findById(77));
        System.out.println(nieuwAdres);
        System.out.print("[Test] Eerst " + adresList.size() + " adressen, na AdresDAO.save() ");
        adao.save(nieuwAdres);
        adresList = adao.findAll();
        System.out.println(adresList.size() + " adressen\n");

        // Update een adres uit de database
        String postcodeUpdate = "4321BA";
        Adres updateAdres = new Adres(10, postcodeUpdate, "12", "Jan Willemstraat", "Utrecht", rdao.findById(77));
        System.out.print("[Test] Eerst was de data van het adres: \n" + adao.findByReiziger(rdao.findById(77)));
        adao.update(updateAdres);
        System.out.println("\nEn nu is de data van het adres geupdate naar: \n" + adao.findByReiziger(rdao.findById(77)));
        System.out.println();

        // Verwijder een adres uit de database
        System.out.print("[Test] Eerst " + adresList.size() + " adressen, na AdresDAO.delete() ");
        adao.delete(adao.findByReiziger(rdao.findById(77)));
        adresList = adao.findAll();
        System.out.println(adresList.size() + " reizigers\n");

    }

    private static void testOVChipkaartDAO(OVChipkaartDAO ovcdao, ReizigerDAO rdao, ProductDAO pdao) {
        System.out.println("\n---------- Test OVChipkaartDAO -------------");

        // Haal alle adressen op uit de database
        System.out.println("[Test] OVChipkaartDAO.findAll() geeft de volgende ovchipkaarten:");
        List<OVChipkaart> ovChipkaartList = ovcdao.findAll();
        for (OVChipkaart o : ovChipkaartList) {
            System.out.println(o);
        }
        System.out.println();

        // Vind een lijst OVChipkaarten op basis van een reiziger
        System.out.println("[Test] OVChipkaartDAO.findByReiziger() vindt de volgende kaarten:");
        List<OVChipkaart> ovChipkaartListReiziger = ovcdao.findByReiziger(rdao.findById(3));
        for (OVChipkaart o : ovChipkaartListReiziger) {
            System.out.println(o);
        }
        System.out.println();

        // Update een kaart uit de database
        String dateUpdate = "2023-05-30";
        OVChipkaart updateOVChipkaart = new OVChipkaart(12345, Date.valueOf(dateUpdate), 1, 50.00, rdao.findById(2));
        System.out.print("[Test] Eerst was de data van de kaart: \n" + ovcdao.findById(12345));
        updateOVChipkaart.addProduct(pdao.findById(6));
        ovcdao.update(updateOVChipkaart);
        System.out.println("\nEn nu is de data van de kaart geupdate naar: \n" + ovcdao.findById(12345));
        System.out.println();

        // Verwijder een kaart uit de database
        System.out.print("[Test] Eerst " + ovChipkaartList.size() + " kaarten, na OVChipkaartDAO.delete() ");
        ovcdao.delete(ovcdao.findById(12345));
        ovChipkaartList = ovcdao.findAll();
        System.out.println(ovChipkaartList.size() + " kaarten\n");
    }

    private static void testProduct(ProductDAO pdao, OVChipkaartDAO ovcdao, ReizigerDAO rdao) {
        System.out.println("\n---------- Test ProductDAO -------------");

        // Vind een lijst Producten op basis van een OVChipkaart
        System.out.println("[Test] ProductDAO.findByOVChipkaart() geeft de volgende producten:");
        List<OVChipkaart> ovChipkaartList = ovcdao.findByReiziger(rdao.findById(2));
        for (OVChipkaart o : ovChipkaartList) {
            List<Product> productList = pdao.findByOVChipkaart(o);
            for (Product p : productList) {
                System.out.println(p);
            }
        }
        System.out.println();

        // Haal alle producten op uit de database
        System.out.println("[Test] ProductDAO.findAll() vindt de volgende producten:");
        List<Product> productList = pdao.findAll();
        for (Product p : productList) {
            System.out.println(p);
        }
        System.out.println();

        // Maak een nieuw product aan en persisteer deze in de database
        Product newProduct = new Product(7, "Dagkaart 1e klas", "Een hele dag onbeperkt reizen met de trein.", 75.00);
        System.out.print("[Test] Eerst " + productList.size() + " producten, na ProductDAO.save() ");
        newProduct.addChipkaart(ovcdao.findById(57401));
        pdao.save(newProduct);
        productList = pdao.findAll();
        System.out.println(productList.size() + " producten\n");

        // Update een product uit de database
        String beschrijvingUpdate = "Geen heerlijke reis met de trein.";
        Product updateProduct = new Product(7, "Dagkaart 1e klas", beschrijvingUpdate, 75.00);
        updateProduct.addChipkaart(ovcdao.findById(57401));
        System.out.print("[Test] Eerst was de data van het product: \n" + pdao.findById(7));
        pdao.update(updateProduct);
        System.out.println("\nEn nu is de data van het product geupdate naar: \n" + pdao.findById(7));
        System.out.println();

        // Delete een product uit de database
        System.out.print("[Test] Eerst " + productList.size() + " producten, na ProductDAO.delete() ");
        pdao.delete(pdao.findById(7));
        productList = pdao.findAll();
        System.out.println(productList.size() + " producten\n");
    }

    public static void main(String[] args) {
        try {
            Session session = getSession();
            ReizigerDAOHibernate rdao = new ReizigerDAOHibernate(session);
            AdresDAOHibernate adao = new AdresDAOHibernate(session, rdao);
            OVChipkaartDAOHibernate ovcdao = new OVChipkaartDAOHibernate(session, rdao);
            ProductDAOHibernate pdao = new ProductDAOHibernate(session, ovcdao);
            testReizigerDAO(rdao);
            testAdresDAO(adao, rdao);
            testOVChipkaartDAO(ovcdao, rdao, pdao);
            testProduct(pdao, ovcdao, rdao);
            closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
