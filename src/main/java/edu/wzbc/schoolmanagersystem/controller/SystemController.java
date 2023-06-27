package edu.wzbc.schoolmanagersystem.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import edu.wzbc.schoolmanagersystem.pojo.Admin;
import edu.wzbc.schoolmanagersystem.pojo.LoginForm;
import edu.wzbc.schoolmanagersystem.pojo.Student;
import edu.wzbc.schoolmanagersystem.pojo.Teacher;
import edu.wzbc.schoolmanagersystem.service.AdminService;
import edu.wzbc.schoolmanagersystem.service.StudentService;
import edu.wzbc.schoolmanagersystem.service.TeacherService;
import edu.wzbc.schoolmanagersystem.utils.*;
import io.jsonwebtoken.Jwt;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/sms/system")
public class SystemController {

    @Resource
    private AdminService adminService;

    @Resource
    private TeacherService teacherService;

    @Resource
    private StudentService studentService;

    /**
     * 获取验证码信息返回给浏览器
     * */
    @RequestMapping("/getVerifiCodeImage")
    public void getVerifiCodeImage(HttpSession session, HttpServletResponse response) throws IOException {
        //获取雁阵够吗图片
        BufferedImage verifiCodeImage= CreateVerifiCodeImage.getVerifiCodeImage();//get图片方法
        //获取验证码图片值
        String code = new String(CreateVerifiCodeImage.getVerifiCode());
        //验证码值保存到ession 校验验证码
        session.setAttribute("code",code);
        //验证码图片返回浏览器
        ImageIO.write(verifiCodeImage,"JPG",response.getOutputStream());//返回输出流
    }

    /** 登录功能 验证码校验 + 用户名密码检验
     *  pass 传token 给 浏览器
     */
    @ApiOperation("登录功能 登陆成功将查询到的用户信息、用户类型 并封装id和userType成token 一起响应到浏览器")
    @PostMapping("/login")
    public Result<Object> login(@ApiParam("封装到实体类中请求体的json数据") @RequestBody LoginForm loginForm, HttpSession session) {
        //校验验证码:
        String userInputCode = loginForm.getVerifiCode();
        //获取session中存放的验证码中的值
        String code = session.getAttribute("code").toString();
        //判断session中的验证码的值是否还在 若时间太长 会失效
        if (code == null || "".equals(code)) {
            return Result.fail().message("验证码失效,请重新输入");
        }
        //判断用户输入验证码与实际验证码的值是否相等
        if (!userInputCode.equalsIgnoreCase(code)) {
            return Result.fail().message("验证码输入有误");
        }
        //验证码输入正确后 将session中的验证码销毁
        session.removeAttribute("code");
        //获取用户类型 根据不同的用户判断输入的账号 密码 是否正确
        Integer userType = loginForm.getUserType();
        Map<String, Object> map = new LinkedHashMap<>();
        if (userType == 1) {
            Admin admin = adminService.selectAdminByUsernameAndPwd(loginForm.getUsername(), MD5.encrypt(loginForm.getPassword()));
            //若查询结果为null 则数据库中查无相应的账号和密码 登录失败
            if (admin == null) {
                return Result.fail().message("账号或密码有误");
            }
            //登录成功 将用户id与用户类型 封装为一个 token 响应到浏览器
            //让浏览器通过token再发送请求来进行解析 告诉前端应该前往哪个用户以及哪个类型用户的首页
            String token = JwtHelper.createToken(admin.getId().longValue(), userType);
            map.put("token", token);
        } else if (userType == 2) {
            Student student = studentService.selectStudentByUsernameAndPwd(loginForm.getUsername(), MD5.encrypt(loginForm.getPassword()));
            if (student == null) {
                return Result.fail().message("账号或密码有误");
            }
            String token = JwtHelper.createToken(student.getId().longValue(), userType);
            map.put("token", token);
        } else {
            Teacher teacher = teacherService.selectTeacherByUsernameAndPwd(loginForm.getUsername(), MD5.encrypt(loginForm.getPassword()));
            if (teacher == null) {
                return Result.fail().message("账号或密码有误");
            }
            String token = JwtHelper.createToken(teacher.getId().longValue(), userType);
            map.put("token", token);
        }
        return Result.ok(map);
    }
//    @PostMapping("/login")
//    public Result<Object> login(@RequestBody LoginForm loginForm,HttpSession session){
//        //获取验证码
//        String code = (String) session.getAttribute("code");
//        //判断是否已经失效
//        if(code == null || "".equals(code)){
//            return Result.fail().message("验证码失效，重新输入");
//        }
//        //获取input验证码
//        String userInputCode = loginForm.getVerifiCode();
//        if (userInputCode.equalsIgnoreCase(code)) {
//            return Result.fail().message("验证码输入错误");
//        }
//        //clear验证码值
//        session.removeAttribute("code");//session类中
//
//        //获取用户类型 根据用户类型返回进行判断 用户名和密码检验
//        Integer userType = loginForm.getUserType();
//        String username = loginForm.getUsername();
//        String password = MD5.encrypt(loginForm.getPassword());//MD5加密
//        Map<String,Object> map = new LinkedHashMap<>();
//        if(userType == 1){//判断用户名和密码  1:admin 2:stu 3:teacher
//            Admin admin = adminService.selectAdminByUsernameAndPwd(username,password);
//            //判断是否不存在
//            if(admin != null){
//                //根据用户id和类型生成token return ->浏览器
//                String token = JwtHelper.createToken(admin.getId().longValue(),userType);
//                map.put("token",token);
//                map.put("userType",userType);
//                return Result.ok(map);
//            }
//            return Result.fail().message("用户名或密码有误");
//        }else if (userType == 2){
//            Student student = studentService.selectStudentByUsernameAndPwd(username,password);
//            //判断是否不存在
//            if(student != null){
//                //根据用户id和类型生成token return ->浏览器
//                String token = JwtHelper.createToken(student.getId().longValue(),userType);
//                map.put("token",token);
//                return Result.ok(map);
//            }
//            return Result.fail().message("用户名或密码有误");
//        }else {
//            Teacher teacher = teacherService.selectTeacherByUsernameAndPwd(username,password);
//            //判断是否不存在
//            if(teacher != null){
//                //根据用户id和类型生成token return ->浏览器
//                String token = JwtHelper.createToken(teacher .getId().longValue(),userType);
//                map.put("token",token);
//                return Result.ok(map);
//            }
//            return Result.fail().message("用户名或密码有误");
//        }
//    }

