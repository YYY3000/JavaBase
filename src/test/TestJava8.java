package test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * java8 新特性
 *
 * @author yinyiyun
 * @date 2018/7/6 10:38
 */
public class TestJava8 {

    private String id;

    private String name;

    public TestJava8() {
    }

    public TestJava8(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "TestJava8{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public static void test1(String s) {
        System.out.println(s);
    }

    public void test2(String s) {
        System.out.println(s);
    }

    public static TestJava8 create(final Supplier<TestJava8> supplier) {
        return supplier.get();
    }

    class InnerClass {

        public void test() {
            System.out.println(id);
        }

    }

    public static void main(String[] args) {
        // 1 Lambda 表达式
        testLambda(() -> System.out.println("testLambda"));

        TestJava8 testJava8 = new TestJava8("1", "3");

        // 2 effectively final(内部内调用局部变量时 会自动给变量加上final修饰 因此不可以再重新赋值)
        testLambda(() -> {
            testJava8.setId("9");
            System.out.println(testJava8.getName());
        });

        List<String> list = new ArrayList<>();
        list.add("1");

        TestJava8 j = TestJava8.create(TestJava8::new);

        // 3 方法引用
        list.forEach(TestJava8::test1);
        list.forEach(j::test2);
        list.forEach(System.out::println);

        // 4 接口的默认实现和静态方法(TestInterface)

        // 5 集合的stream

        // 6 java.time (LocalTime LocalDate)
    }

    private static void testLambda(TestInterface test) {
        test.test();
    }
}
