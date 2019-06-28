import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.client.fluent.Request;

import java.io.IOException;

/**
 * @author yinyiyun
 * @date 2019/5/8 11:28
 */
public class GaodeMap {

    private static final String AREASHAPE_URL = "http://restapi.amap.com/v3/config/district?key=77441ed52a769b39a5201731c6374a7b&subdistrict=1&extensions=all&keywords=";

    private static String getAreaShape(String code) {
        try {
            String result = Request.Get(AREASHAPE_URL + code)
                    .connectTimeout(1000).socketTimeout(1000)
                    .execute().returnContent().asString();
            JSONObject jsonObject = JSONObject.parseObject(result);
            if (jsonObject.containsKey("districts")) {
                JSONArray datas = jsonObject.getJSONArray("districts");
                if (!datas.isEmpty()) {
                    JSONObject data = datas.getJSONObject(0);
                    if (data.containsKey("polyline")) {
                        return data.getString("polyline");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



}