    @ApiOperation("将请求头中的token解析成id和用户类型 并根据id和类型查询用户信息 将信息和用户类型数据一起返回")
    @RequestMapping("/getInfo")
    public Result<Object> getInfo(@ApiParam("请求头中的token数据")@RequestHeader("token") String token){

        //判断token是否有效
        if(JwtHelper.isExpiration(token)) {//有效返回false，异常返回true
            return Result.build(null, ResultCodeEnum.TOKEN_ERROR);
        }

        //解析token获取id和类型
        Long userId = JwtHelper.getUserId(token);
        Integer userType = JwtHelper.getUserType(token);
        Map<String,Object> map = new LinkedHashMap<>();
        map.put("userType",userType);
        if(userType == 1){
            Admin admin = adminService.selectAdminById(userId);
            map.put("user",admin);
        }else if(userType == 2){
            Student student = studentService.selectStudentById(userId);
            map.put("user",student);
        }else{
            Teacher teacher = teacherService.selectTeacherById(userId);
            map.put("user",teacher);
        }

        return Result.ok(map);
    }

    @ApiOperation("上传头像")
    @PostMapping("/headerImgUpload")
    public Result<Object> headerImgUpload(
            @ApiParam("封装请求体中的图片二进制数据")
            @RequestPart("multipartFile") MultipartFile multipartFile) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename();
        assert originalFilename != null;
        //获取文件后缀
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String photoName = UUID.randomUUID().toString().replace("-", "").toLowerCase().concat(suffix);
        String savePath = "E:/JavaProjects/smart_campus-master/smart_campus-master/SchoolManagerSystem/src/main/resources/static/upload/".concat(photoName);
        //保存图片
        multipartFile.transferTo(new File(savePath));
        return Result.ok("upload/".concat(photoName));
    }

    @ApiOperation("修改用户密码功能")
    @PostMapping("updatePwd/{oldPwd}/{newPwd}")
    public Result<Object> updatePwd(@ApiParam("请求头中的token数据") @RequestHeader("token") String token,
                                    @ApiParam("路径参数中的原密码") @PathVariable("oldPwd") String oldPwd,
                                    @ApiParam("路径参数中的新密码") @PathVariable("newPwd") String newPwd){
        //先判断一下token是否过期
        if (JwtHelper.isExpiration(token)) {
            return Result.fail().message("token失效,请重新登录后修改");
        }
        //获取用户id
        Long userId = JwtHelper.getUserId(token);
        //根据token 判断用户类型
        Integer userType = JwtHelper.getUserType(token);
        assert userType != null;
        if(userType == 1){
            //对旧密码进行校验
            Admin admin = adminService.selectAdminById(userId);
            if(!MD5.encrypt(oldPwd).equals(admin.getPassword())){
                return Result.fail().message("原密码输入有误");
            }
            //原密码输入正确 就将新密码进行加密后进行修改
            admin.setPassword(MD5.encrypt(newPwd));
            adminService.update(admin,new LambdaQueryWrapper<Admin>().eq(Admin::getId,userId));
        }else if(userType == 2){
            Student student = studentService.selectStudentById(userId);
            if(!MD5.encrypt(oldPwd).equals(student.getPassword())){
                return Result.fail().message("原密码输入有误");
            }
            student.setPassword(MD5.encrypt(newPwd));
            studentService.update(student,new LambdaQueryWrapper<Student>().eq(Student::getId,userId));
        }else {
            Teacher teacher = teacherService.selectTeacherById(userId);
            if(!MD5.encrypt(oldPwd).equals(teacher.getPassword())){
                return Result.fail().message("原密码输入有误");
            }
            teacher.setPassword(MD5.encrypt(newPwd));
            teacherService.update(teacher,new LambdaQueryWrapper<Teacher>().eq(Teacher::getId,userId));
        }
        return Result.ok();
    }
}
