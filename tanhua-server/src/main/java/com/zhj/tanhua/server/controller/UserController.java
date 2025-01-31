package com.zhj.tanhua.server.controller;

import com.zhj.tanhua.common.vo.ResponseResult;
import com.zhj.tanhua.server.service.UserService;
import com.zhj.tanhua.user.dto.UserInfoDto;
import com.zhj.tanhua.user.dto.UserDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author huanjie.zhuang
 * @date 2021/6/13
 */
@Api(tags = "用户")
@RequestMapping("tanhua/user")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    private static final String AUTHORIZATION = "authorization";

    /**
     * 用户登录
     *
     * @param phone 用户手机号
     * @param checkCode  验证码
     * @return ResponseResult<UserDto>
     */
    @ApiOperation("用户登录")
    @GetMapping("login")
    public ResponseResult<UserDto> login(@RequestParam("phone") String phone,
                                         @RequestParam("checkCode") String checkCode){
        UserDto userDto;
        try {
            userDto = userService.login(phone, checkCode);
        } catch (Exception e) {
            return ResponseResult.fail(e.getMessage());
        }
        return ResponseResult.ok(userDto);
    }

    /**
     * 发送验证码
     *
     * @param phone 用户手机号
     * @return ResponseResult<UserDto>
     */
    @ApiOperation("发送验证码")
    @GetMapping("sentCheckCode")
    public ResponseResult<UserDto> sentCheckCode(@RequestParam("phone") String phone) {

        UserDto userDto;
        try {
            userDto = userService.sentCheckCode(phone);
        } catch (Exception e) {
            return ResponseResult.fail(e.getMessage());
        }
        return ResponseResult.ok(userDto);
    }

    /**
     * 根据token查询用户数据
     *
     * @param  token 用户token
     * @return ResponseResult<UserDto>
     */
    @ApiOperation("根据token查询用户")
    @GetMapping("token/{token}")
    public ResponseResult<UserDto> getUserByToken(@PathVariable("token") String token) {

        UserDto user = userService.getUserByToken(token);
        if (null == user) {
            return ResponseResult.fail("当前登录用户token已失效，请重新登录");
        }

        return ResponseResult.ok(user);
    }

    /**
     * 完善个人信息
     *
     * @param token 用户token
     * @param userInfoDto 用户信息
     * @return ResponseResult<Object>
     */
    @ApiOperation("保存用户信息")
    @PostMapping("saveInfo")
    public ResponseResult<Object> saveUserInfo(@RequestHeader(AUTHORIZATION) String token,
                                               @RequestBody UserInfoDto userInfoDto) {
        try {
            userService.saveUserInfo(token, userInfoDto);
        } catch (Exception e) {
            return ResponseResult.fail(e.getMessage());
        }

        return ResponseResult.ok();
    }

    /**
     * 上传头像
     *
     * @param token 用户token
     * @param file 用户头像图片文件
     * @return ResponseResult<Object>
     */
    @ApiOperation("保存用户头像")
    @PostMapping("saveAvatar")
    public ResponseResult<Object> saveAvatar(@RequestHeader(AUTHORIZATION) String token,
                                             @RequestParam("avatar") MultipartFile file) {
        try {
            userService.saveAvatar(token, file);
        } catch (Exception e) {
            return ResponseResult.fail(e.getMessage());
        }

        return ResponseResult.ok();
    }

    /**
     * 获取用户详细信息
     *
     * @param token 用户token
     * @return ResponseResult<UserInfoDto>
     */
    @ApiOperation("获取用户详细信息")
    @GetMapping("info")
    public ResponseResult<UserInfoDto> getUserInfo(@RequestHeader(AUTHORIZATION) String token) {

        UserDto user = userService.getUserByToken(token);
        if (null == user) {
            return ResponseResult.fail("当前登录用户token已失效，请重新登录");
        }

        return ResponseResult.ok(userService.getUserInfo(user.getId()));
    }
}
