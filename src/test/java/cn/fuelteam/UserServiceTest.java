package cn.fuelteam;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.fuelteam.DemoWebApplication;
import cn.fuelteam.data.dto.OperationDto;
import cn.fuelteam.manager.OperationManager;
import cn.fuelteam.manager.UserManager;
import cn.fuelteam.user.dto.UserDto;

@SpringBootTest(classes = DemoWebApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class UserServiceTest {

    @Autowired
    private OperationManager operationManager;

    @Autowired
    private UserManager userManager;

    @Test
    public void testFindById() {
        OperationDto operation = operationManager.get(1L);
        System.err.println(operation);
    }

    @Test
    public void testInsert() throws InterruptedException {
        userManager.save(new UserDto().setUsername("test").setPassword("123456").setCreatedAt(new Date()).setAddress("Shanghai"));
        operationManager.save(new OperationDto().setContents("test"));
    }
}
