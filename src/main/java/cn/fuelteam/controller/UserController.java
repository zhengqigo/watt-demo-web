package cn.fuelteam.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cn.fuelteam.manager.UserManager;
import cn.fuelteam.user.dto.UserDto;

@RestController
public class UserController {

    @Autowired
    private UserManager userManager;

    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    @ResponseBody
    public UserDto getUser(@PathVariable("id") Long id) {
        return userManager.get(id);
    }
}
