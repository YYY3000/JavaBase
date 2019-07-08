package util;

import java.util.Random;

/**
 * 随机数
 *
 * @author yinyiyun
 * @date 2019/7/8 11:06
 */
public class RandomUtil {

    public static void main(String[] args) {

    }

    static int random(int n) {
        Random random = new Random();
        return Math.abs(random.nextInt()) % n;
    }

    static int random2(int n) {
        Random random = new Random();
        return random.nextInt(n);
    }

}
