package ru.netology.testmode.data;
import lombok.Data;
import lombok.RequiredArgsConstructor;

public class DataArguments {
    @Data
    @RequiredArgsConstructor
    public class UserInfo {
        private final String login;
        private final String password;
        private final String status;
    }
}
