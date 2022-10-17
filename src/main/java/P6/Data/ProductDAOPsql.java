package P6.Data;

import P6.Domain.OVChipkaart;
import P6.Domain.Product;

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

    private void productStatement(Product product, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, product.getNaam());
        preparedStatement.setString(2, product.getBeschrijving());
        preparedStatement.setDouble(3, product.getPrijs());
        preparedStatement.setInt(4, product.getId());
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    @Override
    public boolean save(Product product) {
        try {
            PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO product(naam, beschrijving, prijs, product_nummer) VALUES(?, ?, ?, ?)");
            List<OVChipkaart> ovChipkaartList = product.getOvchipkaartList();
            productStatement(product, preparedStatement);
            if (ovChipkaartList != null) {
                for (OVChipkaart o : ovChipkaartList) {
                    PreparedStatement productStatement = conn.prepareStatement("INSERT INTO ov_chipkaart_product(kaart_nummer, product_nummer) VALUES(?, ?)");
                    productStatement.setInt(1, o.getId());
                    productStatement.setInt(2, product.getId());
                    productStatement.executeUpdate();
                    productStatement.close();
                    ovcdao.update(o);
                }
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
            List<OVChipkaart> ovChipkaartList = product.getOvchipkaartList();
            if (ovChipkaartList != null) {
                for (OVChipkaart o : ovChipkaartList) {
                    ovcdao.update(o);
                }
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
            PreparedStatement deleteStatement = conn.prepareStatement("DELETE FROM ov_chipkaart_product WHERE product_nummer = ?");
            preparedStatement.setInt(1, product.getId());
            deleteStatement.setInt(1, product.getId());
            deleteStatement.executeUpdate();
            deleteStatement.close();
            List<OVChipkaart> ovChipkaartList = ovcdao.findByProduct(product);
            if (ovChipkaartList != null) {
                for (OVChipkaart o : ovChipkaartList) {
                    o.removeProduct(product);
                    ovcdao.update(o);
                }
            }
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
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * from product");
            List<Product> productList = new ArrayList<>();
            while (rs.next()) {
                Product product = new Product(
                        rs.getInt("product_nummer"),
                        rs.getString("naam"),
                        rs.getString("beschrijving"),
                        rs.getDouble("prijs"));
                productList.add(product);
            }

            for (Product p : productList) {
                List<OVChipkaart> ovChipkaartList = ovcdao.findByProduct(p);
                if (ovChipkaartList != null) {
                    p.setOvChipkaartList(ovChipkaartList);
                }
            }

            statement.close();
            rs.close();
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
            PreparedStatement preparedStatement = conn.prepareStatement("SELECT p.product_nummer, p.naam, p.beschrijving, p.prijs FROM ov_chipkaart_product as ovc JOIN product as p ON ovc.product_nummer = p.product_nummer WHERE kaart_nummer = ?");
            preparedStatement.setInt(1, ovChipkaart.getId());
            ResultSet rs = preparedStatement.executeQuery();
            List<Product> productList = new ArrayList<>();
            while (rs.next()) {
                Product product = new Product(
                        rs.getInt("product_nummer"),
                        rs.getString("naam"),
                        rs.getString("beschrijving"),
                        rs.getDouble("prijs"));
                productList.add(product);
            }

            for (Product p : productList) {
                List<OVChipkaart> ovChipkaartList = ovcdao.findByProduct(p);
                if (ovChipkaartList != null) {
                    p.setOvChipkaartList(ovChipkaartList);
                }
            }

            preparedStatement.close();
            rs.close();
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
            PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM product WHERE product_nummer  = ?");
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            Product product = null;
            while (rs.next()) {
                product = new Product(
                        rs.getInt("product_nummer"),
                        rs.getString("naam"),
                        rs.getString("beschrijving"),
                        rs.getDouble("prijs"));
            }

            List<OVChipkaart> ovChipkaartList = ovcdao.findByProduct(product);
            if (ovChipkaartList != null) {
                product.setOvChipkaartList(ovChipkaartList);
            }

            preparedStatement.close();
            rs.close();
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
