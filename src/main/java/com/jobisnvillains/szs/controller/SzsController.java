package com.jobisnvillains.szs.controller;

import com.jobisnvillains.szs.domain.LoginInfo;
import com.jobisnvillains.szs.domain.Member;
import com.jobisnvillains.szs.domain.MemberIncomeInfo;
import com.jobisnvillains.szs.dto.UserLoginRequestDto;
import com.jobisnvillains.szs.dto.UserSignupRequestDto;
import com.jobisnvillains.szs.dto.common.BaseResponseDto;
import com.jobisnvillains.szs.dto.common.TokenResponseDto;
import com.jobisnvillains.szs.service.SzsService;
import com.jobisnvillains.szs.util.JWTUtil;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/szs")
public class SzsController {

    private final SzsService service;
    private final PasswordEncoder encoder;
    private final JWTUtil jwtUtil;

    @Autowired
    public SzsController(SzsService service, PasswordEncoder encoder, JWTUtil jwtUtil) {
        this.service = service;
        this.encoder = encoder;
        this.jwtUtil = jwtUtil;
    }

    /**
     *  회원가입 API
     *
     * @param userSignupRequestDto {@link UserSignupRequestDto}
     * @return {@link BaseResponseDto}
     */
    @PostMapping(value="/signup")
    @ResponseBody
    public BaseResponseDto signUp(@Valid @RequestBody UserSignupRequestDto userSignupRequestDto) throws Exception {

        // 사용자 정보 setting
        String userId = userSignupRequestDto.getUserId();
        String password = encoder.encode(userSignupRequestDto.getPassword());
        String name = userSignupRequestDto.getName();
        String regNo = encoder.encode(userSignupRequestDto.getRegNo());
        Member member = new Member(userId, password, name, regNo);

        return service.signUp(member);

    }

    /**
     * 로그인 API
     *
     * @param userLoginRequestDto {@link UserLoginRequestDto}
     */
    @PostMapping(value="/login")
    @ResponseBody
    public TokenResponseDto login(@Valid @RequestBody UserLoginRequestDto userLoginRequestDto) throws Exception {

        String userId = userLoginRequestDto.getUserId();
        String password = userLoginRequestDto.getPassword();

        LoginInfo loginInfo = LoginInfo.of(userId, password);

        return service.login(loginInfo);
    }


    /**
     *  스크래핑 API
     *
     */
    @PostMapping(value="/scrap")
    @ResponseBody
    public BaseResponseDto scrap(HttpServletRequest request) throws Exception {

        String authorization = request.getHeader("Authorization");

        if(authorization != null && !authorization.startsWith("Bearer ")){
            System.out.println("실패");
            throw new IllegalArgumentException();
        }

        String token = authorization.split(" ")[1];
        String userId = jwtUtil.getUserId(token);
        Optional<MemberIncomeInfo> result = Optional.ofNullable(service.scrap(userId));

        return result.isPresent()? new BaseResponseDto("success", "scrap success") : new BaseResponseDto("fail","scrap fail");

        // find로 스크래핑한 정보 저장한 사람 있는지 확인해서 있으면 성공,  없으면 실패
    }

    /**
     *  결정세액 조회 API
     *
     */
    @PostMapping(value="/refund")
    @ResponseBody
    public Map<String,String> refund(HttpServletRequest request) throws Exception {

        String authorization = request.getHeader("Authorization");

        if(authorization != null && !authorization.startsWith("Bearer ")){
            System.out.println("실패");
            throw new IllegalArgumentException();
        }

        String token = authorization.split(" ")[1];
        String userId = jwtUtil.getUserId(token);

        int determinedTax = service.refund(userId);

        DecimalFormat formatter = new DecimalFormat("#,###");
        String formattedTax = formatter.format(determinedTax);

        Map<String,String> response = new HashMap<>();
        response.put("결정세액", formattedTax);

        return response;

    }




}
