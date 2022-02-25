package com.cydeo.implementation;

import com.cydeo.dto.BatchDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.InstructorLesson;
import com.cydeo.entity.Lesson;
import com.cydeo.entity.User;
import com.cydeo.enums.UserRole;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.BatchRepository;
import com.cydeo.repository.InstructorLessonRepository;
import com.cydeo.repository.LessonRepository;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    MapperUtil mapperUtil;
    UserRepository userRepository;
    LessonRepository lessonRepository;
    BatchRepository batchRepository;
    InstructorLessonRepository instructorLessonRepository;

    public UserServiceImpl(MapperUtil mapperUtil, UserRepository userRepository, LessonRepository lessonRepository,
                           BatchRepository batchRepository, InstructorLessonRepository instructorLessonRepository) {
        this.mapperUtil = mapperUtil;
        this.userRepository = userRepository;
        this.lessonRepository = lessonRepository;
        this.batchRepository = batchRepository;
        this.instructorLessonRepository = instructorLessonRepository;
    }

    @Override
    public List<UserDTO> listAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(obj -> mapperUtil.convert(obj, new UserDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> getAllStaffs() {
        return userRepository.findAllByUserRoleNot(UserRole.STUDENT)
                .stream()
                .map(obj -> mapperUtil.convert(obj, new UserDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> getAllStudents() {
        return userRepository.findAllByUserRole(UserRole.STUDENT)
                .stream()
                .map(obj -> mapperUtil.convert(obj, new UserDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public List<BatchDTO> getAllBatches() {
        return batchRepository.findAll()
                .stream()
                .map(obj -> mapperUtil.convert(obj, new BatchDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        return mapperUtil.convert(userRepository.findByEmail(email), new UserDTO());
    }

    @Override
    public UserDTO getUserById(Long id) {
        return mapperUtil.convert(userRepository.findById(id).get(), new UserDTO());
    }

    @Override
    public UserDTO save(UserDTO userDTO) {
        if (userDTO.getUserRole() == null) userDTO.setUserRole(UserRole.STUDENT);
        userDTO.setEnabled(true);
        User user = mapperUtil.convert(userDTO, new User());
        userRepository.save(user);
        return mapperUtil.convert(user, new UserDTO());
    }

    @Override
    public void delete(Long id) {
        User user = userRepository.findById(id).get();
        user.setIsDeleted(true);
        userRepository.save(user);
    }

    //    @Override
//    public Map<UserDTO, String> getCydeoMentorsAndGroupsMap() {
//        Map<UserDTO,String> cydeoMentorsGroupsMap = new HashMap<>();
//        List<UserDTO> cydeoMentors = listAllUsersByRole(UserRole.CYDEO_MENTOR);
//        for (UserDTO mentor : cydeoMentors) {
//            List<GroupDTO> groupDTO = groupService.listAllGroupsOfCydeoMentor(mentor.getEmail());
//            String groups = groupDTO
//                    .stream()
//                    .map(obj -> (obj.getBatch().getName() + " " + obj.getName())).reduce("",(x,y)-> x + y + " / ");
//            groups = (groups.equals("")) ? "-" : groups.substring(0,groups.length()-2);
//            cydeoMentorsGroupsMap.put(mentor,groups);
//        }
//        return cydeoMentorsGroupsMap;
//    }
//
//    @Override
//    public Map<UserDTO, String> getAlumniMentorsAndGroupsMap() {
//        Map<UserDTO,String> alumniMentorsGroupsMap = new HashMap<>();
//        List<UserDTO> alumniMentors = listAllUsersByRole(UserRole.ALUMNI_MENTOR);
//        for (UserDTO mentor : alumniMentors) {
//            List<GroupDTO> groupDTO = groupService.listAllGroupsOfAlumniMentor(mentor.getEmail());
//            String groups = groupDTO
//                    .stream()
//                    .map(obj -> (obj.getBatch().getName() + " " + obj.getName())).reduce("",(x,y)-> x + y + " / ");
//            groups = (groups.equals("")) ? "-" : groups.substring(0,groups.length()-2);
//            alumniMentorsGroupsMap.put(mentor,groups);
//        }
//        return alumniMentorsGroupsMap;
//    }

}
