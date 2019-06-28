import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author yinyiyun
 * @date 2018/5/16 13:35
 */
public class Test {

    static boolean foo(char c) {
        System.out.println(c);
        return true;
    }

    private int a;

    private String b;

    public Test(int a, String b) {
        this.a = a;
        this.b = b;
    }

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    @Override
    public String toString() {
        return "Test{" +
                "a=" + a +
                ", b='" + b + '\'' +
                '}';
    }

    public double getSpeed(RegionType regionType) {
        Supplier<Double> supplier = getSpeedFunctionMap.get(regionType);
        if (supplier == null) {
            return 0;
        }
        Supplier<Double> supplier1 = this::getSpeedAfrican;
        return supplier.get();
    }

    private enum RegionType {
        EUROPEAN, AFRICAN, NORWEGIAN_BLUE
    }

    private Map<RegionType, Supplier<Double>> getSpeedFunctionMap;

    {
        getSpeedFunctionMap = new EnumMap<>(RegionType.class);
        getSpeedFunctionMap.put(RegionType.EUROPEAN, this::getSpeedEuropean);
        getSpeedFunctionMap.put(RegionType.AFRICAN, this::getSpeedAfrican);
        getSpeedFunctionMap.put(RegionType.NORWEGIAN_BLUE, this::getSpeedNorwegianBlue);
    }

    private double getSpeedEuropean() {

        return 2;
    }

    private double getSpeedAfrican() {
        return 2 - 0.7 * 1;
    }

    private double getSpeedNorwegianBlue() {
        return 3;
    }

    public static void main(String[] args) {
        int i = 0;
        for (foo('A'); foo('B') && i < 2; foo('C')) {
            i++;
            foo('D');
        }
    }

}
