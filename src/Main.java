import java.io.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class Main {

    public static void main(String[] args) {

    }

    public static void fileRename(String path) {
        path = "E:\\mastercom\\sql\\导入";
        File file = new File(path);
        File[] files = file.listFiles();
        for (File file1 : files) {
            String name = file1.getName();
            if (!name.contains("_copy")) {
                file1.renameTo(new File(path + File.separator + name.replace(".bcp", "") + "_copy.bcp"));
            }
            System.out.println(file1.getParent());
        }
    }

    public static void readFile(String path) {
        path = "E:\\mastercom\\log\\clog2.txt";
        String writePath = "E:\\mastercom\\log\\clog2共入库.txt";
        Map<String, Integer> sumMap = new HashMap<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            BufferedWriter writer = new BufferedWriter(new FileWriter(writePath));
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (line.contains("共入库") && !line.contains(" 0 条记录")) {
                    writer.write(line.substring(line.indexOf("共入库")));
                    writer.newLine();
//                    String tableName = line.substring(line.indexOf("上午12:00") + 7, line.indexOf(":==缩放级别"));
//                    int count = Integer.valueOf(line.substring(line.indexOf("共入库 ") + 4, line.indexOf(" 条记录")).replace(",", ""));
//                    if (sumMap.containsKey(tableName)) {
//                        sumMap.put(tableName, sumMap.get(tableName) + count);
//                    } else {
//                        sumMap.put(tableName, 0);
//                    }
                }
            }
            reader.close();
//            for (Map.Entry<String, Integer> entry : sumMap.entrySet()) {
//                writer.write("表 " + entry.getKey() + " 共入库 " + entry.getValue() + " 条记录");
//                writer.newLine();
//            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sum(String path) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            String line = null;
            int sum = 0;
            while ((line = reader.readLine()) != null) {
                if (!line.equals("")) {
                    int count = Integer.valueOf(line.substring(line.indexOf("共入库 ") + 4, line.indexOf(" 条记录")));
                    sum = sum + count;
                }
            }
            System.out.println("======sum:" + sum);
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Integer> getCount(String path) {
        List<Integer> list = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (!line.equals("")) {
                    int count = Integer.valueOf(line.substring(line.indexOf("共入库 ") + 4, line.indexOf(" 条记录")));
                    list.add(count);
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 字节数组抓换成 与 i 相对应的进制的字符串
     *
     * @param bytes 字节数组
     * @param i     进制
     */
    public static void byteTranString(byte[] bytes, int i) {
        new BigInteger(1, bytes).toString(i);
    }

    /**
     * 数字自动补0
     * @param num
     */
    public static void format(int num) {
        // 4为最小长度 小于4位前面自动补0 大于4位不操作
        String.format("%04d", num);
    }

    /**
     * List<Integer> 与 int[] 数组转换
     */
    public static void listTranArray() {
        List<Integer> a = new ArrayList<>();
        a.add(66);
        IntStream intStream = a.stream().mapToInt((value) ->
        {
            return value.intValue();
        });
        int[] i = intStream.toArray();
        for (int j : i) {
            System.out.println(j);
        }
    }
}
