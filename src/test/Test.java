package test;

/**
 * @author yinyiyun
 * @date 2018/7/6 11:17
 */
public class Test implements TestInterface1, TestInterface2 {

    @Override
    public void test0() {
        System.out.println("test0");
    }

    @Override
    public void test1() {
        System.out.println("test1");
    }

    @Override
    public void test2() {
        TestInterface1.super.test2();
        TestInterface2.super.test2();
        System.out.println("测试接口多重实现3");
    }
}
