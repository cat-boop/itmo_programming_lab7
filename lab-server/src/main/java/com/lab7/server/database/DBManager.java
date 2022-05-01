package com.lab7.server.database;

import com.lab7.common.entity.Coordinates;
import com.lab7.common.entity.Location;
import com.lab7.common.entity.Route;
import com.lab7.common.util.Request;
import com.lab7.server.encryption.IEncryptor;
import com.lab7.server.logger.ServerLogger;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.NavigableSet;
import java.util.TreeSet;

public class DBManager {
    private final Connection connection;
    private final IEncryptor encryptor;

    public DBManager(String dbUrl, String username, String password, IEncryptor encryptor) throws SQLException {
        connection = DriverManager.getConnection(dbUrl, username, password);
        if (connection != null) {
            System.out.println("Соединение с базой данных успешно установлено");
            this.encryptor = encryptor;
        } else {
            throw new SQLException("Соединение с базой данных не установлено");
        }
    }

    public NavigableSet<Route> readElementsFromDB() throws SQLException {
        NavigableSet<Route> set = new TreeSet<>();
        PreparedStatement statement = connection.prepareStatement(DBRequest.GET_All_ROUTES.getRequest());
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            Route route = new Route();
            int columnIndex = 1;
            route.setId(resultSet.getLong(columnIndex++));
            route.setClientName(resultSet.getString(columnIndex++));
            route.setName(resultSet.getString(columnIndex++));
            route.setCoordinates(new Coordinates(resultSet.getInt(columnIndex++), resultSet.getLong(columnIndex++)));
            route.setCreationDate(resultSet.getTimestamp(columnIndex++).toLocalDateTime());
            route.setFrom(new Location(resultSet.getInt(columnIndex++), resultSet.getInt(columnIndex++), resultSet.getDouble(columnIndex++), resultSet.getString(columnIndex++)));

            int xTo = resultSet.getInt(columnIndex++);
            int yTo = resultSet.getInt(columnIndex++);
            double zTo = resultSet.getDouble(columnIndex++);
            String nameTo = resultSet.getString(columnIndex++);
            if (nameTo == null) {
                route.setTo(null);
            } else {
                route.setTo(new Location(xTo, yTo, zTo, nameTo));
            }

            route.setDistance(resultSet.getDouble(columnIndex));
            set.add(route);
        }
        return set;
    }

    public long addRouteToDB(Route route) {
        try {
            PreparedStatement preparedStatement = fillRouteStatement(connection.prepareStatement(DBRequest.ADD_NEW_ROUTE.getRequest(), Statement.RETURN_GENERATED_KEYS), route);
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
            return resultSet.getLong(1);
        } catch (SQLException e) {
            ServerLogger.logErrorMessage(e.getMessage());
            return -1;
        }
    }

    public boolean updateRouteByID(Route route) {
        try {
            PreparedStatement preparedStatement = fillRouteStatement(connection.prepareStatement(DBRequest.UPDATE_ROUTE.getRequest()), route);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            ServerLogger.logErrorMessage(e.getMessage());
            return false;
        }
    }

    public boolean deleteClientRoutes(String clientName) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DBRequest.DELETE_CLIENT_ROUTES.getRequest());
            preparedStatement.setString(1, clientName);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            ServerLogger.logErrorMessage(e.getMessage());
            return false;
        }
    }

    public boolean deleteRouteByID(long id) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DBRequest.DELETE_ROUTE_BY_ID.getRequest());
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            ServerLogger.logErrorMessage(e.getMessage());
            return false;
        }
    }

    public boolean deleteRoutesGreaterThanDistance(String clientName, double distance) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DBRequest.DELETE_ALL_GREATER_THAN_DISTANCE_ROUTES.getRequest());
            preparedStatement.setString(1, clientName);
            preparedStatement.setDouble(2, distance);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            ServerLogger.logErrorMessage(e.getMessage());
            return false;
        }
    }

    public boolean deleteRoutesLowerThanDistance(String clientName, double distance) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DBRequest.DELETE_ALL_LOWER_THAN_DISTANCE_ROUTES.getRequest());
            preparedStatement.setString(1, clientName);
            preparedStatement.setDouble(2, distance);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            ServerLogger.logErrorMessage(e.getMessage());
            return false;
        }
    }

    public boolean checkIfUserExist(String clientName) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DBRequest.CHECK_IF_CLIENT_EXIST.getRequest());
            preparedStatement.setString(1, clientName);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            ServerLogger.logErrorMessage(e.getMessage());
            return false;
        }
    }

    public boolean checkIfUserConnect(Request request) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DBRequest.CHECK_IF_CLIENT_ENTER_RIGHT_PASSWORD.getRequest());
            preparedStatement.setString(1, request.getClientName());
            preparedStatement.setString(2, encryptor.encrypt(request.getClientPassword()));
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException | NoSuchAlgorithmException e) {
            ServerLogger.logErrorMessage(e.getMessage());
            return false;
        }
    }

    public boolean registerNewUser(Request request) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DBRequest.REGISTER_NEW_CLIENT.getRequest());
            preparedStatement.setString(1, request.getClientName());
            preparedStatement.setString(2, encryptor.encrypt(request.getClientPassword()));
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException | NoSuchAlgorithmException e) {
            ServerLogger.logErrorMessage(e.getMessage());
            return false;
        }
    }

    private PreparedStatement fillRouteStatement(PreparedStatement preparedStatement, Route route) throws SQLException {
        int parameterIndex = 1;
        preparedStatement.setString(parameterIndex++, route.getClientName());
        preparedStatement.setString(parameterIndex++, route.getName());
        //filed "coordinates" setting
        preparedStatement.setInt(parameterIndex++, route.getCoordinates().getX());
        preparedStatement.setLong(parameterIndex++, route.getCoordinates().getY());
        preparedStatement.setTimestamp(parameterIndex++, Timestamp.valueOf(route.getCreationDate()));
        //field "from" setting
        preparedStatement.setInt(parameterIndex++, route.getFrom().getX());
        preparedStatement.setInt(parameterIndex++, route.getFrom().getY());
        preparedStatement.setDouble(parameterIndex++, route.getFrom().getZ());
        preparedStatement.setString(parameterIndex++, route.getFrom().getName());
        //field "to" setting
        Location to = route.getTo();
        if (to != null) {
            preparedStatement.setInt(parameterIndex++, to.getX());
            preparedStatement.setInt(parameterIndex++, to.getY());
            preparedStatement.setDouble(parameterIndex++, to.getZ());
            preparedStatement.setString(parameterIndex++, to.getName());
        } else {
            preparedStatement.setNull(parameterIndex++, Types.INTEGER);
            preparedStatement.setNull(parameterIndex++, Types.INTEGER);
            preparedStatement.setNull(parameterIndex++, Types.DOUBLE);
            preparedStatement.setNull(parameterIndex++, Types.VARCHAR);
        }
        //filed "distance" setting
        preparedStatement.setDouble(parameterIndex, route.getDistance());
        return preparedStatement;
    }
}
