package repository;

import domain.Echipa;
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

public class EchipaJdbcRepository implements CRUDRepository<Integer, Echipa> {
    private JdbcUtils dbUtils;

    private static final Logger logger = LogManager.getLogger();

    private IValidator<Echipa> validator;

    public EchipaJdbcRepository(Properties props, IValidator<Echipa> val){
        logger.info(ConsoleColors.YELLOW +
                        "Initializing EchipaJdbcRepository with properties: {} "
                        + ConsoleColors.RESET
                ,props);
        dbUtils=new JdbcUtils(props);
        this.validator = val;
    }


    public IValidator<Echipa> getValidator() {
        return validator;
    }

    @Override
    public int size() {
        logger.traceEntry();
        logger.info(ConsoleColors.YELLOW + "se calculeaza numarul de echipe din EchipaJdbcRepository" + ConsoleColors.RESET);
        Connection con = dbUtils.getConnection();
        String statementString = "SELECT count(*) AS 'size' FROM Echipe";
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
    public void save(Echipa entity) {
        logger.traceEntry( );
        logger.info(ConsoleColors.YELLOW +
                "se salveaza echipa {}"
                + ConsoleColors.RESET, entity);

        Connection con = dbUtils.getConnection();
        String statementString = "INSERT INTO Echipe VALUES (?,?,?,?)";
        try(PreparedStatement statement = con.prepareStatement(statementString)){
            statement.setInt(1,entity.getId());
            statement.setString(2,entity.getNume());
            statement.setInt(3,entity.getNrJucatori());
            statement.setString(4,entity.getOras());

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
        String statementString = "DELETE FROM Echipe WHERE id=?";
        try(PreparedStatement statement = con.prepareStatement(statementString)){
            statement.setInt(1,integer);

            statement.executeUpdate();

        } catch (SQLException e){
            logger.error(e);
            e.printStackTrace();
        }
    }

    @Override
    public void update(Integer integer, Echipa entity) {
        logger.traceEntry();

        logger.info(ConsoleColors.YELLOW +
                "actualizare echipa cu id {} cu echipa {}"
                + ConsoleColors.RESET, integer, entity);

        Connection con = dbUtils.getConnection();
        String statementString = "UPDATE Echipe SET nume=?, nr_jucatori=?, oras=? WHERE id=?";
        try(PreparedStatement statement = con.prepareStatement(statementString)){
            statement.setString(1,entity.getNume());
            statement.setInt(2,entity.getNrJucatori());
            statement.setString(3,entity.getOras());
            statement.setInt(4,integer);

            statement.executeUpdate();

        } catch (SQLException c){
            logger.error(c);
            c.printStackTrace();
        }

    }

    @Override
    public Echipa findOne(Integer integer) {
        logger.traceEntry();
        logger.info(ConsoleColors.YELLOW +
                        "se cauta echipa cu id-ul {} "
                        + ConsoleColors.RESET
                ,integer);

        Connection con = dbUtils.getConnection();
        String statementString = "SELECT * FROM Echipe WHERE id = ?";
        try(PreparedStatement statement = con.prepareStatement(statementString)){
            statement.setInt(1,integer);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()){
                int id = resultSet.getInt("id");
                String nume = resultSet.getString("nume");
                String oras = resultSet.getString("oras");
                int players = resultSet.getInt("nr_jucatori");

                Echipa echipa = new Echipa(id,nume,oras,players);
                logger.traceExit(echipa);
                return echipa;
            }

        } catch (SQLException x){
            logger.error(x);
            x.printStackTrace();
        }

        logger.info("nu s-a gasit echipa cu id-ul {}", integer);
        logger.traceExit();
        return null;
    }

    @Override
    public Iterable<Echipa> findAll() {
        logger.traceEntry();

        List<Echipa> resultList = new ArrayList<>();

        Connection con = dbUtils.getConnection();
        String statementString = "SELECT * FROM Echipe";
        try(PreparedStatement statement = con.prepareStatement(statementString)){
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                int id = resultSet.getInt("id");
                String nume = resultSet.getString("nume");
                String oras = resultSet.getString("oras");
                int players = resultSet.getInt("nr_jucatori");

                Echipa echipa = new Echipa(id,nume,oras,players);
                resultList.add(echipa);
            }
        } catch (SQLException x){
            logger.error(x);
            x.printStackTrace();
        }
        logger.traceExit(resultList);
        return resultList;
    }
}
