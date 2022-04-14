package com.cydeo.implementation;

import com.cydeo.dto.BatchDTO;
import com.cydeo.dto.LessonDTO;
import com.cydeo.dto.TaskDTO;
import com.cydeo.entity.*;
import com.cydeo.enums.TaskStatus;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {


    @InjectMocks
    TaskServiceImpl taskService;

    @Mock
    BatchRepository batchRepository;
    @Mock
    TaskRepository taskRepository;
    @Mock
    MapperUtil mapperUtil;
    @Mock
    UserRoleRepository userRoleRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    StudentTaskRepository studentTaskRepository;
    @Mock
    LessonRepository lessonRepository;

    @Test
    void getAllTasksOfBatch() {
        Batch batch = new Batch();
        TaskDTO taskDTO = new TaskDTO();
        when(batchRepository.findById(anyLong())).thenReturn(Optional.of(batch));
        when(taskRepository.findAllByBatch(batch)).thenReturn(List.of(new Task(), new Task(), new Task()));
        when(mapperUtil.convert(any(), any())).thenReturn(taskDTO);
        taskService.getAllTasksOfBatch(anyLong());

        verify(batchRepository).findById(anyLong());
        verify(taskRepository).findAllByBatch(batch);
        verify(mapperUtil, times(3)).convert(any(), any());
        Assertions.assertNotNull(taskService.getAllTasksOfBatch(anyLong()));
    }

    @Test
    void getTaskById() {
        given(taskRepository.findById(anyLong())).willReturn(Optional.of(new Task()));
        given(mapperUtil.convert(any(), any())).willReturn(new TaskDTO());

        taskService.getTaskById(anyLong());

        then(taskRepository).should().findById(anyLong());
        then(mapperUtil).should().convert(any(), any());

        assertNotNull(taskService.getTaskById(anyLong()));
    }

    @Test
    void create() {
        TaskDTO taskDTO = new TaskDTO();
        Long batchId = 1L;
        Task task = new Task();
        taskDTO.setTaskStatus(TaskStatus.PLANNED);
        taskDTO.setBatch(new BatchDTO());
        taskDTO.getBatch().setId(batchId);
        when(mapperUtil.convert(any(TaskDTO.class), any(Task.class))).thenReturn(task);
        when(batchRepository.findById(batchId)).thenReturn(Optional.of(new Batch()));
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        when(mapperUtil.convert(any(Task.class), any(TaskDTO.class))).thenReturn(taskDTO);
        TaskDTO result = taskService.create(taskDTO, batchId);

        verify(mapperUtil).convert(any(TaskDTO.class), any(Task.class));
        verify(batchRepository).findById(batchId);
        verify(taskRepository).save(task);
        verify(mapperUtil).convert(any(Task.class), any(TaskDTO.class));

        assertNotNull(result);
        assertEquals(TaskStatus.PLANNED, result.getTaskStatus());
        assertEquals(batchId, result.getBatch().getId());
    }

    @Test
    void save() {
        TaskDTO taskDTO = new TaskDTO();
        Long taskId = 1L;
        Long batchId = 1L;
        Task task = new Task();
        task.setTaskStatus(TaskStatus.PUBLISHED);
        taskDTO.setId(taskId);
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(mapperUtil.convert(any(TaskDTO.class), any(Task.class))).thenReturn(task);
        when(batchRepository.findById(batchId)).thenReturn(Optional.of(new Batch()));
        task.setBatch(new Batch());
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        when(mapperUtil.convert(any(Task.class), any(TaskDTO.class))).thenReturn(taskDTO);

        TaskDTO result = taskService.save(taskDTO, taskId, batchId);

        verify(taskRepository).findById(taskId);
        verify(mapperUtil).convert(any(TaskDTO.class), any(Task.class));
        verify(batchRepository).findById(batchId);
        verify(taskRepository).save(any(Task.class));
        verify(mapperUtil).convert(any(Task.class), any(TaskDTO.class));

        assertNotNull(result);
        assertEquals(TaskStatus.PUBLISHED, result.getTaskStatus());


    }

    @Test
    void delete() {
        Long taskId = 1L;
        Task task = new Task();
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        task.setIsDeleted(true);
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        taskService.delete(taskId);

        verify(taskRepository).findById(taskId);
        verify(taskRepository).save(any(Task.class));

        assertTrue(task.getIsDeleted());
    }

    @Test
    void complete() {
        Long taskId = 1L;
        Task task = new Task();

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        task.setTaskStatus(TaskStatus.OUT_OF_TIME);
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        taskService.complete(taskId);

        verify(taskRepository).findById(taskId);
        verify(taskRepository).save(any(Task.class));

        assertEquals(TaskStatus.OUT_OF_TIME, task.getTaskStatus());
    }

    @Test
    void publish() {
        Long taskId = 1L;
        Task task = new Task();
        String userRoleName = "Student";
        UserRole userRole = new UserRole();
        Batch batch = new Batch();
        batch.setName("testing-batch");
        task.setBatch(batch);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        task.setTaskStatus(TaskStatus.PUBLISHED);
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        when(userRoleRepository.findByName(userRoleName)).thenReturn(userRole);
        User student = new User();
        student.setBatch(batch);
        when(userRepository.findAllByUserRoleAndBatch(userRole, task.getBatch())).thenReturn(List.of(new User()));
        StudentTask studentTask = new StudentTask(task.getName(), false, student, task);
        when(studentTaskRepository.save(any(StudentTask.class))).thenReturn(studentTask);

        taskService.publish(taskId);

        verify(taskRepository).findById(taskId);
        verify(taskRepository).save(any(Task.class));
        verify(userRoleRepository).findByName(userRoleName);
        verify(userRepository).findAllByUserRoleAndBatch(userRole, task.getBatch());
        verify(studentTaskRepository).save(any(StudentTask.class));

        assertFalse(studentTask.isCompleted());
        assertEquals(batch.getName(), studentTask.getStudent().getBatch().getName());

    }

    @Test
    void getBatchById() {
        Long batchId = 1L;
        Batch batch = new Batch();

        BatchDTO batchDTO = new BatchDTO();
        when(batchRepository.findById(batchId)).thenReturn(Optional.of(batch));
        when(mapperUtil.convert(any(Batch.class), any(BatchDTO.class))).thenReturn(batchDTO);

        BatchDTO result = taskService.getBatchById(batchId);

        verify(batchRepository).findById(batchId);
        verify(mapperUtil).convert(any(Batch.class), any(BatchDTO.class));

        assertNotNull(result);

    }

    @Test
    void getAllBatches() {
        List<Batch> batches = new ArrayList<>();
        Batch batch1 = new Batch();
        batches.add(batch1);
        Batch batch2 = new Batch();
        batches.add(batch2);

        when(batchRepository.findAll()).thenReturn(batches);
        when(mapperUtil.convert(any(Batch.class), any(BatchDTO.class))).thenReturn(new BatchDTO());

        List<BatchDTO> result = taskService.getAllBatches();

        verify(batchRepository).findAll();
        verify(mapperUtil, times(2)).convert(any(Batch.class), any(BatchDTO.class));

        assertNotNull(result);
    }

    @Test
    void getAllLessons() {

        Lesson lesson1 = new Lesson();
        Lesson lesson2 = new Lesson();
        List<Lesson> lessons = new ArrayList<>(List.of(lesson1, lesson2));

        when(lessonRepository.findAll()).thenReturn(lessons);
        when(mapperUtil.convert(any(Lesson.class), any(LessonDTO.class))).thenReturn(new LessonDTO());

        List<LessonDTO> result = taskService.getAllLessons();

        verify(lessonRepository).findAll();
        verify(mapperUtil, times(2)).convert(any(Lesson.class), any(LessonDTO.class));

        assertNotNull(result);
    }
}