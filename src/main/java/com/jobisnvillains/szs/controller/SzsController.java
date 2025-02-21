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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/szs")
@Tag(name = "API", description = "사용자 환급액 계산을 위한 API")
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
    @Operation(summary = "1. 회원가입 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "success", description = "회원가입에 성공하였습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "fail", description = "회원가입에 실패하였습니다.", content = @Content(mediaType = "application/json")),
    })
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
     * @return {@link TokenResponseDto}
     */
    @PostMapping(value="/login")
    @ResponseBody
    @Operation(summary = "2. 로그인 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "success", description = "로그인에 성공하였습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "fail", description = "로그인에 실패하였습니다.", content = @Content(mediaType = "application/json")),
    })
    public TokenResponseDto login(@Valid @RequestBody UserLoginRequestDto userLoginRequestDto) throws Exception {

        String userId = userLoginRequestDto.getUserId();
        String password = userLoginRequestDto.getPassword();

        LoginInfo loginInfo = LoginInfo.of(userId, password);

        String token = service.login(loginInfo);

        if(token != null) {
            return new TokenResponseDto("Success", token);
        } else {
            return new TokenResponseDto("fail", null);
        }

    }


    /**
     *  스크래핑 API
     *
     * @param request HttpServletRequest
     * @return {@link BaseResponseDto}
     */
    @PostMapping(value="/scrap")
    @ResponseBody
    @Operation(summary = "3. 스크래핑 API")
    public BaseResponseDto scrap(HttpServletRequest request) throws Exception {

        String token = getToken(request);

        String userId = jwtUtil.getUserId(token);
        Optional<MemberIncomeInfo> result = Optional.ofNullable(service.scrap(userId));

        return result.isPresent()? new BaseResponseDto("success", "scrap success") : new BaseResponseDto("fail","scrap fail");

    }

    /**
     *  결정세액 조회 API
     *
     * @param request HttpServletRequest
     * @return Map<String,String>
     */
    @PostMapping(value="/refund")
    @ResponseBody
    @Operation(summary = "4. 결정세액 조회 API")
    public Map<String,String> refund(HttpServletRequest request) {

        String token = getToken(request);
        String userId = jwtUtil.getUserId(token);
        Integer determinedTax = service.refund(userId);
        String formatDeterminedTax = makePriceFormat(determinedTax);

        Map<String,String> response = new HashMap<>();
        response.put("결정세액", formatDeterminedTax);

        return response;

    }


    /**
     *  금액 Fomatter
     *
     * @param price int
     * @return String
     */
    private String makePriceFormat(int price) {
        return new DecimalFormat("#,###").format(price);
    }


    /**
     *  토큰 추출
     *
     * @param request HttpServletRequest
     * @return String
     */
    private String getToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        String token = authorization.split(" ")[1];
        return token;
    }


}
