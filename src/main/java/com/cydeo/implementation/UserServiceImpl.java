package com.cydeo.implementation;

import com.cydeo.dto.GroupDTO;
import com.cydeo.dto.LessonDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.User;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.GroupService;
import com.cydeo.service.LessonService;
import com.cydeo.service.UserService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    MapperUtil mapperUtil;
    LessonService lessonService;
    GroupService groupService;

    public UserServiceImpl(UserRepository userRepository, MapperUtil mapperUtil, LessonService lessonService, GroupService groupService) {
        this.userRepository = userRepository;
        this.mapperUtil = mapperUtil;
        this.lessonService = lessonService;
        this.groupService = groupService;
    }

    @Override
    public List<UserDTO> listAllUsersByRole(String role) {
        List<User> userList = userRepository.findAllByRoleDescriptionIgnoreCase(role);
        return userList.stream().map(obj -> mapperUtil.convert(obj, new UserDTO())).collect(Collectors.toList());
    }

    @Override
    public Map<UserDTO, String> getInstructorsAndLessonsMap() {
        Map<UserDTO,String> instructorsLessonsMap = new HashMap<>();
        List<UserDTO> instructors = listAllUsersByRole("Instructor");
        for (UserDTO instructor : instructors) {
            List<LessonDTO> lessonsDTO = lessonService.listAllLessonsOfInstructor(instructor.getEmail());
            String lessons = lessonsDTO.stream().map(obj -> obj.getName()).reduce("",(x,y)-> x + y + " / ");
            lessons = lessons.substring(0,lessons.length()-2);
            instructorsLessonsMap.put(instructor,lessons);
        }
        return instructorsLessonsMap;
    }

    @Override
    public Map<UserDTO, String> getCybertekMentorsAndGroupsMap() {
        Map<UserDTO,String> cybertekMentorsGroupsMap = new HashMap<>();
        List<UserDTO> cybertekMentors = listAllUsersByRole("CybertekMentor");
        for (UserDTO mentor : cybertekMentors) {
            List<GroupDTO> groupDTO = groupService.listAllGroupsOfCybertekMentor(mentor.getEmail());
            String groups = groupDTO.stream().map(obj -> (obj.getBatch().getName() + " " + obj.getName())).reduce("",(x,y)-> x + y + " / ");
            groups = groups.substring(0,groups.length()-2);
            cybertekMentorsGroupsMap.put(mentor,groups);
        }
        return cybertekMentorsGroupsMap;
    }

    @Override
    public Map<UserDTO, String> getAlumniMentorsAndGroupsMap() {
        Map<UserDTO,String> alumniMentorsGroupsMap = new HashMap<>();
        List<UserDTO> alumniMentors = listAllUsersByRole("AlumniMentor");
        for (UserDTO mentor : alumniMentors) {
            List<GroupDTO> groupDTO = groupService.listAllGroupsOfAlumniMentor(mentor.getEmail());
            String groups = groupDTO.stream().map(obj -> (obj.getBatch().getName() + " " + obj.getName())).reduce("",(x,y)-> x + y + " / ");
            groups = groups.substring(0,groups.length()-2);
            alumniMentorsGroupsMap.put(mentor,groups);
        }
        return alumniMentorsGroupsMap;
    }

}
