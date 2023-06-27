package edu.wzbc.schoolmanagersystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.wzbc.schoolmanagersystem.mapper.AdminMapper;
import edu.wzbc.schoolmanagersystem.pojo.Admin;
import edu.wzbc.schoolmanagersystem.pojo.Student;
import edu.wzbc.schoolmanagersystem.service.StudentService;
import edu.wzbc.schoolmanagersystem.mapper.StudentMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author -
* @description 针对表【tb_student】的数据库操作Service实现
* @createDate 2023-04-30 21:36:02
*/
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student>
    implements StudentService{
    @Resource
    private StudentMapper studentMapper;

    @Override
    public Student selectStudentByUsernameAndPwd(String username, String password) {
        return studentMapper.selectOne(new LambdaQueryWrapper<Student>().eq(Student::getName,username).eq(Student::getPassword,password));//判断用户名密码
    }

    @Override
    public Student selectStudentById(Long userId) {
        return studentMapper.selectOne(new LambdaQueryWrapper<Student>().eq(Student::getId,userId));//判断id
    }
}




