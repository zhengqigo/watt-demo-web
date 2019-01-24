package cn.fuelteam.manager;

import org.fuelteam.watt.result.Result;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Reference;

import cn.fuelteam.user.client.UserClient;
import cn.fuelteam.user.dto.UserDto;

@Component
public class UserManager {

    @Reference(version = "1.0.0", check = false, retries = 0, timeout = 3000)
    private UserClient userClient;

    public UserDto get(Long id) {
        Result<UserDto> result = userClient.findById(id);
        if (result.succeded()) return result.getData();
        return null;
    }

    public Integer save(UserDto user) {
        Result<Integer> result = userClient.insert(user);
        if (result.succeded()) return result.getData();
        return null;
    }
}
