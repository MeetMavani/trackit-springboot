package com.trackit.dto;

import lombok.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskRequest {
    @NotBlank
    private String title;

    private String description;

    private Integer priority;

    private String status; // optional: "TODO", "IN_PROGRESS" ...

    private LocalDateTime dueDate;

    @NotNull
    private Long projectId;

    private Long assigneeId;
}