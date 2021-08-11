package com.wangyue.serviceverificationcode.service.impl;

import com.wangyue.internalcommon.constant.CommonStatusEnum;
import com.wangyue.internalcommon.constant.IdentityConstant;
import com.wangyue.internalcommon.constant.RedisKeyPrefixConstant;
import com.wangyue.internalcommon.dto.ResponseResult;
import com.wangyue.internalcommon.dto.serviceverificationcode.response.VerifyCodeResponse;
import com.wangyue.serviceverificationcode.service.VerifyCodeService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * @Author: wangy
 * @Date: 2021/8/9 17:15
 */
@Service
public class VerifyCodeServiceImpl implements VerifyCodeService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public ResponseResult<VerifyCodeResponse> generate( int identity, String phoneNumber ) {
        // 0.9*9=8.1+1 9,去掉首位为0的情况。 0.11225478552211(0.0-<1)
        // String code = String.valueOf((int)((Math.random() * 9 + 1) * Math.pow(10,5))); 过时
        int code = ThreadLocalRandom.current().nextInt( 100000, 1000000 );
        VerifyCodeResponse verifyCodeResponse = new VerifyCodeResponse();
        verifyCodeResponse.setCode( code );

        //生成redis key
        String keyPre = generateKeyPreByIdentity( identity );
        String key = keyPre + phoneNumber;
        //存redis，2分钟过期
        BoundValueOperations codeRedis = redisTemplate.boundValueOps( key );

//        Boolean aBoolean = codeRedis.setIfAbsent(code);
//        if (aBoolean){
//            codeRedis.expire(2,TimeUnit.MINUTES);
//        }
        codeRedis.set( code, 2, TimeUnit.MINUTES );
//        codeRedis.expire(2,TimeUnit.MINUTES);


        return ResponseResult.success( verifyCodeResponse );
    }

    @Override
    public ResponseResult verify( int identity, String phoneNumber, String code ) {
        //三档验证


        //生成redis key
        String keyPre = generateKeyPreByIdentity( identity );
        String key = keyPre + phoneNumber;
        BoundValueOperations<String, String> codeRedis = redisTemplate.boundValueOps( key );
        String redisCode = codeRedis.get();

        if ( StringUtils.isNotBlank( code )
                && StringUtils.isNotBlank( redisCode )
                && code.trim().equals( redisCode.trim() ) ) {

            return ResponseResult.success( "" );
        } else {
            return ResponseResult.fail( CommonStatusEnum.VERIFY_CODE_ERROR.getCode(), CommonStatusEnum.VERIFY_CODE_ERROR.getValue() );
        }

    }


    /**
     * 判断此手机号发送时限限制
     *
     * @param phoneNumber
     * @return
     */
    private ResponseResult checkSendCodeTimeLimit( String phoneNumber ) {
        //判断是否有 限制1分钟，10分钟，24小时。

        return ResponseResult.success( "" );
    }

    /**
     * 三档验证校验
     *
     * @param phoneNumber
     * @param code
     * @return
     */
    private ResponseResult checkCodeThreeLimit( String phoneNumber, String code ) {
        //看流程图

        return ResponseResult.success( "" );
    }

    /**
     * 根据身份类型生成对应的缓存key
     *
     * @param identity
     * @return
     */
    private String generateKeyPreByIdentity( int identity ) {
        String keyPre = "";
        if ( identity == IdentityConstant.PASSENGER ) {
            keyPre = RedisKeyPrefixConstant.PASSENGER_LOGIN_CODE_KEY_PRE;
        } else if ( identity == IdentityConstant.DRIVER ) {
            keyPre = RedisKeyPrefixConstant.DRIVER_LOGIN_CODE_KEY_PRE;
        }
        return keyPre;
    }

    public static void main( String[] args ) {
        int sum = 10000000;
        long start1 = System.currentTimeMillis();
        for ( int i = 0; i < sum; i++ ) {
            String a = String.valueOf( (int) ( ( Math.random() * 9 + 1 ) * Math.pow( 10, 5 ) ) );
        }
        System.out.println( "字符串求乘方：" + ( System.currentTimeMillis() - start1 ) );

        long start2 = System.currentTimeMillis();
        for ( int i = 0; i < sum; i++ ) {
            ThreadLocalRandom.current().nextInt( 100000, 1000000 );
        }
        System.out.println( "感悟方案：" + ( System.currentTimeMillis() - start2 ) );
    }
}
