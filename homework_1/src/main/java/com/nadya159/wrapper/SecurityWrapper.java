package com.nadya159.wrapper;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SecurityWrapper {
    private String login;
    private String password;
}
