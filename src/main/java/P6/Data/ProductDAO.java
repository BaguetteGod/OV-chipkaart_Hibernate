package P6.Data;

import P6.Domain.OVChipkaart;
import P6.Domain.Product;

import java.util.List;

public interface ProductDAO {
    boolean save(Product product);
    boolean update(Product product);
    boolean delete(Product product);
    List<Product> findAll();
    List<Product> findByOVChipkaart(OVChipkaart ovChipkaart);
    Product findById(int id);
}
