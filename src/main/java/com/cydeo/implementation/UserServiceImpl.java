package com.cydeo.implementation;

import com.cydeo.dto.UserDTO;
import com.cydeo.entity.InstructorLesson;
import com.cydeo.entity.Lesson;
import com.cydeo.entity.User;
import com.cydeo.enums.UserRole;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.InstructorLessonRepository;
import com.cydeo.repository.LessonRepository;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    MapperUtil mapperUtil;
    InstructorLessonRepository instructorLessonRepository;
    LessonRepository lessonRepository;

    public UserServiceImpl(UserRepository userRepository, MapperUtil mapperUtil,
                           InstructorLessonRepository instructorLessonRepository, LessonRepository lessonRepository) {
        this.userRepository = userRepository;
        this.mapperUtil = mapperUtil;
        this.instructorLessonRepository = instructorLessonRepository;
        this.lessonRepository = lessonRepository;
    }

    @Override
    public List<UserDTO> listAllUsers() {
        List<User> userList = userRepository.findAll();
        return userList.stream().map(obj -> mapperUtil.convert(obj, new UserDTO())).collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> listAllUsersByRole(UserRole userRole) {
        List<User> userList = userRepository.findAllByUserRole(userRole);
        return userList.stream().map(obj -> mapperUtil.convert(obj, new UserDTO())).collect(Collectors.toList());
    }

    @Override
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id).get();
        return mapperUtil.convert(user, new UserDTO());
    }

    @Override
    public UserDTO save(UserDTO userDTO) {
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

    @Override
    public List<UserDTO> listAllInstructorsOfLesson(Long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId).get();
        List<InstructorLesson> instructorLessonListByLesson = instructorLessonRepository.findAllByLesson(lesson);
        return instructorLessonListByLesson
                .stream()
                .map(obj -> mapperUtil.convert(obj.getInstructor(), new UserDTO()))
                .collect(Collectors.toList());
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
