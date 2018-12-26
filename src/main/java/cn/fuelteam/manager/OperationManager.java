package cn.fuelteam.manager;

import org.fuelteam.watt.result.Result;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Reference;

import cn.fuelteam.data.client.OperationClient;
import cn.fuelteam.data.dto.OperationDto;

@Component
public class OperationManager {

    @Reference(version = "1.0.0", check = false, retries = 0, timeout = 3000)
    private OperationClient operationClient;

    public OperationDto get(Long id) {
        Result<OperationDto> result = operationClient.findById(id);
        if (result.succeded()) return result.getData();
        return null;
    }

    public Integer save(OperationDto operation) {
        Result<Integer> result = operationClient.insert(operation);
        if (result.succeded()) return result.getData();
        return null;
    }
}
