package com.renato.redminetimer.repository;

import com.renato.redminetimer.entity.UserConfig;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserConfigRepository extends JpaRepository<UserConfig, Long> {

    Optional<UserConfig> findFirstByOrderByIdDesc();
}
