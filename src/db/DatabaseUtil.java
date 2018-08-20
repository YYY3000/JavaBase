package db;

import java.text.MessageFormat;
import java.util.Properties;

/**
 * 数据库 读写工具类(包括 源生jdbc操作 和 spark读写)
 *
 * @author yinyiyun
 * @date 2018/6/5 18:36
 */
public abstract class DatabaseUtil {

    private String url;

    private Properties properties;

    private DatabaseClient client;

    /**
     * 数据库连接
     *
     * @param url      url
     * @param user     用户
     * @param password 密码
     */
    public DatabaseUtil(String url, String user, String password) {
        this.url = url;
        this.properties = getProperties(user, password, this.getDriver());
        this.client = new DatabaseClient(this.getDriver(), url, user, password);
    }

    /**
     * 数据库连接
     *
     * @param serverName 数据库服务器名称(或ip)
     * @param port       端口
     * @param dbName     数据库名称
     * @param user       用户
     * @param password   密码
     */
    public DatabaseUtil(String serverName, String port, String dbName, String user, String password) {
        this.url = this.getUrl(serverName, port, dbName);
        this.properties = getProperties(user, password, this.getDriver());
        this.client = new DatabaseClient(this.getDriver(), this.url, user, password);
    }

    /**
     * 获取参数
     *
     * @param user     用户名
     * @param password 密码
     * @param driver   驱动
     * @return
     */
    private static Properties getProperties(String user, String password, String driver) {
        Properties properties = new Properties();
        properties.setProperty("driver", driver);
        properties.setProperty("user", user);
        properties.setProperty("password", password);
        return properties;
    }

    /**
     * 拼装url
     *
     * @param serverName 数据库服务器名称(或ip)
     * @param port       数据库连接端口
     * @param dbName     数据库名称
     * @return
     */
    protected abstract String getUrl(String serverName, String port, String dbName);

    /**
     * 获取驱动
     *
     * @return
     */
    protected abstract String getDriver();

    /**
     * 根据已存在表的表结构创建新表 (源生jdbc操作)
     *
     * @param oldTableName 旧表名
     * @param newTableName 新表名
     */
    public void copyTableStructure(String oldTableName, String newTableName) throws Exception {
        String sql = "CREATE TABLE IF NOT EXISTS {0} (LIKE {1})";
        this.client.executeNonQuery(MessageFormat.format(sql, newTableName, oldTableName));
    }

    /**
     * 复制已存在表的表结构和数据至新表 (源生jdbc操作)
     *
     * @param oldTableName 旧表名
     * @param newTableName 新表名
     */
    public void copyTable(String oldTableName, String newTableName) throws Exception {
        String sql = "CREATE TABLE IF NOT EXISTS {0} AS (SELECT * FROM {1})";
        this.client.executeNonQuery(MessageFormat.format(sql, newTableName, oldTableName));
    }

    /**
     * 表重命名 (源生jdbc操作)
     *
     * @param oldTableName 旧表名
     * @param newTableName 新表名
     */
    public void renameTable(String oldTableName, String newTableName) throws Exception {
        String sql = "ALTER TABLE {0} RENAME {1}";
        this.client.executeNonQuery(MessageFormat.format(sql, oldTableName, newTableName));
    }

    /**
     * 查询 (源生jdbc操作)
     *
     * @param sql     sql
     * @param handler 结果集处理
     * @param params  参数
     */
    public void executeQuery(String sql, ResultSetHandler handler, Object... params) throws Exception {
        this.client.executeQuery(sql, handler, params);
    }

    /**
     * 更新 (源生jdbc操作)
     *
     * @param sql    sql
     * @param params 参数
     */
    public void executeNonQuery(String sql, Object... params) throws Exception {
        this.client.executeNonQuery(sql, params);
    }

}
