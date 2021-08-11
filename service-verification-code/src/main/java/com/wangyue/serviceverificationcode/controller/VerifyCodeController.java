package com.wangyue.serviceverificationcode.controller;

import com.wangyue.internalcommon.dto.ResponseResult;
import com.wangyue.serviceverificationcode.service.VerifyCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 获取短信验证码
 *
 * @Author: wangy
 * @Date: 2021/8/9 16:48
 */
@RestController
@RequestMapping( "/verify-code" )
public class VerifyCodeController {

    @Autowired
    private VerifyCodeService verifyCodeService;

    @GetMapping( "/generate/{identity}/{phoneNumber}" )
    public ResponseResult generate( @PathVariable( "identity" ) int identity, @PathVariable( "phoneNumber" ) String phoneNumber ) {
        return verifyCodeService.generate( identity, phoneNumber );
    }
}
