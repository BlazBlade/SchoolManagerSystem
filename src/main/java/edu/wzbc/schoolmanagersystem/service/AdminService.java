package edu.wzbc.schoolmanagersystem.service;

import edu.wzbc.schoolmanagersystem.pojo.Admin;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author -
* @description 针对表【tb_admin】的数据库操作Service
* @createDate 2023-04-30 21:35:20
*/
public interface AdminService extends IService<Admin> {

    Admin selectAdminByUsernameAndPwd(String username, String password);
    Admin selectAdminById(Long id);
}
