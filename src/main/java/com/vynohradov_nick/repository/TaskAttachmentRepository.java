package com.vynohradov_nick.repository;


import com.vynohradov_nick.entity.TaskAttachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskAttachmentRepository extends JpaRepository<TaskAttachment, Long> {
}
