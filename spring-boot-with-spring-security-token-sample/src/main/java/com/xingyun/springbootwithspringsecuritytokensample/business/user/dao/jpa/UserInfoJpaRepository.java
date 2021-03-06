package com.xingyun.springbootwithspringsecuritytokensample.business.user.dao.jpa;

import com.xingyun.springbootwithspringsecuritytokensample.business.user.model.UserInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author qing-feng.zhao
 */
public interface UserInfoJpaRepository extends JpaRepository<UserInfoEntity,Long> {

    /**
     * 查找用户信息
     * @param userInfoToken
     * @return
     */
    Optional<UserInfoEntity> findByUserInfoToken(String userInfoToken);
}
