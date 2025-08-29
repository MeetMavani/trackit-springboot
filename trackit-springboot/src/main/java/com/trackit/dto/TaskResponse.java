package com.trackit.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private String status;
    private Integer priority;
    private LocalDateTime dueDate;
    private Long projectId;
    private Long assigneeId;
}