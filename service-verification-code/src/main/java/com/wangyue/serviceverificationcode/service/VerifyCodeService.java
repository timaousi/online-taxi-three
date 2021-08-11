package com.wangyue.serviceverificationcode.service;

import com.wangyue.internalcommon.dto.ResponseResult;
import com.wangyue.internalcommon.dto.serviceverificationcode.response.VerifyCodeResponse;

/**
 * @Author: wangy
 * @Date: 2021/8/9 17:11
 */
public interface VerifyCodeService {

    public ResponseResult<VerifyCodeResponse> generate( int identity, String phoneNumber );

    public ResponseResult verify(int identity,String phoneNumber,String code);

}
