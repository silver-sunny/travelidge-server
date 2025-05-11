package com.studio.core.report.repository;

import com.studio.core.report.entity.ReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportJpaRepository extends JpaRepository<ReportEntity, Long> {

}
