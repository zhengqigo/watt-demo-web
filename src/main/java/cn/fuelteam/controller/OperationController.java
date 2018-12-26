package cn.fuelteam.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cn.fuelteam.data.dto.OperationDto;
import cn.fuelteam.manager.OperationManager;

@RestController
public class OperationController {

    @Autowired
    private OperationManager operationManager;

    @RequestMapping(value = "/operation/{id}", method = RequestMethod.GET)
    @ResponseBody
    public OperationDto getOperation(@PathVariable("id") Long id) {
        return operationManager.get(id);
    }
}
