package com.zhansaya.lovelypets.request;

import com.zhansaya.lovelypets.domain.enums.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RegisterRequest {
    String firstname;
    String lastname;
    String email;
    String phone;
    String password;
    Role role;
}
