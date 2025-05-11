package com.studio.core.member.entity;

import com.studio.core.global.repository.TimeBaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "REFRESH_TOKEN")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RefreshTokenEntity extends TimeBaseEntity {

    @Id
    private Long memberNo;

    private String refreshToken;



//    public RefreshTokenEntity(RefreshToken refreshToken) {
//        this.memberNo = refreshToken.getMemberNo();
//        this.refreshToken = refreshToken.getRefreshToken();
//    }
//
//    public RefreshToken toAdminRefreshToken() {
//        return new RefreshToken(memberNo, refreshToken);
//    }

}
