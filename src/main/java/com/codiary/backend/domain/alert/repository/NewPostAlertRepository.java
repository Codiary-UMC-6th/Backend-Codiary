package com.codiary.backend.domain.alert.repository;

import com.codiary.backend.domain.alert.entity.NewPostAlert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewPostAlertRepository extends JpaRepository<NewPostAlert, Long> {
}
