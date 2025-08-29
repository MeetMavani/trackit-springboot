package com.trackit.service;

import com.trackit.dto.TaskRequest;
import com.trackit.dto.TaskResponse;
import java.util.List;

public interface TaskService {
    TaskResponse create(TaskRequest request);
    TaskResponse getById(Long id);
    List<TaskResponse> listAll();
    List<TaskResponse> listByProject(Long projectId);
    TaskResponse update(Long id, TaskRequest request);
    void delete(Long id);
}
