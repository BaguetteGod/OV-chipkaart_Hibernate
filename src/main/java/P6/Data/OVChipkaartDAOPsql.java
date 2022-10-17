package P5.data;

import P5.domain.OVChipkaart;
import P5.domain.Product;
import P5.domain.Reiziger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OVChipkaartDAOPsql implements OVChipkaartDAO {
    private Connection conn;
    private ReizigerDAOPsql rdao;
    private ProductDAOPsql pdao;


    public OVChipkaartDAOPsql(Connection conn, ReizigerDAOPsql rdao) {
        this.conn = conn;
        this.rdao = rdao;
        this.pdao = new ProductDAOPsql(conn, this);
    }

    private boolean ovChipkaartStatement(OVChipkaart ovChipkaart, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setDate(1, ovChipkaart.getGeldigTot());
        preparedStatement.setInt(2, ovChipkaart.getKlasse());
        preparedStatement.setDouble(3, ovChipkaart.getSaldo());
        preparedStatement.setInt(4, ovChipkaart.getReiziger().getId());
        preparedStatement.setInt(5, ovChipkaart.getId());
        preparedStatement.executeUpdate();
        preparedStatement.close();
        return true;
    }

    @Override
    public boolean save(OVChipkaart ovChipkaart) {
        try {
            PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO ov_chipkaart(geldig_tot, klasse, saldo, reiziger_id, kaart_nummer) VALUES(?, ?, ?, ?, ?)");
            ovChipkaartStatement(ovChipkaart, preparedStatement);
            return insertOVChipkaart(ovChipkaart);
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(OVChipkaart ovChipkaart) {
        try {
            PreparedStatement preparedStatement = conn.prepareStatement("UPDATE ov_chipkaart SET geldig_tot = ?, klasse = ?, saldo = ?, reiziger_id = ? WHERE kaart_nummer = ?");
            ovChipkaartStatement(ovChipkaart, preparedStatement);
            PreparedStatement deleteStatement = conn.prepareStatement("DELETE FROM ov_chipkaart_product WHERE kaart_nummer = ?");
            deleteStatement.setInt(1, ovChipkaart.getId());
            deleteStatement.executeUpdate();
            deleteStatement.close();
            return insertOVChipkaart(ovChipkaart);
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean insertOVChipkaart(OVChipkaart ovChipkaart) throws SQLException {
        List<Product> productList = ovChipkaart.getProductList();
        if (productList != null) {
            for (Product p : productList) {
                PreparedStatement insertStatement = conn.prepareStatement("INSERT INTO ov_chipkaart_product(kaart_nummer, product_nummer) VALUES(?, ?)");
                insertStatement.setInt(1, ovChipkaart.getId());
                insertStatement.setInt(2, p.getId());
                insertStatement.executeUpdate();
                insertStatement.close();
            }
        }
        return true;
    }

    @Override
    public boolean delete(OVChipkaart ovChipkaart) {
        try {
            PreparedStatement preparedStatement = conn.prepareStatement("DELETE FROM ov_chipkaart WHERE kaart_nummer = ?");
            preparedStatement.setInt(1, ovChipkaart.getId());

            PreparedStatement deleteStatement = conn.prepareStatement("DELETE FROM ov_chipkaart_product WHERE kaart_nummer = ?");
            deleteStatement.setInt(1, ovChipkaart.getId());
            deleteStatement.executeUpdate();
            deleteStatement.close();

            preparedStatement.executeUpdate();
            preparedStatement.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<OVChipkaart> findByReiziger(Reiziger reiziger) {
        try {
            PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM ov_chipkaart WHERE reiziger_id = ?");
            preparedStatement.setInt(1, reiziger.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            List<OVChipkaart> ovChipkaartList = new ArrayList<>();
            while (resultSet.next()) {
                OVChipkaart ovChipkaart = new OVChipkaart(
                        resultSet.getInt("kaart_nummer"),
                        resultSet.getDate("geldig_tot"),
                        resultSet.getInt("klasse"),
                        resultSet.getDouble("saldo"), reiziger);
                ovChipkaart.setProductList(pdao.findByOVChipkaart(ovChipkaart));
                ovChipkaartList.add(ovChipkaart);
            }
            resultSet.close();
            preparedStatement.close();
            return ovChipkaartList;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<OVChipkaart> findByProduct(Product product) {
        try {
            PreparedStatement preparedStatement = conn.prepareStatement("SELECT o.kaart_nummer, o.geldig_tot, o.klasse, o.saldo, o.reiziger_id FROM ov_chipkaart_product as ovc JOIN ov_chipkaart as o ON ovc.kaart_nummer = o.kaart_nummer WHERE product_nummer = ?");
            preparedStatement.setInt(1, product.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            List<OVChipkaart> ovChipkaartList = new ArrayList<>();
            while (resultSet.next()) {
                OVChipkaart ovChipkaart = new OVChipkaart(
                        resultSet.getInt("kaart_nummer"),
                        resultSet.getDate("geldig_tot"),
                        resultSet.getInt("klasse"),
                        resultSet.getDouble("saldo"),
                        rdao.findById(resultSet.getInt("reiziger_id")));
                ovChipkaartList.add(ovChipkaart);
            }
            preparedStatement.close();
            resultSet.close();
            return ovChipkaartList;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<OVChipkaart> findAll() {
        try {
            PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM ov_chipkaart");
            ResultSet resultSet = preparedStatement.executeQuery();
            List<OVChipkaart> ovChipkaartList = new ArrayList<>();
            while (resultSet.next()) {
                OVChipkaart ovChipkaart = new OVChipkaart(
                        resultSet.getInt("kaart_nummer"),
                        resultSet.getDate("geldig_tot"),
                        resultSet.getInt("klasse"),
                        resultSet.getDouble("saldo"),
                        rdao.findById(resultSet.getInt("reiziger_id")));
                ovChipkaart.setProductList(pdao.findByOVChipkaart(ovChipkaart));
                ovChipkaartList.add(ovChipkaart);
            }
            resultSet.close();
            preparedStatement.close();
            return ovChipkaartList;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public OVChipkaart findById(int id) {
        try {
            // find ovchipkaart by id and add product to it
            PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM ov_chipkaart WHERE kaart_nummer = ?");
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            OVChipkaart ovChipkaart = null;
            while (resultSet.next()) {
                ovChipkaart = new OVChipkaart(
                        resultSet.getInt("kaart_nummer"),
                        resultSet.getDate("geldig_tot"),
                        resultSet.getInt("klasse"),
                        resultSet.getDouble("saldo"),
                        rdao.findById(resultSet.getInt("reiziger_id")));
                ovChipkaart.setProductList(pdao.findByOVChipkaart(ovChipkaart));
            }
            resultSet.close();
            preparedStatement.close();
            return ovChipkaart;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
