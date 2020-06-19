package repository;

import domain.Meci;
import domain.Vanzare;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import repository.utils.JdbcUtils;
import utils.ConsoleColors;
import validator.IValidator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class VanzareJdbcRepository implements CRUDRepository<Integer, Vanzare> {
    private JdbcUtils dbUtils;

    private static final Logger logger = LogManager.getLogger();

    private MeciJdbcRepository meciRepo;

    private IValidator<Vanzare> validator;

    public VanzareJdbcRepository(Properties props, IValidator<Vanzare> val, MeciJdbcRepository meciRepo){
        logger.info(ConsoleColors.YELLOW + "Initializing VanzareJdbcRepository with properties: {} " + ConsoleColors.RESET
                ,props);
        dbUtils=new JdbcUtils(props);
        this.validator = val;
        this.meciRepo = meciRepo;
    }

    public IValidator<Vanzare> getValidator() {
        return validator;
    }

    @Override
    public int size() {
        logger.traceEntry();
        logger.info(ConsoleColors.YELLOW +
                "se calculeaza numarul de vanzari din VanzareJdbcRepository" +
                ConsoleColors.RESET);
        Connection con = dbUtils.getConnection();
        String statementString = "SELECT count(*) AS [size] FROM Vanzari";
        try(PreparedStatement statement = con.prepareStatement(statementString)){
            ResultSet result = statement.executeQuery();
            if(result.next()){
                logger.traceExit(result.getInt("size"));
                return  result.getInt("size");
            }
        } catch (SQLException x){
            logger.error(x);
            x.printStackTrace();
        }
        return 0;
    }

    @Override
    public void save(Vanzare entity) {
        logger.traceEntry( );
        logger.info(ConsoleColors.YELLOW +
                "se salveaza vanzarea {}"
                + ConsoleColors.RESET,entity);
        Connection con = dbUtils.getConnection();
        String statementString = "INSERT INTO Vanzari(id,nume_client,bilete_cumparate,id_meci) VALUES (?,?,?,?)";
        try(PreparedStatement statement = con.prepareStatement(statementString)){
            statement.setInt(1,entity.getId());
            statement.setString(2,entity.getNumeClient());
            statement.setInt(3,entity.getBileteCumparate());
            statement.setInt(4,entity.getMeci().getId());

            statement.executeUpdate();
        }catch (SQLException x){
            logger.error(x);
            x.printStackTrace();
        }
    }

    @Override
    public void delete(Integer integer) {
        logger.traceEntry();
        logger.info(ConsoleColors.YELLOW + "se sterge vanzarea cu id-ul {}" + ConsoleColors.RESET,integer);
        Connection con = dbUtils.getConnection();
        String statementString = "DELETE FROM Vanzari WHERE id=?";
        try(PreparedStatement statement = con.prepareStatement(statementString)){
            statement.setInt(1,integer);

            statement.executeUpdate();

        } catch (SQLException e){
            logger.error(e);
            e.printStackTrace();
        }
    }

    @Override
    public void update(Integer integer, Vanzare entity) {
        logger.traceEntry();
        logger.info(ConsoleColors.YELLOW + "actualizare vanzare cu id {} cu  vanzarea {}" + ConsoleColors.RESET,
                integer, entity);

        Connection con = dbUtils.getConnection();
        String statementString = "UPDATE Vanzari SET nume_client=?, bilete_cumparate=?, id_meci=? WHERE id=?";
        try(PreparedStatement statement = con.prepareStatement(statementString)){
            statement.setString(1,entity.getNumeClient());
            statement.setInt(2,entity.getBileteCumparate());
            statement.setInt(3,entity.getMeci().getId());
            statement.setInt(4,integer);

            statement.executeUpdate();

        } catch (SQLException c){
            logger.error(c);
            c.printStackTrace();
        }
    }

    @Override
    public Vanzare findOne(Integer integer) {
        logger.traceEntry();
        logger.info(ConsoleColors.YELLOW + "se cauta vanzarea cu id-ul {}" + ConsoleColors.RESET,integer);
        Connection con = dbUtils.getConnection();
        String statementString = "SELECT * FROM Vanzari WHERE id = ?";
        try(PreparedStatement statement = con.prepareStatement(statementString)){
            statement.setInt(1,integer);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()){
                int id = resultSet.getInt("id");
                String client = resultSet.getString("nume_client");
                int nr = resultSet.getInt("bilete_cumparate");

                Meci m = meciRepo.findOne(resultSet.getInt("id_meci"));

                Vanzare v = new Vanzare(id,client,m,nr);
                logger.traceExit(v);
                return v;
            }

        } catch (SQLException x){
            logger.error(x);
            x.printStackTrace();
        }

        logger.info(ConsoleColors.YELLOW + "nu s-a gasit vanzarea cu id-ul {}" + ConsoleColors.RESET, integer);
        logger.traceExit();
        return null;
    }

    @Override
    public Iterable<Vanzare> findAll() {
        logger.traceEntry();

        List<Vanzare> resultList = new ArrayList<>();

        Connection con = dbUtils.getConnection();
        String statementString = "SELECT * FROM Vanzari";
        try(PreparedStatement statement = con.prepareStatement(statementString)){
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                int id = resultSet.getInt("id");
                String client = resultSet.getString("nume_client");
                int nr = resultSet.getInt("bilete_cumparate");

                Meci m = meciRepo.findOne(resultSet.getInt("id_meci"));

                Vanzare utilizator = new Vanzare(id,client,m,nr);
                resultList.add(utilizator);
            }
        } catch (SQLException x){
            logger.error(x);
            x.printStackTrace();
        }
        logger.traceExit(resultList);
        return resultList;
    }

    public int sizeForMeci(Meci meci){
        logger.traceEntry();
        meciRepo.getValidator().validate(meci);

        Connection con = dbUtils.getConnection();
        String statementString = "SELECT count(*) AS [size] FROM Utilizatori WHERE id_meci=?";
        try(PreparedStatement statement = con.prepareStatement(statementString)){
            statement.setInt(1,meci.getId());
            ResultSet result = statement.executeQuery();
            if(result.next()){
                logger.traceExit(result.getInt("size"));
                return  result.getInt("size");
            }
        } catch (SQLException x){
            logger.error(x);
            x.printStackTrace();
        }
        return 0;
    }

    public Iterable<Vanzare> findAllForMeci(Meci meci) {
        logger.traceEntry();
        meciRepo.getValidator().validate(meci);
        List<Vanzare> resultList = new ArrayList<>();

        Connection con = dbUtils.getConnection();
        String statementString = "SELECT * FROM Vanzari WHERE id_meci=?";
        try(PreparedStatement statement = con.prepareStatement(statementString)){
            statement.setInt(1,meci.getId());
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                int id = resultSet.getInt("id");
                String client = resultSet.getString("nume_client");
                int nr = resultSet.getInt("bilete_cumparate");

                Meci m = meciRepo.findOne(resultSet.getInt("id_meci"));

                Vanzare utilizator = new Vanzare(id,client,m,nr);
                resultList.add(utilizator);
            }
        } catch (SQLException x){
            logger.error(x);
            x.printStackTrace();
        }
        logger.traceExit(resultList);
        return resultList;
    }

    public int getTotalBileteCumparateForMeci(Meci meci){
        logger.traceEntry();
        meciRepo.getValidator().validate(meci);

        int suma = 0;
        Connection con = dbUtils.getConnection();
        String statementString = "SELECT bilete_cumparate FROM Vanzari WHERE id_meci=?";
        try(PreparedStatement statement = con.prepareStatement(statementString)){
            statement.setInt(1,meci.getId());
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                int nr = resultSet.getInt("bilete_cumparate");
                suma += nr;
            }
        } catch (SQLException x){
            logger.error(x);
            x.printStackTrace();
        }
        return suma;
    }

    public int calculateLastFreeId()
    {
        int max = 0;
        for(Vanzare v : this.findAll())
        {
            if (v.getId() > max)
                max = v.getId();
        }
        return max;
    }
}
