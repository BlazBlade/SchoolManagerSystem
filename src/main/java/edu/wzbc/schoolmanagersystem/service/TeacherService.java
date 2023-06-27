package edu.wzbc.schoolmanagersystem.service;

import edu.wzbc.schoolmanagersystem.pojo.Teacher;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author -
* @description 针对表【tb_teacher】的数据库操作Service
* @createDate 2023-04-30 21:36:04
*/
public interface TeacherService extends IService<Teacher> {

    Teacher selectTeacherByUsernameAndPwd(String username, String password);

    Teacher selectTeacherById(Long userId);
}
