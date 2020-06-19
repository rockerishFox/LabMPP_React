package repository;

import domain.Utilizator;
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

public class UtilizatorJdbcRepository implements CRUDRepository<Integer, Utilizator> {
    private JdbcUtils dbUtils;

    private static final Logger logger = LogManager.getLogger();

    private IValidator<Utilizator> validator;

    public UtilizatorJdbcRepository(Properties props, IValidator<Utilizator> val) {
        logger.info("Initializing UtilizatorJdbcRepository with properties: {} ", props);
        dbUtils = new JdbcUtils(props);
        this.validator = val;
    }

    public IValidator<Utilizator> getValidator() {
        return validator;
    }

    @Override
    public int size() {
        logger.traceEntry();

        logger.info(ConsoleColors.YELLOW +
                "se calculeaza numarul de utilizatori din UtilizatorJdbcRepository"
                + ConsoleColors.RESET);
        Connection con = dbUtils.getConnection();
        String statementString = "SELECT count(*) AS [size] FROM Utilizatori";
        try (PreparedStatement statement = con.prepareStatement(statementString)) {
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                logger.traceExit(result.getInt("size"));
                return result.getInt("size");
            }
        } catch (SQLException x) {
            logger.error(x);
            x.printStackTrace();
        }
        return 0;
    }

    @Override
    public void save(Utilizator entity) {
        logger.traceEntry();
        logger.info(ConsoleColors.YELLOW +
                "se salveaza utilizatorul {}"
                + ConsoleColors.RESET,entity);
        Connection con = dbUtils.getConnection();
        String statementString = "INSERT INTO Utilizatori(id,username,password) VALUES (?,?,?)";
        try (PreparedStatement statement = con.prepareStatement(statementString)) {
            statement.setInt(1, entity.getId());
            statement.setString(2, entity.getUsername());
            statement.setString(3, entity.getPassword());

            statement.executeUpdate();
        } catch (SQLException x) {
            logger.error(x);
            x.printStackTrace();
        }
    }

    @Override
    public void delete(Integer integer) {
        logger.traceEntry();
        logger.info(ConsoleColors.YELLOW +
                "se sterge utilizatorul cu id {}"
                + ConsoleColors.RESET, integer);
        Connection con = dbUtils.getConnection();
        String statementString = "DELETE FROM Utilizatori WHERE id=?";
        try (PreparedStatement statement = con.prepareStatement(statementString)) {
            statement.setInt(1, integer);

            statement.executeUpdate();

        } catch (SQLException e) {
            logger.error(e);
            e.printStackTrace();
        }
    }

    @Override
    public void update(Integer integer, Utilizator entity) {
        logger.traceEntry();
        logger.info(ConsoleColors.YELLOW +
                "se actualizeaza utilizatorul cu id-ul {} cu utilizatorul {}"
                + ConsoleColors.RESET,integer,entity);

        Connection con = dbUtils.getConnection();
        String statementString = "UPDATE Utilizatori SET username=?, password=? WHERE id=?";
        try (PreparedStatement statement = con.prepareStatement(statementString)) {
            statement.setString(1, entity.getUsername());
            statement.setString(2, entity.getPassword());
            statement.setInt(3, integer);

            statement.executeUpdate();

        } catch (SQLException c) {
            logger.error(c);
            c.printStackTrace();
        }
    }

    @Override
    public Utilizator findOne(Integer integer) {
        logger.traceEntry();
        logger.info(ConsoleColors.YELLOW +
                "se cauta utilizatorul cu id-ul {}"
                + ConsoleColors.RESET, integer);
        Connection con = dbUtils.getConnection();
        String statementString = "SELECT * FROM Utilizatori WHERE id = ?";
        try (PreparedStatement statement = con.prepareStatement(statementString)) {
            statement.setInt(1, integer);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String user = resultSet.getString("username");
                String password = resultSet.getString("password");

                Utilizator utilizator = new Utilizator(id, user, password);
                logger.traceExit(utilizator);
                return utilizator;
            }

        } catch (SQLException x) {
            logger.error(x);
            x.printStackTrace();
        }

        logger.info("nu s-a gasit utilizator cu id-ul {}", integer);
        logger.traceExit();
        return null;
    }

    @Override
    public Iterable<Utilizator> findAll() {
        logger.traceEntry();

        List<Utilizator> resultList = new ArrayList<>();

        Connection con = dbUtils.getConnection();
        String statementString = "SELECT * FROM Utilizatori";
        try (PreparedStatement statement = con.prepareStatement(statementString)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String user = resultSet.getString("username");
                String password = resultSet.getString("password");

                Utilizator utilizator = new Utilizator(id, user, password);
                resultList.add(utilizator);
            }
        } catch (SQLException x) {
            logger.error(x);
            x.printStackTrace();
        }
        logger.traceExit(resultList);
        return resultList;
    }

    public Utilizator findByUsername(String name){
        logger.traceEntry();
        logger.info(ConsoleColors.YELLOW +
                "se cauta utilizatorul  {}"
                + ConsoleColors.RESET, name);
        Connection con = dbUtils.getConnection();
        String statementString = "SELECT * FROM Utilizatori WHERE username = ?";
        try (PreparedStatement statement = con.prepareStatement(statementString)) {
            statement.setString(1, name);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String user = resultSet.getString("username");
                String password = resultSet.getString("password");

                Utilizator utilizator = new Utilizator(id, user, password);
                logger.traceExit(utilizator);
                return utilizator;
            }

        } catch (SQLException x) {
            logger.error(x);
            x.printStackTrace();
        }

        logger.info(ConsoleColors.YELLOW +
                "nu s-a gasit utilizatorul {}"
                + ConsoleColors.RESET, name);
        logger.traceExit();
        return null;
    }
}
