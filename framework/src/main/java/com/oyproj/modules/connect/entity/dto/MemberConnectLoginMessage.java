package com.oyproj.modules.connect.entity.dto;

import com.oyproj.modules.connect.entity.Connect;
import com.oyproj.modules.mamber.entity.dos.Member;
import lombok.Data;

@Data
public class MemberConnectLoginMessage {
    private Member member;
    private ConnectAuthUser connectAuthUser;
}
