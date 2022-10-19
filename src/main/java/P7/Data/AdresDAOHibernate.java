package P7.Data;

import P7.Domain.Adres;
import P7.Domain.Reiziger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.sql.SQLException;
import java.util.List;

public class AdresDAOHibernate implements AdresDAO {
    private Session session;
    private ReizigerDAOHibernate reizigerDAO;

    public AdresDAOHibernate(Session session, ReizigerDAOHibernate reizigerDAOPsql) throws SQLException {
        this.session = session;
        this.reizigerDAO = reizigerDAOPsql;
    }

    @Override
    public boolean save(Adres adres) {
        Transaction transaction = session.beginTransaction();
        try {
            session.save(adres);
            transaction.commit();
            return true;
        } catch (Exception e) {
            transaction.rollback();
            return false;
        }
    }

    @Override
    public boolean update(Adres adres) {
        Transaction transaction = session.beginTransaction();
        try {
            session.update(adres);
            transaction.commit();
            return true;
        } catch (Exception e) {
            transaction.rollback();
            return false;
        }
    }

    @Override
    public boolean delete(Adres adres) {
        Transaction transaction = session.beginTransaction();
        try {
            session.delete(adres);
            transaction.commit();
            return true;
        } catch (Exception e) {
            transaction.rollback();
            return false;
        }
    }

    @Override
    public Adres findByReiziger(Reiziger reiziger) {
        try {
            return session.createQuery("from Adres where reiziger_id = :id", Adres.class)
                    .setParameter("id", reiziger.getId())
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Adres> findAll() {
        Transaction transaction = session.beginTransaction();
        try {
            List<Adres> adressen = session.createQuery("from Adres", Adres.class).list();
            transaction.commit();
            return adressen;
        } catch (Exception e) {
            transaction.rollback();
            return null;
        }
    }
}
