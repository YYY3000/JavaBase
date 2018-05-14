package db;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * @author yinyiyun
 * @date 2018/5/2 10:32
 */
public class BaseClient {

    private static final Logger logger = Logger.getLogger(BaseClient.class);

    private DataType dataType;

    private String url;

    public BaseClient() {
    }

    public BaseClient(DataType dataType, String url) {
        this.dataType = dataType;
        this.url = url;
    }

    public DataType getDataType() {
        return dataType;
    }

    public String getUrl() {
        return url;
    }

    public void executeQuery(String sql, ResultSetHandler handler) throws Exception {
        executeQuery(sql, dataType, url, handler);
    }

    public void executeNonQuery(String sql) throws Exception {
        executeNonQuery(sql, dataType, url);
    }

    public static void executeQuery(String sql, DataType dataType, String url, ResultSetHandler handler) throws Exception {
        Connection connection = SqlQuery.getConnection(dataType, url);
        PreparedStatement statement = SqlQuery.getStatement(connection, sql);
        try {
            logger.info("--sql执行开始--  sql:" + sql);
            ResultSet set = SqlQuery.executeQuery(statement);
            handler.handle(set);
        } catch (Exception e) {
            logger.error("--sql执行失败--  sql:" + sql);
            throw e;
        } finally {
            SqlQuery.close(statement, connection);
        }
    }

    public static ResultSet executeQuery(String sql, DataType dataType, String url) throws Exception {
        Connection connection = SqlQuery.getConnection(dataType, url);
        PreparedStatement statement = SqlQuery.getStatement(connection, sql);
        try {
            logger.info("--sql执行开始--  sql:" + sql);
            ResultSet set = SqlQuery.executeQuery(statement);
            return set;
        } catch (Exception e) {
            logger.error("--sql执行失败--  sql:" + sql);
            throw e;
        } finally {
            SqlQuery.close(statement, connection);
        }
    }

    public static void executeNonQuery(String sql, DataType dataType, String url) throws Exception {
        Connection connection = SqlQuery.getConnection(dataType, url);
        PreparedStatement statement = SqlQuery.getStatement(connection, sql);
        try {
            logger.info("--sql执行开始--  sql:" + sql);
            SqlQuery.executeUpdate(statement);
        } catch (Exception e) {
            logger.error("--sql执行失败--  sql:" + sql);
            throw e;
        } finally {
            SqlQuery.close(statement, connection);
        }
    }
}
