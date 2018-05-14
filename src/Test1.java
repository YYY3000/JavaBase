import java.util.ArrayList;
import java.util.List;

/**
 * @author yinyiyun
 * @date 2018/5/4 15:06
 */
public class Test1 {

    private String a;

    private String b;

    public List<String> l = new ArrayList<>();

    public Test1(String a, String b) {
        this.a = a;
        this.b = b;
        l.add(a);
        l.add(b);
    }
}
