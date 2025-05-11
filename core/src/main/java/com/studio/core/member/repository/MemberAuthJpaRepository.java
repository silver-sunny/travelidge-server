package com.studio.core.member.repository;

import com.studio.core.global.enums.ProviderType;
import com.studio.core.member.entity.MemberAuthEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberAuthJpaRepository extends JpaRepository<MemberAuthEntity, Long> {

    Optional<MemberAuthEntity> findByMemberNo(Long memberId);

    boolean existsByNickname(String nickname);

    Optional<MemberAuthEntity> findByProviderIdAndProviderType(String providerId,
        ProviderType providerType);

    @Query("SELECT COUNT(a) > 0 FROM MemberAuthEntity a WHERE a.id = :id")
    boolean existsById(String id);

    @Query("SELECT a FROM MemberAuthEntity a WHERE a.id = :id")
    Optional<MemberAuthEntity> findById(String id);

}
