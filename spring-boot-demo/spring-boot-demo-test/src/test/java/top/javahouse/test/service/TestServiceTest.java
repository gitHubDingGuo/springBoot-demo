package top.javahouse.test.service;


import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TestServiceTest {

     @Autowired
     private  TestService testService;


    @Test
    void doSomething() {
        System.out.println("---执行业务逻辑---");
        testService.doSomething();
        System.out.println("debug-断点");
        //断言二者相等
        Assertions.assertEquals(1,1);
        //断言二者不相等
        Assertions.assertNotEquals(1,2);

        Assertions.assertNull(null);

        Assertions.assertNotNull(null);
        //...... 使用Assertions的方法代替assert可以提高代码可读性
    }

    //all 都是作用于静态方法之上的
    @BeforeAll
    public static void beforeClass() {
        System.out.println("在所有方法执行之前执行 before all");
    }

    @BeforeEach
    public void setup() {
        System.out.println("在每个方法之前 before each ");
    }

    @Test
    public void test_1() {
        System.out.println("test1 执行");
    }

    @Test
    public void test_2() {
        System.out.println("test_2 执行");
    }

    @AfterEach
    public void teardown() {
        System.out.println("在每个测试之后 after each");
    }

    @AfterAll  //只能作用于静态方法
    public static void afterAll() {
        System.out.println("所有测试之后  after all");
    }

}