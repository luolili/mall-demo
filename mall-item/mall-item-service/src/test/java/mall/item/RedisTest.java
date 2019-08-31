package mall.item;

import com.mall.ItemApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ItemApplication.class})
//@ContextConfiguration("application.yml")
public class RedisTest {
    @Autowired
    private StringRedisTemplate template;

    @Test
    public void test01() {
        template.opsForValue().set("nni", "halo");
        String re = template.opsForValue().get("nni");
        System.out.println(re);


    }
}
