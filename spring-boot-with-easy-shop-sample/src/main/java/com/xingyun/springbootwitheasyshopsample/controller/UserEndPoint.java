package com.xingyun.springbootwitheasyshopsample.controller;

import com.xingyun.springbootwitheasyshopsample.model.User;
import com.xingyun.springbootwitheasyshopsample.model.dto.UserDto;
import com.xingyun.springbootwitheasyshopsample.service.user.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 熟悉Spring MVC 的应该知道这个类其实是一个Controller类
 * Spring MVC 针对Controller 提供了两个注解,@Controller @RestController
 * 当在一个Controller类中添加了@RestController注解时,该类中所有使用了@RequstMapping的方法就会返回响应体(response body)
 * 如果使用的是@Controller注解，则会使将HTML部分的代码也一起返回给调用者。
 * 也可以在@Controller 注解的方法上加上@ResponseBody 来达到同样的目的
 * 值得注意的是,返回的对象如果是int,boolean 等基本类型可能会出错,最好对返回值进行包装。
 * @Api(value = "UserEndPoint",tags = "用户管理相关Api") 是Swagger 注解不是Spring MVC自带的
 * @author 星云
 * @功能
 * @date 9/22/2019 12:49 PM
 */
@Api(value = "UserEndPoint",tags = "用户管理相关Api")
@RequestMapping(value = "/users")
@RestController
public class UserEndPoint {

    @Autowired
    UserService userService;

    @ApiOperation(value = "获取用户分页数据",notes = "获取用户分页数据",httpMethod = "GET",tags = "用户管理相关Api")


    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "page",
                    value = "第几页,从0开始,默认为第0页",
                    dataType = "int",
                    paramType = "query"),
            @ApiImplicitParam(
                    name = "size",
                    value = "每一页记录数的大小,默认为20",
                    dataType = "int",
                    paramType = "query"),
            @ApiImplicitParam(
                    name = "sort",
                    value = "排序,格式为:property,property(,ASC|DESC)的方式组织",
                    dataType = "String",
                    paramType = "query"),
    })
    @GetMapping(value = "/")
    public List<UserDto> findAll(Pageable pageable){
        Page<User> pageUser=this.userService.getPage(pageable);

        if(null!=pageable){
            //转换成Dto对象
            return pageUser.getContent().stream().map((user) -> {
                return new UserDto(user);
            }).collect(Collectors.toList());
        }
        return Collections.EMPTY_LIST;
    }

    @GetMapping(value = "/{id}")
    @ApiOperation(value = "获取用户详情数据",notes = "获取用户详情数据",httpMethod = "GET",tags = "用户管理相关Api")
    @ApiImplicitParam(name = "id",value = "用户的主键",dataType = "int",paramType = "path")
    public UserDto detail(@PathVariable Long id){
        User user=this.userService.load(id);
        return (null!=user)?new UserDto(user):null;
    }


    @PostMapping(value = "/{id}")
    @ApiOperation(value = "更新用户详情数据",notes = "更新用户详情数据",httpMethod = "POST",tags = "用户管理相关Api")
    @ApiImplicitParam(name = "id",value = "用户的主键",dataType = "int",paramType = "path")
    public UserDto update(@PathVariable Long id,@RequestBody UserDto userDto){
       userDto.setId(id);
       User user=this.userService.save(userDto);
        return (null!=user)?new UserDto(user):null;
    }


    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "删除指定用户",notes = "删除指定用户",httpMethod = "DELETE",tags = "用户管理相关Api")
    @ApiImplicitParam(name = "id",value = "所要删除用户的主键",dataType = "int",paramType = "path")
    public boolean delete(@PathVariable Long id){
        this.userService.delete(id);
        return true;
    }
}