package com.vynohradov_nick.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "task_attachments")
@Getter
@Setter
public class TaskAttachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;

    @Lob
    private byte[] data;

    @ManyToOne
    @JoinColumn(name = "task_id")

    private Task task;
    public TaskAttachment() {}


    public TaskAttachment(String fileName, byte[] data, Task task) {
        this.fileName = fileName;
        this.data = data;
        this.task = task;
    }
}
