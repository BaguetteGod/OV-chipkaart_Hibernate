package P5.data;

import P5.domain.OVChipkaart;
import P5.domain.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOPsql implements ProductDAO {
    private Connection conn;
    private OVChipkaartDAOPsql ovcdao;

    public ProductDAOPsql(Connection conn, OVChipkaartDAOPsql ovcdao) {
        this.conn = conn;
        this.ovcdao = ovcdao;
    }

    private boolean productStatement(Product product, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, product.getNaam());
        preparedStatement.setString(2, product.getBeschrijving());
        preparedStatement.setDouble(3, product.getPrijs());
        preparedStatement.setInt(4, product.getId());
        preparedStatement.executeUpdate();
        preparedStatement.close();
        return true;
    }

    @Override
    public boolean save(Product product) {
        try {
            PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO product(naam, beschrijving, prijs, product_nummer) VALUES(?, ?, ?, ?)");
            productStatement(product, preparedStatement);
            List<Integer> ovChipkaartList = product.getOvChipkaartList();
            for (Integer o : ovChipkaartList) {
                PreparedStatement saveStatement = conn.prepareStatement("INSERT INTO ov_chipkaart_product(kaart_nummer, product_nummer) VALUES(?, ?)");
                saveStatement.setInt(1, o);
                saveStatement.setInt(2, product.getId());
                saveStatement.executeUpdate();
                saveStatement.close();
            }
            return true;
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
    public boolean update(Product product) {
        try {
            PreparedStatement preparedStatement = conn.prepareStatement("UPDATE product SET naam = ?, beschrijving = ?, prijs = ? WHERE product_nummer = ?");
            productStatement(product, preparedStatement);
            PreparedStatement deleteStatement = conn.prepareStatement("DELETE FROM ov_chipkaart_product WHERE product_nummer = ?");
            deleteStatement.setInt(1, product.getId());
            deleteStatement.executeUpdate();
            deleteStatement.close();
            List<Integer> ovChipkaartList = product.getOvChipkaartList();
            for (Integer o : ovChipkaartList) {
                PreparedStatement saveStatement = conn.prepareStatement("INSERT INTO ov_chipkaart_product(kaart_nummer, product_nummer) VALUES(?, ?)");
                saveStatement.setInt(1, o);
                saveStatement.setInt(2, product.getId());
                saveStatement.executeUpdate();
                saveStatement.close();
            }
            return true;
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
    public boolean delete(Product product) {
        try {
            PreparedStatement preparedStatement = conn.prepareStatement("DELETE FROM product WHERE product_nummer = ?");
            preparedStatement.setInt(1, product.getId());

            PreparedStatement deleteStatement = conn.prepareStatement("DELETE FROM ov_chipkaart_product WHERE product_nummer = ?");
            deleteStatement.setInt(1, product.getId());
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
    public List<Product> findAll() {
        try {
            PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM product");
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Product> productList = new ArrayList<>();
            while (resultSet.next()) {
                Product product = new Product(
                        resultSet.getInt("product_nummer"),
                        resultSet.getString("naam"),
                        resultSet.getString("beschrijving"),
                        resultSet.getDouble("prijs"));
                PreparedStatement ovChipkaartStatement = conn.prepareStatement("SELECT kaart_nummer FROM ov_chipkaart_product WHERE product_nummer = ?");
                ovChipkaartStatement.setInt(1, product.getId());
                ResultSet ovChipkaartResultSet = ovChipkaartStatement.executeQuery();
                while (ovChipkaartResultSet.next()) {
                    product.addOvChipkaart(ovChipkaartResultSet.getInt("kaart_nummer"));
                }
                productList.add(product);
            }

            return productList;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Product> findByOVChipkaart(OVChipkaart ovChipkaart) {
        try {
            PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM product WHERE product_nummer IN (SELECT product_nummer FROM ov_chipkaart_product WHERE kaart_nummer = ?)");
            preparedStatement.setInt(1, ovChipkaart.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Product> productList = new ArrayList<>();
            while (resultSet.next()) {
                Product product = new Product(
                        resultSet.getInt("product_nummer"),
                        resultSet.getString("naam"),
                        resultSet.getString("beschrijving"),
                        resultSet.getDouble("prijs"));
                PreparedStatement ovChipkaartStatement = conn.prepareStatement("SELECT kaart_nummer FROM ov_chipkaart_product WHERE product_nummer = ?");
                ovChipkaartStatement.setInt(1, product.getId());
                ResultSet ovChipkaartResultSet = ovChipkaartStatement.executeQuery();
                while (ovChipkaartResultSet.next()) {
                    product.addOvChipkaart(ovChipkaartResultSet.getInt("kaart_nummer"));
                }
                productList.add(product);
                ovChipkaartResultSet.close();
                ovChipkaartStatement.close();
            }
            return productList;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Product findById(int id) {
        try {
            PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM product WHERE product_nummer = ?");
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            Product product = null;
            while (resultSet.next()) {
                product = new Product(
                        resultSet.getInt("product_nummer"),
                        resultSet.getString("naam"),
                        resultSet.getString("beschrijving"),
                        resultSet.getDouble("prijs"));
                PreparedStatement ovChipkaartStatement = conn.prepareStatement("SELECT kaart_nummer FROM ov_chipkaart_product WHERE product_nummer = ?");
                ovChipkaartStatement.setInt(1, product.getId());
                ResultSet ovChipkaartResultSet = ovChipkaartStatement.executeQuery();
                while (ovChipkaartResultSet.next()) {
                    product.addOvChipkaart(ovChipkaartResultSet.getInt("kaart_nummer"));
                }
                ovChipkaartResultSet.close();
                ovChipkaartStatement.close();
            }
            return product;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
