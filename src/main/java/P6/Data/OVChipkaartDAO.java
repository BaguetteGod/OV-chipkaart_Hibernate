package P6.Data;

import P6.Domain.OVChipkaart;
import P6.Domain.Product;
import P6.Domain.Reiziger;

import java.util.List;

public interface OVChipkaartDAO {
    boolean save(OVChipkaart ovChipkaart);
    boolean update(OVChipkaart ovChipkaart);
    boolean delete(OVChipkaart ovChipkaart);
    List<OVChipkaart> findByReiziger(Reiziger reiziger);
    List<OVChipkaart> findAll();

    List<OVChipkaart> findByProduct(Product p);

    OVChipkaart findById(int i);
}
