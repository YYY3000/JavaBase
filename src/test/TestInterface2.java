package test;

/**
 * @author yinyiyun
 * @date 2018/7/6 10:42
 */
public interface TestInterface2 {

    void test0();

    void test1();

    default void test2() {
        System.out.println("测试接口默认实现2");
    }

    static void test3() {
        System.out.println("测试接口静态方法2");
    }

}
