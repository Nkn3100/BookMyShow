package com.scaler.BookMyShow.controller.utils;

import com.scaler.BookMyShow.dto.UserSignUpRequestDTO;
import com.scaler.BookMyShow.dto.UserSignUpResponseDTO;
import com.scaler.BookMyShow.models.User;

public class UserControllerUtil {
    public static void validateUserSignUpRequestDTO(UserSignUpRequestDTO userSignUpRequestDTO){
        //validate login
        // if anything fails throw an exception
    }
    public static UserSignUpResponseDTO convertUserToUserResponseDTO(User user, UserSignUpResponseDTO responseDTO){
        responseDTO.setId(user.getId());
        responseDTO.setName(user.getName());
        responseDTO.setEmail(user.getEmail());
        responseDTO.setTickets(user.getTickets());
        return responseDTO;

    }
}
