package db;

import com.microsoft.sqlserver.jdbc.SQLServerDriver;

import java.sql.*;

public class SqlQuery {

    /**
     * 根据完整的url获取 sqlServer 的 connection
     *
     * @param url
     * @return
     */
    public static Connection getConnection(DataType type, String url) {
        try {
            switch (type) {
                case GBASE:
                    break;
                case GREENPLUM:
                    break;
                case MYSQL:
                    break;
                case SQLSERVER:
                    Class.forName(SQLServerDriver.class.getName());
                    break;
                default:
                    break;
            }
            return DriverManager.getConnection(url);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据数据库连接配置参数获取 sqlServer 的 connection
     *
     * @param ip
     * @param port
     * @param dbName
     * @param user
     * @param password
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public static Connection getConnection(DataType type, String ip, Integer port, String dbName, String user, String password) {
        return getConnection(type, getUrl(type, ip, port, dbName, user, password));
    }

    /**
     * 根据数据库连接配置参数获取完整连接url
     *
     * @param ip
     * @param port
     * @param dbName
     * @param user
     * @param password
     * @return
     */
    public static String getUrl(DataType type, String ip, Integer port, String dbName, String user, String password) {
        String url = "";
        switch (type) {
            case GBASE:
                break;
            case GREENPLUM:
                break;
            case MYSQL:
                break;
            case SQLSERVER:
                url = "jdbc:sqlserver://" + ip + (null == port ? "" : ":" + port + ";DatabaseName=" + dbName + ";user=" + user + ";password=" + password);
                break;
            default:
                break;
        }
        return url;
    }

    /**
     * 根据 connection 获取 statement
     *
     * @param connection
     * @param sql
     * @param params
     * @return
     * @throws SQLException
     */
    public static PreparedStatement getStatement(Connection connection, String sql, Object... params) {
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }
            return statement;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 数据更新
     *
     * @param statement
     * @return
     * @throws SQLException
     */
    public static int executeUpdate(PreparedStatement statement) throws SQLException {
        return statement.executeUpdate();
    }

    /**
     * 数据查询
     *
     * @param statement
     * @return
     * @throws SQLException
     */
    public static ResultSet executeQuery(PreparedStatement statement) throws SQLException {
        return statement.executeQuery();
    }

    /**
     * 数据更新
     *
     * @param connection
     * @param sql
     * @param params
     * @return
     * @throws SQLException
     */
    public static int executeUpdate(Connection connection, String sql, Object... params) throws SQLException {
        PreparedStatement statement = getStatement(connection, sql, params);
        return executeUpdate(statement);
    }

    /**
     * 数据查询
     *
     * @param connection
     * @param sql
     * @param params
     * @return
     * @throws SQLException
     */
    public static ResultSet executeQuery(Connection connection, String sql, Object... params) throws SQLException {
        PreparedStatement statement = getStatement(connection, sql, params);
        return executeQuery(statement);
    }

    /**
     * 关闭连接
     *
     * @param statement
     * @param connection
     * @throws SQLException
     */
    public static void close(PreparedStatement statement, Connection connection) {
        try {
            if (statement != null) {
                statement.close();
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 重载关闭方法
     *
     * @param statement
     * @param connection
     * @throws Exception
     */
    public void close(ResultSet rs, PreparedStatement statement, Connection connection) {
        try {
            if (rs != null) {
                rs.close();
                if (statement != null) {
                    statement.close();
                    if (connection != null) {
                        connection.close();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            String strConn = "jdbc:sqlserver://localhost:1433;DatabaseName=test;user=test;password=test";
            Connection connection = getConnection(DataType.SQLSERVER, strConn);
            ResultSet rs = executeQuery(connection, "select * from test");
            while (rs.next()) {
                int i = 1;
                int id = rs.getInt(i++);
                System.out.println("id: " + id);
                String name = rs.getString(i++);
                System.out.println("name: " + name);
                System.out.println(rs.wasNull());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
