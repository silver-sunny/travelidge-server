package com.studio.core.member.entity;


import com.studio.core.global.enums.AuthRole;
import com.studio.core.global.enums.ProviderType;
import com.studio.core.global.repository.TimeBaseEntity;
import com.studio.core.member.dto.admin.UpdateUserRequestDto;
import com.studio.core.product.entity.ProductEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicUpdate;

import java.util.List;

@Entity
@Table(
    name = "MEMBER_AUTH",
    uniqueConstraints = @UniqueConstraint(columnNames = {"providerId", "providerType"})
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@DynamicUpdate
@Builder
public class MemberAuthEntity extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("멤버 번호")
    private Long memberNo;

    @Column(unique = true)
    @Comment("아이디")
    private String id;

    @Comment("비밀번호")
    private String password;

    @Enumerated(EnumType.STRING)
    @Comment("권한")
    private AuthRole role;

    @Comment("소셜 아이디")
    private String providerId;

    @Enumerated(EnumType.STRING)
    @Comment("소셜 타입")
    private ProviderType providerType;

    @Comment("핸드폰번호")
    private String phoneNumber;

    @Comment("닉네임")
    private String nickname;

    @Comment("이름")
    private String name;

    @Comment("이메일")
    private String email;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_no")
    private List<ProductEntity> products;

    public void updatePassword(String encryptPassword) {
        this.password = encryptPassword;
    }

    public void updateUser(UpdateUserRequestDto req) {
        if (req.name() != null && !req.name().isBlank()) {
            this.name = req.name();
        }
        if (req.nickname() != null && !req.nickname().isBlank()) {
            this.nickname = req.nickname();
        }
        if (req.phone() != null && !req.phone().isBlank()) {
            this.phoneNumber = req.phone();
        }
    }
}
