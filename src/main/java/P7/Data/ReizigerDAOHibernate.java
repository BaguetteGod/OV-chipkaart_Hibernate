package P7.Data;

import P7.Domain.Reiziger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class ReizigerDAOHibernate implements ReizigerDAO {
    private Session session;
    private AdresDAOHibernate adao;
    private OVChipkaartDAOHibernate ovcdao;

    public ReizigerDAOHibernate(Session session) throws SQLException {
        this.session = session;
        this.adao = new AdresDAOHibernate(this.session, this);
        this.ovcdao = new OVChipkaartDAOHibernate(this.session, this);
    }

    @Override
    public boolean save(Reiziger reiziger) {
        Transaction transaction = session.beginTransaction();
        try {
            session.save(reiziger);
            transaction.commit();
            return true;
        } catch (Exception e) {
            transaction.rollback();
            return false;
        }
    }

    @Override
    public boolean update(Reiziger reiziger) {
        Transaction transaction = session.beginTransaction();
        try {
            session.update(reiziger);
            transaction.commit();
            return true;
        } catch (Exception e) {
            transaction.rollback();
            return false;
        }
    }

    @Override
    public boolean delete(Reiziger reiziger) {
        Transaction transaction = session.beginTransaction();
        try {
            session.delete(reiziger);
            transaction.commit();
            return true;
        } catch (Exception e) {
            transaction.rollback();
            return false;
        }
    }

    @Override
    public Reiziger findById(int id) {
        Transaction transaction = session.beginTransaction();
        try {
            Reiziger reiziger = session.get(Reiziger.class, id);
            transaction.commit();
            return reiziger;
        } catch (Exception e) {
            transaction.rollback();
            return null;
        }
    }

    @Override
    public List<Reiziger> findByGbdatum(String date) {
        try {
            Date sqlDate = Date.valueOf(date);
            return session.createQuery("from Reiziger where geboortedatum = :date", Reiziger.class)
                    .setParameter("date", sqlDate)
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Reiziger> findAll() {
        Transaction transaction = session.beginTransaction();
        try {
            List<Reiziger> reizigers = session.createQuery("from Reiziger").list();
            transaction.commit();
            return reizigers;
        } catch (Exception e) {
            transaction.rollback();
            return null;
        }
    }
}
