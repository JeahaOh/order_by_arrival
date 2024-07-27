package com.example.consumer.repository;

import com.example.consumer.domain.FailedIssueEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FailedIssueEventRepository extends JpaRepository<FailedIssueEvent, Long> {

}
