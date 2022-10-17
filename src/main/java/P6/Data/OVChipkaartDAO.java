package P6.Data;

import P5.domain.OVChipkaart;
import P5.domain.Product;
import P5.domain.Reiziger;

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
