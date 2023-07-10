import io.github.fairyspace.context.support.ClassPathXmlApplicationContext;
import io.github.fairyspace.test.bean.Husband;
import io.github.fairyspace.test.bean.Wife;
import org.junit.Test;

public class ApiTest {
    @Test
    public void testCircular() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        Husband husband = applicationContext.getBean("husband", Husband.class);
        Wife wife = applicationContext.getBean("wife", Wife.class);
        System.out.println("husband执行：" + husband.queryWife());
        System.out.println("wife执行：" + wife.queryHusband());
    }

}



