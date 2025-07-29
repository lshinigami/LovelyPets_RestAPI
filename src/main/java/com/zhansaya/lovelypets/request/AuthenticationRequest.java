package com.zhansaya.lovelypets.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AuthenticationRequest {
    String phone;
    String password;

}
