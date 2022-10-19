package P7.Data;

import P7.Domain.OVChipkaart;
import P7.Domain.Product;
import P7.Domain.Reiziger;

import java.util.List;

public interface OVChipkaartDAO {

    boolean save(OVChipkaart ovChipkaart);
    boolean update(OVChipkaart ovChipkaart);
    boolean delete(OVChipkaart ovChipkaart);
    List<OVChipkaart> findByReiziger(Reiziger reiziger);
    List<OVChipkaart> findByProduct(Product product);
    List<OVChipkaart> findAll();
    OVChipkaart findById(int id);

}
