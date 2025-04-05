package com.example.myapplication.response;

import com.example.myapplication.enums.Role;

import java.time.LocalDateTime;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private String name;
    private String email;
    private String phoneNumber;
    private Role role;
    private String profilePicture;
    private String status;
    private Date lastSeen;
}
