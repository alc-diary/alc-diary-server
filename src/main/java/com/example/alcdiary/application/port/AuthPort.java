package com.example.alcdiary.application.port;

import com.example.alcdiary.application.command.LoginCommand;
import com.example.alcdiary.domain.model.UserModel;

public interface AuthPort {

    AuthPort service(LoginCommand.Service service);
    AuthPort token(String token);
    UserModel authentication();
}
