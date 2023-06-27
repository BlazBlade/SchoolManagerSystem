package edu.wzbc.schoolmanagersystem.service;

import edu.wzbc.schoolmanagersystem.pojo.Student;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author -
* @description 针对表【tb_student】的数据库操作Service
* @createDate 2023-04-30 21:36:02
*/
public interface StudentService extends IService<Student> {

    Student selectStudentByUsernameAndPwd(String username, String password);

    Student selectStudentById(Long userId);
}
