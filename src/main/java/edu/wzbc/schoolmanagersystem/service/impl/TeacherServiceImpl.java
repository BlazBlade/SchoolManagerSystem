package edu.wzbc.schoolmanagersystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.wzbc.schoolmanagersystem.pojo.Admin;
import edu.wzbc.schoolmanagersystem.pojo.Student;
import edu.wzbc.schoolmanagersystem.pojo.Teacher;
import edu.wzbc.schoolmanagersystem.service.TeacherService;
import edu.wzbc.schoolmanagersystem.mapper.TeacherMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author -
* @description 针对表【tb_teacher】的数据库操作Service实现
* @createDate 2023-04-30 21:36:04
*/
@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher>
    implements TeacherService{
    @Resource
    private TeacherMapper teacherMapper;

    @Override
    public Teacher selectTeacherByUsernameAndPwd(String username, String password) {
        return teacherMapper.selectOne(new LambdaQueryWrapper<Teacher>().eq(Teacher::getName,username).eq(Teacher::getPassword,password));
    }

    @Override
    public Teacher selectTeacherById(Long userId) {
        return teacherMapper.selectOne(new LambdaQueryWrapper<Teacher>().eq(Teacher::getId,userId));
    }
}




