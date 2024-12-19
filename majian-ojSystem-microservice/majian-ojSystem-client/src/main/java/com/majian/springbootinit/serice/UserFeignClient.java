package com.majian.springbootinit.serice;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.majian.springbootinit.common.ErrorCode;
import com.majian.springbootinit.exception.BusinessException;
import com.majian.springbootinit.model.dto.user.UserQueryRequest;
import com.majian.springbootinit.model.entity.User;
import com.majian.springbootinit.model.enums.UserRoleEnum;
import com.majian.springbootinit.model.vo.LoginUserVO;
import com.majian.springbootinit.model.vo.UserVO;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;

import static com.majian.springbootinit.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户服务
 *
 * @author ""程序员马建</a>
 * @from <a href="">自我开发项目</a>
 */
@FeignClient(value = "majian-ojSystem-user-service", path = "/api/user/inner")
public interface UserFeignClient {
    @GetMapping("/ger/{userId}")
    User getById(@PathVariable("userId") Long userId);
    @GetMapping("/get/{ids}")
    List<User> listByIds(@PathVariable("ids") Collection<Long> userIdSet);
    default User getLoginUser(HttpServletRequest request){
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        System.out.println("session1"+request.getSession().getId());
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return currentUser;
    }

    default boolean isAdmin(User user){
        return user != null && UserRoleEnum.ADMIN.getValue().equals(user.getUserRole());
    }


    default UserVO getUserVO(User user){
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

}
