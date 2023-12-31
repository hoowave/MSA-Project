package org.msa.authentication.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.msa.authentication.domain.Account;
import org.msa.authentication.dto.AccountDTO;
import org.msa.authentication.dto.ResponseDTO;
import org.msa.authentication.service.AccountService;
import org.msa.authentication.util.JWTUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "v1/account")
@Slf4j
@RequiredArgsConstructor
public class AuthenticationController {
    private final AccountService accountService;

    @RequestMapping(value = "/join", method = RequestMethod.POST)
    public ResponseEntity<ResponseDTO> join(@Valid @RequestBody AccountDTO accountDTO) throws Exception {
        ResponseDTO.ResponseDTOBuilder responseDTOBuilder = ResponseDTO.builder();
        Account account = accountService.selectAccount(accountDTO);

        if (account != null) {
            responseDTOBuilder.code("100").message("already join user.");
        } else {
            accountService.saveAccount(accountDTO, null);
            responseDTOBuilder.code("200").message("success");
        }

        log.debug("join.account.id = {}", accountDTO.getAccountId());
        return ResponseEntity.ok(responseDTOBuilder.build());
    }

    @RequestMapping(value = "/token", method = RequestMethod.POST)
    public ResponseEntity<ResponseDTO> token(@Valid @RequestBody AccountDTO accountDTO) throws Exception {
        ResponseDTO.ResponseDTOBuilder responseDTOBuilder = ResponseDTO.builder();
        Account account = accountService.selectAccount(accountDTO);

        if (account == null) {
            responseDTOBuilder.code("101").message("Unknown user.");
        } else {
            String token = getToken(accountDTO);
            accountService.saveAccount(accountDTO, token);
            responseDTOBuilder.code("200").message("success");
            responseDTOBuilder.token(token);
        }

        log.debug("token.account.id = {}", accountDTO.getAccountId());
        return ResponseEntity.ok(responseDTOBuilder.build());
    }

    private String getToken(AccountDTO accountDTO) {
        return JWTUtil.generate(accountDTO);
    }

}
