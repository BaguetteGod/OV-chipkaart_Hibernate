package P7.Data;

import P7.Domain.OVChipkaart;
import P7.Domain.Product;
import P7.Domain.Reiziger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class OVChipkaartDAOHibernate implements OVChipkaartDAO {

    private Session session;
    private ReizigerDAOHibernate rdao;
    private ProductDAOHibernate pdao;


    public OVChipkaartDAOHibernate(Session session, ReizigerDAOHibernate rdao) {
        this.session = session;
        this.rdao = rdao;
        this.pdao = new ProductDAOHibernate(session, this);
    }

    @Override
    public boolean save(OVChipkaart ovChipkaart) {
        Transaction transaction = session.beginTransaction();
        try {
            session.save(ovChipkaart);
            transaction.commit();
            return true;
        } catch (Exception e) {
            transaction.rollback();
            return false;
        }
    }

    @Override
    public boolean update(OVChipkaart ovChipkaart) {
        Transaction transaction = session.beginTransaction();
        try {
            session.update(ovChipkaart);
            transaction.commit();
            return true;
        } catch (Exception e) {
            transaction.rollback();
            return false;
        }
    }

    @Override
    public boolean delete(OVChipkaart ovChipkaart) {
        Transaction transaction = session.beginTransaction();
        try {
            session.delete(ovChipkaart);
            transaction.commit();
            return true;
        } catch (Exception e) {
            transaction.rollback();
            return false;
        }
    }

    @Override
    public List<OVChipkaart> findByReiziger(Reiziger reiziger) {
        // find by reiziger with a transaction because of lazy loading of producten
        Transaction transaction = session.beginTransaction();
        try {
            List<OVChipkaart> ovChipkaarten = session.createQuery("from OVChipkaart where reiziger_id = :id", OVChipkaart.class)
                    .setParameter("id", reiziger.getId())
                    .getResultList();
            transaction.commit();
            return ovChipkaarten;
        } catch (Exception e) {
            transaction.rollback();
            return null;
        }
    }

    @Override
    public List<OVChipkaart> findByProduct(Product product) {
        Transaction transaction = session.beginTransaction();
        try {
            List<OVChipkaart> ovChipkaarten = session.createQuery("from OVChipkaart where product_id = :id", OVChipkaart.class)
                    .setParameter("id", product.getId())
                    .getResultList();
            transaction.commit();
            return ovChipkaarten;
        } catch (Exception e) {
            transaction.rollback();
            return null;
        }
    }

    @Override
    public List<OVChipkaart> findAll() {
        Transaction transaction = session.beginTransaction();
        try {
            List<OVChipkaart> ovChipkaarten = session.createQuery("from OVChipkaart ", OVChipkaart.class).getResultList();
            transaction.commit();
            return ovChipkaarten;
        } catch (Exception e) {
            transaction.rollback();
            return null;
        }
    }

    @Override
    public OVChipkaart findById(int id) {
        Transaction transaction = session.beginTransaction();
        try {
            OVChipkaart ovChipkaart = session.createQuery("from OVChipkaart where id = :id", OVChipkaart.class)
                    .setParameter("id", id)
                    .getSingleResult();
            transaction.commit();
            return ovChipkaart;
        } catch (Exception e) {
            transaction.rollback();
            return null;
        }
    }

}
