package com.majian.springbootinit.controller.inner;

import com.majian.springbootinit.model.entity.User;
import com.majian.springbootinit.serice.UserFeignClient;
import com.majian.springbootinit.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
@RestController
@RequestMapping("/inner")
public class UserInnerController implements UserFeignClient {
    @Resource
    private UserService userService;
    @Override
    @GetMapping("/ger/{userId}")
    public User getById(@PathVariable("userId") Long userId){
        final User user = userService.getById(userId);
        return user;
    }
    @Override
    @GetMapping("/get/{ids}")
    public List<User> listByIds(@PathVariable("ids") Collection<Long> userIdSet){
        final List<User> users = userService.listByIds(userIdSet);
        return users;
    }
}
