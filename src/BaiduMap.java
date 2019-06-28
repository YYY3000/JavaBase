import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.client.fluent.Request;
import util.ExcelUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yinyiyun
 * @date 2019/5/8 11:28
 */
public class BaiduMap {

    private static final String ADDRESS_URL = "http://api.map.baidu.com/place/v2/search?region=北京&output=json&ak=42b8ececa9cd6fe72ae4cddd77c0da5d&query=";

    private static final String NAME_URL = "http://api.map.baidu.com/place/v2/suggestion?region=北京&output=json&ak=42b8ececa9cd6fe72ae4cddd77c0da5d&query=";

    private static String getNameByName(String name) {
        try {
            if (name.contains(" ")) {
                name = name.replace(" ", "%20");
            }
            if (name.contains("（")) {
                name = name.replace("（", "%28");
            }
            if (name.contains("）")) {
                name = name.replace("）", "%29");
            }
            String result = Request.Get(NAME_URL + name)
                    .connectTimeout(1000).socketTimeout(1000)
                    .execute().returnContent().asString();
            JSONObject jsonObject = JSONObject.parseObject(result);
            if (jsonObject.containsKey("result")) {
                JSONArray datas = jsonObject.getJSONArray("result");
                if (!datas.isEmpty()) {
                    JSONObject data = datas.getJSONObject(0);
                    if (data.containsKey("name")) {
                        return data.getString("name");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据名称获取地址
     *
     * @param name 名称
     * @return
     */
    private static Map<String, Object> getAddressByName(String name) {
        Map<String, Object> addressMap = new HashMap<>();
        addressMap.put("name", name);
        addressMap.put("searchName", "无");
        addressMap.put("address", "无");
        addressMap.put("addressDetail", "无");
        try {
            String searchName = getNameByName(name);
            if (null == searchName || "".equals(searchName)) {
                return addressMap;
            }
            addressMap.put("searchName", searchName);
            String result = Request.Get(ADDRESS_URL + searchName)
                    .connectTimeout(1000).socketTimeout(1000)
                    .execute().returnContent().asString();
            JSONObject jsonObject = JSONObject.parseObject(result);
            if (jsonObject.containsKey("results")) {
                JSONArray datas = jsonObject.getJSONArray("results");
                if (!datas.isEmpty()) {
                    JSONObject data = datas.getJSONObject(0);
                    String province = data.getString("province");
                    if (null == province) {
                        province = "";
                    }
                    String city = data.getString("city");
                    if (null == city) {
                        city = "";
                    }
                    String area = data.getString("area");
                    if (null == area) {
                        area = "";
                    }
                    String addressDetail = data.getString("address");
                    if (null != addressDetail && !"".equals(addressDetail)) {
                        addressMap.put("addressDetail", addressDetail);
                    }
                    String address = province + city + area;
                    if (!"".equals(address)) {
                        addressMap.put("address", address);
                    }
                    return addressMap;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return addressMap;
    }

    private static List<Map<String, Object>> getAddressList(String path) {
        List<Map<String, Object>> addressList = new ArrayList<>();
        try (FileReader fr = new FileReader(path);
             BufferedReader bf = new BufferedReader(fr);) {
            String line;
            // 按行读取字符串
            while ((line = bf.readLine()) != null) {
                Map<String, Object> addressMap = getAddressByName(line);
                addressList.add(addressMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("===========" + addressList.size());
        return addressList;
    }

    private static void writeData(List<Map<String, Object>> addressList, String path) {
        String name = "公司地址";
        String style = "xlsx";
        List<String> titles = new ArrayList<>();
        titles.add("名称");
        titles.add("搜索名称");
        titles.add("地址");
        titles.add("具体地址");
        ExcelUtil.generateWorkbook(path, name, style, titles, addressList);
    }

    public static void main(String[] args) {
        String path = "F:\\晓儿\\A列.txt";
        List<Map<String, Object>> addressList = getAddressList(path);

        String writePath = "F:\\晓儿\\公司地址A.xlsx";
        writeData(addressList, writePath);

        String path1 = "F:\\晓儿\\C列.txt";
        List<Map<String, Object>> addressList1 = getAddressList(path1);

        String writePath1 = "F:\\晓儿\\公司地址C.xlsx";
        writeData(addressList1, writePath1);
    }

}
