package com.trackit.service.impl;

import com.trackit.dto.*;
import com.trackit.entity.*;
import com.trackit.exception.ResourceNotFoundException;
import com.trackit.repository.*;
import com.trackit.service.TaskService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepo;
    private final ProjectRepository projectRepo;
    private final UserRepository userRepo;

    public TaskServiceImpl(TaskRepository taskRepo, ProjectRepository projectRepo, UserRepository userRepo) {
        this.taskRepo = taskRepo;
        this.projectRepo = projectRepo;
        this.userRepo = userRepo;
    }

    @Override
    public TaskResponse create(TaskRequest req) {
        Project project = projectRepo.findById(req.getProjectId())
            .orElseThrow(() -> new ResourceNotFoundException("Project not found: " + req.getProjectId()));

        User assignee = null;
        if (req.getAssigneeId() != null) {
            assignee = userRepo.findById(req.getAssigneeId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + req.getAssigneeId()));
        }

        Task task = Task.builder()
                .title(req.getTitle())
                .description(req.getDescription())
                .priority(req.getPriority() == null ? 3 : req.getPriority())
                .dueDate(req.getDueDate())
                .project(project)
                .assignee(assignee)
                .build();

        if (req.getStatus() != null) {
            try { task.setStatus(TaskStatus.valueOf(req.getStatus())); }
            catch (IllegalArgumentException ignored) {}
        }

        Task saved = taskRepo.save(task);
        return toResponse(saved);
    }

    @Override
    public TaskResponse getById(Long id) {
        Task t = taskRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task not found: " + id));
        return toResponse(t);
    }

    @Override
    public List<TaskResponse> listAll() {
        return taskRepo.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<TaskResponse> listByProject(Long projectId) {
        return taskRepo.findByProjectId(projectId).stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public TaskResponse update(Long id, TaskRequest req) {
        Task t = taskRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task not found: " + id));
        if (req.getTitle() != null) t.setTitle(req.getTitle());
        if (req.getDescription() != null) t.setDescription(req.getDescription());
        if (req.getPriority() != null) t.setPriority(req.getPriority());
        if (req.getDueDate() != null) t.setDueDate(req.getDueDate());
        if (req.getStatus() != null) {
            try { t.setStatus(TaskStatus.valueOf(req.getStatus())); } catch (IllegalArgumentException ignored) {}
        }
        if (req.getAssigneeId() != null) {
            User u = userRepo.findById(req.getAssigneeId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found: " + req.getAssigneeId()));
            t.setAssignee(u);
        }
        Task saved = taskRepo.save(t);
        return toResponse(saved);
    }

    @Override
    public void delete(Long id) {
        if (!taskRepo.existsById(id)) throw new ResourceNotFoundException("Task not found: " + id);
        taskRepo.deleteById(id);
    }

    private TaskResponse toResponse(Task t) {
        return TaskResponse.builder()
                .id(t.getId())
                .title(t.getTitle())
                .description(t.getDescription())
                .status(t.getStatus() == null ? null : t.getStatus().name())
                .priority(t.getPriority())
                .dueDate(t.getDueDate())
                .projectId(t.getProject() == null ? null : t.getProject().getId())
                .assigneeId(t.getAssignee() == null ? null : t.getAssignee().getId())
                .build();
    }
}