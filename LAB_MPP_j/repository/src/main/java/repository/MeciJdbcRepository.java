package repository;

import domain.Echipa;
import domain.Meci;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import repository.utils.JdbcUtils;
import utils.ConsoleColors;
import validator.IValidator;
import validator.ValidationException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class MeciJdbcRepository implements CRUDRepository<Integer, Meci> {
    private JdbcUtils dbUtils;
    private IValidator<Meci> validator;

    private static final Logger logger = LogManager.getLogger();

    private EchipaJdbcRepository repoEchipa;

    public MeciJdbcRepository(Properties props, IValidator<Meci> v, EchipaJdbcRepository repoEchipa){
        logger.info(ConsoleColors.YELLOW + "Initializing MeciJdbcRepository with properties: {} " + ConsoleColors.RESET,props);
        dbUtils=new JdbcUtils(props);
        this.repoEchipa = repoEchipa;
        this.validator = v;
    }

    public IValidator<Meci> getValidator() {
        return validator;
    }


    @Override
    public int size() {
        logger.traceEntry();
        logger.info(ConsoleColors.YELLOW +
                        "se calculeaza numarul de echipe din MeciJdbcRepository"
                        + ConsoleColors.RESET);
        Connection con = dbUtils.getConnection();
        String statementString = "SELECT count(*) AS [size] FROM Meciuri";
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
    public void save(Meci entity) {
        logger.traceEntry( );

        validator.validate(entity);
        logger.info(ConsoleColors.YELLOW +
                "se salveaza echipa {}"
                + ConsoleColors.RESET, entity);

        Echipa e1 = repoEchipa.findOne(entity.getEchipa1().getId());
        Echipa e2 = repoEchipa.findOne(entity.getEchipa2().getId());

        if(e1 == null || e2 == null)
            throw new ValidationException("Nu exista o echipa!");

        Connection con = dbUtils.getConnection();
        String statementString = "INSERT INTO Meciuri(id,pret,nr_bilete,scor,echipa1,echipa2) VALUES (?,?,?,?,?,?)";
        try(PreparedStatement statement = con.prepareStatement(statementString)){
            statement.setInt(1,entity.getId());
            statement.setDouble(2,entity.getPret());
            statement.setInt(3,entity.getNrBilete());
            statement.setString(4,entity.getScor());
            statement.setInt(5,entity.getEchipa1().getId());
            statement.setInt(6,entity.getEchipa2().getId());

            statement.executeUpdate();
        }catch (SQLException x){
            logger.error(x);
            x.printStackTrace();
        }
    }

    @Override
    public void delete(Integer integer) {
        logger.traceEntry();

        logger.info(ConsoleColors.YELLOW +
                "se sterge echipa cu id-ul {}"
                + ConsoleColors.RESET, integer);
        Connection con = dbUtils.getConnection();
        String statementString = "DELETE FROM Meciuri WHERE id=?";
        try(PreparedStatement statement = con.prepareStatement(statementString)){
            statement.setInt(1,integer);

            statement.executeUpdate();

        } catch (SQLException e){
            logger.error(e);
            e.printStackTrace();
        }
    }

    @Override
    public void update(Integer integer, Meci entity) {
        logger.traceEntry();

        validator.validate(entity);

        logger.info(ConsoleColors.YELLOW +
                "actualizare meci cu id {} cu meciul {}"
                + ConsoleColors.RESET,integer, entity);

        Connection con = dbUtils.getConnection();
        String statementString = "UPDATE Meciuri SET pret=?, nr_bilete=?, scor=?, echipa1=?, echipa2=? WHERE id=?";
        try(PreparedStatement statement = con.prepareStatement(statementString)){
            statement.setDouble(1,entity.getPret());
            statement.setInt(2,entity.getNrBilete());
            statement.setString(3,entity.getScor());
            statement.setInt(4,entity.getEchipa1().getId());
            statement.setInt(5,entity.getEchipa2().getId());
            statement.setInt(6,integer);

            statement.executeUpdate();

        } catch (SQLException c){
            logger.error(c);
            c.printStackTrace();
        }
    }

    @Override
    public Meci findOne(Integer integer) {
        logger.traceEntry();

        logger.info(ConsoleColors.YELLOW +
                "se cauta meciul cu id-ul {}"
                + ConsoleColors.RESET,integer);
        Connection con = dbUtils.getConnection();
        String statementString = "SELECT * FROM Meciuri WHERE id = ?";
        try(PreparedStatement statement = con.prepareStatement(statementString)){
            statement.setInt(1,integer);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()){
                int id = resultSet.getInt("id");
                Double pret = resultSet.getDouble("pret");
                int nr_bilete = resultSet.getInt("nr_bilete");
                String scor = resultSet.getString("scor");
                int echipa1 = resultSet.getInt("echipa1");
                int echipa2 = resultSet.getInt("echipa2");

                Echipa e1 = repoEchipa.findOne(echipa1);
                Echipa e2 = repoEchipa.findOne(echipa2);

                Meci meci = new Meci(id,pret,nr_bilete,scor,e1,e2);

                logger.traceExit(meci);
                return meci;
            }

        } catch (SQLException x){
            logger.error(x);
            x.printStackTrace();
        }

        logger.info("niciun meci gasit cu id-ul {}", integer);
        logger.traceExit();
        return null;
    }

    @Override
    public Iterable<Meci> findAll() {
        logger.traceEntry();

        List<Meci> resultList = new ArrayList<>();

        Connection con = dbUtils.getConnection();
        String statementString = "SELECT * FROM Meciuri";
        try(PreparedStatement statement = con.prepareStatement(statementString)){
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                int id = resultSet.getInt("id");
                Double pret = resultSet.getDouble("pret");
                int nr_bilete = resultSet.getInt("nr_bilete");
                String scor = resultSet.getString("scor");
                int echipa1 = resultSet.getInt("echipa1");
                int echipa2 = resultSet.getInt("echipa2");

                Echipa e1 = repoEchipa.findOne(echipa1);
                Echipa e2 = repoEchipa.findOne(echipa2);

                Meci meci = new Meci(id,pret,nr_bilete,scor,e1,e2);
                resultList.add(meci);
            }
        } catch (SQLException x){
            logger.error(x);
            x.printStackTrace();
        }
        logger.traceExit(resultList);
        return resultList;
    }

    public Iterable<Meci> findAllForEchipa(Echipa e){
        logger.traceEntry();
        repoEchipa.getValidator().validate(e);
        List<Meci> resultList = new ArrayList<>();

        Connection con = dbUtils.getConnection();
        String statementString = "SELECT * FROM Meciuri WHERE echipa1=? OR echipa2=?";
        try(PreparedStatement statement = con.prepareStatement(statementString)){
            statement.setInt(1,e.getId());
            statement.setInt(2,e.getId());
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                int id = resultSet.getInt("id");
                Double pret = resultSet.getDouble("pret");
                int nr_bilete = resultSet.getInt("nr_bilete");
                String scor = resultSet.getString("scor");
                int echipa1 = resultSet.getInt("echipa1");
                int echipa2 = resultSet.getInt("echipa2");

                Echipa e1 = repoEchipa.findOne(echipa1);
                Echipa e2 = repoEchipa.findOne(echipa2);

                Meci meci = new Meci(id,pret,nr_bilete,scor,e1,e2);
                resultList.add(meci);
            }
        } catch (SQLException x){
            logger.error(x);
            x.printStackTrace();
        }
        logger.traceExit(resultList);
        return resultList;
    }
}
