import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import db.MysqlUtil;

import java.awt.Polygon;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class Main {

    private static Integer ratio = 10000000;

    public static void main(String[] args) {
        HashMap map = new HashMap();
        String s1 = "1";
        String s2 = "2";
        Test t1 = new Test(1, "1");
        Test t2 = new Test(2, "2");
        map.put(s1, t1);
        map.put(s2, t2);
        System.gc();
        System.out.println("第1步" + map);

        s1= null;
        System.gc();
        System.out.println("第2步" + map);

        map.clear();
        System.gc();
        System.out.println("第3步" + map);
        System.out.println("第3步" + t1);
        System.out.println("第3步" + t2);

        map = null;
        System.gc();
        System.out.println("第3步" + t1);
        System.out.println("第3步" + t2);
    }

    private static int toInt32(byte[] b, int pos) {
        return (b[pos + 0] & 0xFF) | ((b[pos + 1] & 0xFF) << 8) | ((b[pos + 2] & 0xFF) << 16)
                | ((b[pos + 3] & 0xFF) << 24);
    }

    public static Polygon createPolygon(byte[] borderBytes) {
        Polygon pg = new Polygon();

        int pos = 0;
        int lng = 0;
        int lat = 0;
        while (pos < borderBytes.length) {
            lng = toInt32(borderBytes, pos);
            pos += 4;

            lat = toInt32(borderBytes, pos);
            pos += 4;

            pg.addPoint(lng, lat);
        }

        if (pg.npoints > 0) {
            if ((pg.xpoints[0] != pg.xpoints[pg.npoints - 1]) || (pg.ypoints[0] != pg.ypoints[pg.npoints - 1])) {
                pg.addPoint(pg.xpoints[0], pg.ypoints[0]);
            }

            pg.getBounds();

            return pg;
        } else {
            return null;
        }
    }

    public static List<Double[]> getDoublePoints(byte[] bytes) {
        List<Double[]> ls = new ArrayList<>();
        Polygon polygon = createPolygon(bytes);
        if (null != polygon) {
            int[] xpoints = polygon.xpoints;
            int[] ypoints = polygon.ypoints;
            for (int i = 0; i < polygon.npoints; i++) {
                Double[] points = new Double[2];
                points[0] = (double) xpoints[i] / ratio;
                points[1] = (double) ypoints[i] / ratio;
                ls.add(points);
            }
        }
        return ls;
    }

    private static void writeData() {
        MysqlUtil mysqlUtil = new MysqlUtil("192.168.1.60", "3306", "upos_city_main", "root", "mastercom168");
        String sql = "INSERT INTO `upos_city_main`.`province_city_test`(`name`, `id`, `longitude`, `latitude`, `parentid`) VALUES (?, ?, ?, ?, ?)";

        String path = "F:\\项目\\位置服务\\geometryProvince";
        String json = readFile(path + "\\qinghai.json");
        JSONObject jsonObject = JSONObject.parseObject(json);
        JSONArray jsonArray = jsonObject.getJSONArray("features");
        System.out.println(jsonArray);
//        for (int i = 0; i < jsonArray.size(); i++) {
//            JSONObject featuresObject = jsonArray.getJSONObject(i);
//            JSONObject propertiesObject = featuresObject.getJSONObject("properties");
//            String id = propertiesObject.getString("id");
//            String name = propertiesObject.getString("name");
//            if (name.contains("自治区")) {
//                name = name.replace("自治区", "");
//            }
//            if (name.contains("省")) {
//                name = name.replace("省", "");
//            }
//            if (name.contains("市")) {
//                name = name.replace("市", "");
//            }
//            JSONArray cps = propertiesObject.getJSONArray("cp");
//            Integer longitude = null;
//            Integer latitude = null;
//            if (cps != null) {
//                longitude = (int) (cps.getDouble(0) * ratio);
//                latitude = (int) (cps.getDouble(1) * ratio);
//            }
//            try {
//                mysqlUtil.executeNonQuery(sql, name, id, longitude, latitude, "");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//
//            String cityJson = readFile(path + "\\" + id + ".json");
//            JSONObject cityJsonObject = JSONObject.parseObject(cityJson);
//            JSONArray cityJsonArray = cityJsonObject.getJSONArray("features");
//            for (int i1 = 0; i1 < cityJsonArray.size(); i1++) {
//                try {
//                    JSONObject cityFeaturesObject = cityJsonArray.getJSONObject(i1);
//                    JSONObject cityPropertiesObject = cityFeaturesObject.getJSONObject("properties");
//                    String cityId = cityPropertiesObject.getString("id");
//                    String cityName = cityPropertiesObject.getString("name");
//                    if (cityName.contains("市")) {
//                        cityName = cityName.replace("市", "");
//                    }
//                    if (cityName.contains("地区")) {
//                        cityName = cityName.replace("地区", "");
//                    }
//                    if (cityName.contains("区")) {
//                        cityName = cityName.replace("区", "");
//                    }
//                    if (cityName.contains("自治州")) {
//                        cityName = cityName.replace("自治州", "");
//                    }
//                    JSONArray cityCps = cityPropertiesObject.getJSONArray("cp");
//                    Integer cityLongitude = null;
//                    Integer cityLatitude = null;
//                    if (cityCps != null) {
//                        cityLongitude = (int) (cityCps.getDouble(0) * ratio);
//                        cityLatitude = (int) (cityCps.getDouble(1) * ratio);
//                    }
//                    mysqlUtil.executeNonQuery(sql, cityName, cityId, cityLongitude, cityLatitude, id);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
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

    public static String readFile(String path) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            StringBuffer sb = new StringBuffer();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
     *
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
