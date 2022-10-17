package P6.Data;

import P6.Domain.Adres;
import P6.Domain.Reiziger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdresDAOPsql implements AdresDAO {
    Connection conn;
    ReizigerDAOPsql reizigerDAO;

    public AdresDAOPsql(Connection conn, ReizigerDAOPsql reizigerDAO) {
        this.conn = conn;
        this.reizigerDAO = reizigerDAO;
    }

    public boolean adresStatement(Adres adres, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, adres.getPostcode());
        preparedStatement.setString(2, adres.getHuisnummer());
        preparedStatement.setString(3, adres.getStraat());
        preparedStatement.setString(4, adres.getWoonplaats());
        preparedStatement.setInt(5, adres.getReiziger().getId());
        preparedStatement.setInt(6, adres.getId());
        preparedStatement.executeUpdate();
        preparedStatement.close();
        return true;
    }

    @Override
    public boolean save(Adres adres) {
        try {
            PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO adres(postcode, huisnummer, straat, woonplaats, reiziger_id, adres_id) VALUES(?, ?, ?, ?, ?, ?)");
            return adresStatement(adres, preparedStatement);
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            // Dit is zwaar onnodig :)
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Adres adres) {
        try {
            PreparedStatement preparedStatement = conn.prepareStatement("UPDATE adres SET postcode = ?, huisnummer = ?, straat = ?, woonplaats = ?, reiziger_id = ? WHERE adres_id = ?");
            return adresStatement(adres, preparedStatement);
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            // Dit is zwaar onnodig :)
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Adres adres) {
        try {
            PreparedStatement preparedStatement = conn.prepareStatement("DELETE FROM adres WHERE adres_id = ?");
            preparedStatement.setInt(1, adres.getId());
            preparedStatement.executeUpdate();
            preparedStatement.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            // Dit is zwaar onnodig :)
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Adres findByReiziger(Reiziger reiziger) {
        try {
            PreparedStatement preparedStatement = conn.prepareStatement("SELECT * from adres WHERE reiziger_id = ?");
            preparedStatement.setInt(1, reiziger.getId());
            ResultSet rs = preparedStatement.executeQuery();
            Adres adres = null;
            while (rs.next()) {
                adres = new Adres(
                        rs.getInt("adres_id"),
                        rs.getString("postcode"),
                        rs.getString("huisnummer"),
                        rs.getString("straat"),
                        rs.getString("woonplaats"),
                        reiziger);
            }
            preparedStatement.close();
            rs.close();
            return adres;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            // Dit is zwaar onnodig :)
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Adres> findAll() {
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * from adres");
            List<Adres> adresList = new ArrayList<>();
            while (rs.next()) {
                Adres adres = new Adres(
                        rs.getInt("adres_id"),
                        rs.getString("postcode"),
                        rs.getString("huisnummer"),
                        rs.getString("straat"),
                        rs.getString("woonplaats"),
                        reizigerDAO.findById(rs.getInt("reiziger_id")));
                adresList.add(adres);
            }
            statement.close();
            rs.close();
            return adresList;
        } catch (SQLException se) {
            se.printStackTrace();
            return null;
        } catch (Exception e) {
            // Dit is zwaar onnodig :)
            e.printStackTrace();
            return null;
        }
    }
}
