package db;

import util.DateUtil;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * mysql读写工具类
 *
 * @author yinyiyun
 * @date 2018/6/20 10:25
 */
public class MysqlUtil extends DatabaseUtil {

    private static String driver = "com.mysql.jdbc.Driver";

    public MysqlUtil(String serverName, String port, String dbName, String user, String password) {
        super(serverName, port, dbName, user, password);
    }

    @Override
    protected String getUrl(String serverName, String port, String dbName) {
        return MessageFormat.format("jdbc:mysql://{0}:{1}/{2}?serverTimezone=UTC", serverName, port, dbName);
    }

    @Override
    public String getDriver() {
        return driver;
    }

    public static void main(String[] args) {
        try {
            MysqlUtil mysqlUtil = new MysqlUtil("192.168.1.60", "3306", "upos_city_main", "root", "mastercom168");
            String sql = "INSERT INTO `upos_city_main`.`tb_cfg_system_task`(`tasktime`, `genetime`, `dealtime`, `status`) VALUES (?, '2019-01-22 15:46:13', NULL, 20);";
            Calendar calendar = Calendar.getInstance();
            Date date = DateUtil.getDateByString("2018-12-19 00:00:00");
            calendar.setTime(date);
            for (int i = 0; i < 96; i++) {
                String taskTime = DateUtil.transDateToString(calendar.getTime(), "yyyyMMddHHmm");
                mysqlUtil.executeNonQuery(sql, taskTime);
                calendar.add(Calendar.MINUTE, 15);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
