package edu.wzbc.schoolmanagersystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.wzbc.schoolmanagersystem.pojo.Admin;
import edu.wzbc.schoolmanagersystem.pojo.Student;
import edu.wzbc.schoolmanagersystem.service.AdminService;
import edu.wzbc.schoolmanagersystem.mapper.AdminMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author -
* @description 针对表【tb_admin】的数据库操作Service实现
* @createDate 2023-04-30 21:35:20
*/
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin>
    implements AdminService{
    @Resource
    private AdminMapper adminMapper;


    @Override
    public Admin selectAdminByUsernameAndPwd(String username, String password) {
        return adminMapper.selectOne(new LambdaQueryWrapper<Admin>().eq(Admin::getName,username).eq(Admin::getPassword,password));
    }

    @Override
    public Admin selectAdminById(Long userId) {
        return adminMapper.selectOne(new LambdaQueryWrapper<Admin>().eq(Admin::getId,userId));
    }
}




