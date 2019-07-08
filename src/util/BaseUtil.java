package util;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class BaseUtil {

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
        IntStream intStream = a.stream().mapToInt((value) -> value.intValue());
        int[] i = intStream.toArray();
        for (int j : i) {
            System.out.println(j);
        }
    }
}
