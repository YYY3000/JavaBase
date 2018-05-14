package db;

import com.microsoft.sqlserver.jdbc.SQLServerBulkCopy;
import com.microsoft.sqlserver.jdbc.SQLServerBulkCopyOptions;
import com.sun.rowset.CachedRowSetImpl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public class BcpStore {

    private String m_tableName = null;
    private Connection m_conn = null;
    private CachedRowSetImpl m_rowSet = null;
    private int m_columnCount = 0;
    private SQLServerBulkCopy m_bulkCopy = null;
    private boolean m_needCloseConn = false;

    /**
     * 当记录超过此数量时,needFlush 返回true
     */
    private int needFlushCount = 20000;

    /**
     * 当前记录条数
     *
     * @return
     */
    public int getSize() {
        if (m_rowSet == null) {
            return 0;
        }
        return m_rowSet.size();
    }

    public int getColumnCount() {
        return m_columnCount;
    }

    public boolean needFlush() {
        return this.getSize() >= needFlushCount;
    }

    public BcpStore() {

    }

    public BcpStore(String strConn, String tableName) {
        this.init(strConn, tableName);
    }

    private void init(String strConn, String tableName) {
        m_needCloseConn = true;
        Connection conn = SqlQuery.getConnection(DataType.SQLSERVER, strConn);
        this.init(conn, tableName);
    }

    private void init(Connection conn, String tableName) {
        this.m_conn = conn;
        this.m_tableName = tableName;
        m_rowSet = this.getCachedRowSet();
        m_bulkCopy = this.getSQLServerBulkCopy();

    }

    /**
     * 获取空的CachedRowSet对象
     */
    private CachedRowSetImpl getCachedRowSet() {
        try {
            //查询出空值用于构建CachedRowSetImpl对象以省去列映射的步骤
            String sql = "select * from " + m_tableName + " where 1 != 1";
            ResultSet rs = SqlQuery.executeQuery(m_conn, sql);
            CachedRowSetImpl crs = new CachedRowSetImpl();
            // 缓存
            crs.populate(rs);
            ResultSetMetaData rsmd = crs.getMetaData();
            // 获取列数
            m_columnCount = rsmd.getColumnCount();
            return crs;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 批量插入操作对象
     *
     * @return
     */
    private SQLServerBulkCopy getSQLServerBulkCopy() {
        try {
            SQLServerBulkCopyOptions copyOptions = new SQLServerBulkCopyOptions();
            copyOptions.setBulkCopyTimeout(6000);
            SQLServerBulkCopy bulkCopy = new SQLServerBulkCopy(m_conn);
            bulkCopy.setBulkCopyOptions(copyOptions);
            bulkCopy.setDestinationTableName(m_tableName);
            return bulkCopy;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 数据类型必须和表里面的能对应上
     *
     * @param objs
     * @return
     */
    public boolean addData(Object... objs) {
        // 插入的参数必须与表的列数一致
        if (objs.length != m_columnCount) return false;

        try {
            m_rowSet.last();
            // 移动指针到“插入行”，插入行是一个虚拟行
            m_rowSet.moveToInsertRow();

            for (int i = 0; i < objs.length; i++) {
                m_rowSet.updateObject(i + 1, objs[i]);
            }
            // 插入虚拟行
            m_rowSet.insertRow();
            // 移动指针到当前行
            m_rowSet.moveToCurrentRow();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 批量插入
     *
     * @return
     */
    public boolean flush() {
        int count = getSize();
        try {
            // 写入数据库
            m_bulkCopy.writeToServer(m_rowSet);
            // 清空缓存
            this.clear();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 清空缓存
     */
    private void clear() {
        if (m_rowSet != null) {
            try {
                m_rowSet.release();
            } catch (Exception e) {
            }
        }
    }

    /**
     * 关闭连接
     */
    public void close() {
        if (m_rowSet != null) {
            try {
                m_rowSet.close();
            } catch (Exception e) {
            }
        }

        if (m_bulkCopy != null) {
            m_bulkCopy.close();
        }

        if (m_needCloseConn && m_conn != null) {
            try {
                m_conn.close();
            } catch (Exception e) {
            }
        }
    }

    public static void main(String[] args) {
        String strConn = "jdbc:sqlserver://localhost:1433;DatabaseName=test;user=test;password=test";
        BcpStore store = new BcpStore(strConn, "test");
        store.addData(1101, 28513, 17, 18, 13.0, null);
        store.flush();
        store.close();
    }
}
