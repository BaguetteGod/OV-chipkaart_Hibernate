package P6.Data;

import P5.domain.Adres;
import P5.domain.OVChipkaart;
import P5.domain.Reiziger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReizigerDAOPsql implements ReizigerDAO {
    private Connection conn;
    private AdresDAOPsql adao;
    private OVChipkaartDAOPsql ovcdao;

    public ReizigerDAOPsql(Connection conn) throws SQLException {
        this.conn = conn;
        this.adao = new AdresDAOPsql(this.conn, this);
        this.ovcdao = new OVChipkaartDAOPsql(this.conn, this);
    }

    private boolean reizigerStatement(Reiziger reiziger, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, reiziger.getVoorletters());
        preparedStatement.setString(2, reiziger.getTussenvoegsel());
        preparedStatement.setString(3, reiziger.getAchternaam());
        preparedStatement.setDate(4, reiziger.getGeboortedatum());
        preparedStatement.setInt(5, reiziger.getId());
        preparedStatement.executeUpdate();
        preparedStatement.close();
        return true;
    }

    @Override
    public boolean save(Reiziger reiziger) {
        try {
            PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO reiziger(voorletters, tussenvoegsel, achternaam, geboortedatum, reiziger_id) VALUES(?, ?, ?, ?, ?)");
            List<OVChipkaart> ovChipkaartList = reiziger.getOvChipkaartList();
            if (ovChipkaartList != null) {
                for (OVChipkaart o : ovChipkaartList) {
                    ovcdao.save(o);
                }
            }
            Adres adres = reiziger.getAdres();
            if (adres != null) {
                adao.save(adres);
            }
            System.out.println("save");
            return reizigerStatement(reiziger, preparedStatement);
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
    public boolean update(Reiziger reiziger) {
        try {
            PreparedStatement preparedStatement = conn.prepareStatement("UPDATE reiziger SET voorletters = ?, tussenvoegsel = ?, achternaam = ?, geboortedatum = ? WHERE reiziger_id = ?");
            List<OVChipkaart> ovChipkaartList = reiziger.getOvChipkaartList();
            List<OVChipkaart> oldOVChipkaartList = ovcdao.findByReiziger(reiziger);
            if (ovChipkaartList != null) {
                for (OVChipkaart o : ovChipkaartList) {
                    if (ovChipkaartList.contains(o) && !oldOVChipkaartList.contains(o)) {
                        ovcdao.save(o);
                    } else if (!ovChipkaartList.contains(o) && oldOVChipkaartList.contains(o)) {
                        ovcdao.delete(o);
                    } else if (!ovChipkaartList.contains(o) && !oldOVChipkaartList.contains(o)) {
                        ovcdao.update(o);
                    }
                }
            }
            Adres adres = reiziger.getAdres();
            Adres oldAdres = adao.findByReiziger(reiziger);
            if (adres != null && oldAdres != null) {
                adao.update(adres);
                System.out.println("update");
            } else if (adres != null) {
                adao.save(adres);
                System.out.println("save");
            } else if (oldAdres != null) {
                adao.delete(adres);
                System.out.println("delete");
            }
            return reizigerStatement(reiziger, preparedStatement);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Reiziger reiziger) {
        try {
            PreparedStatement preparedStatement = conn.prepareStatement("DELETE FROM reiziger WHERE reiziger_id = ?");
            preparedStatement.setInt(1, reiziger.getId());
            List<OVChipkaart> ovChipkaartList = reiziger.getOvChipkaartList();
            if (ovChipkaartList != null) {
                for (OVChipkaart o : ovChipkaartList) {
                    ovcdao.delete(o);
                }
            }
            Adres adres = reiziger.getAdres();
            if (adres != null) {
                adao.delete(adres);
            }
            System.out.println("delete");
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
    public Reiziger findById(int id) {
        try {
            PreparedStatement preparedStatement = conn.prepareStatement("SELECT * from reiziger WHERE reiziger_id = ?");
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            Reiziger reiziger = null;
            while (rs.next()) {
                reiziger = new Reiziger(
                        rs.getInt("reiziger_id"),
                        rs.getString("voorletters"),
                        rs.getString("tussenvoegsel"),
                        rs.getString("achternaam"),
                        rs.getDate("geboortedatum"));

                List<OVChipkaart> ovChipkaartList = ovcdao.findByReiziger(reiziger);
                if (ovChipkaartList != null) {
                    reiziger.setOvChipkaartList(ovChipkaartList);
                }

                Adres adres = adao.findByReiziger(reiziger);
                if (adres != null) {
                    reiziger.setAdres(adres);
                }
            }
            preparedStatement.close();
            rs.close();
            return reiziger;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Reiziger> findByGbdatum(String date) {
        try {
            PreparedStatement preparedStatement = conn.prepareStatement("SELECT * from reiziger WHERE geboortedatum = ?");
            Date sqlDate = Date.valueOf(date);
            preparedStatement.setDate(1, sqlDate);
            ResultSet rs = preparedStatement.executeQuery();
            List<Reiziger> reizigerList = new ArrayList<>();
            while (rs.next()) {
                Reiziger reiziger = new Reiziger(
                        rs.getInt("reiziger_id"),
                        rs.getString("voorletters"),
                        rs.getString("tussenvoegsel"),
                        rs.getString("achternaam"),
                        rs.getDate("geboortedatum"));
                reizigerList.add(reiziger);

                List<OVChipkaart> ovChipkaartList = ovcdao.findByReiziger(reiziger);
                reiziger.setOvChipkaartList(ovChipkaartList);

                Adres adres = adao.findByReiziger(reiziger);
                if (adres != null) {
                    reiziger.setAdres(adres);
                }
            }
            preparedStatement.close();
            rs.close();
            return reizigerList;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Reiziger> findAll() {
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * from reiziger");
            List<Reiziger> reizigerList = new ArrayList<>();
            while (rs.next()) {
                Reiziger reiziger = new Reiziger(
                        rs.getInt("reiziger_id"),
                        rs.getString("voorletters"),
                        rs.getString("tussenvoegsel"),
                        rs.getString("achternaam"),
                        rs.getDate("geboortedatum"));
                reizigerList.add(reiziger);

                List<OVChipkaart> ovChipkaartList = ovcdao.findByReiziger(reiziger);
                reiziger.setOvChipkaartList(ovChipkaartList);

                Adres adres = adao.findByReiziger(reiziger);
                if (adres != null) {
                    reiziger.setAdres(adres);
                }
            }
            statement.close();
            rs.close();
            return reizigerList;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
