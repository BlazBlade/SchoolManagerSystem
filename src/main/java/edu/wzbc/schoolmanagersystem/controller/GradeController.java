package edu.wzbc.schoolmanagersystem.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.core.util.StrUtil;
import edu.wzbc.schoolmanagersystem.pojo.Grade;
import edu.wzbc.schoolmanagersystem.service.GradeService;
import edu.wzbc.schoolmanagersystem.utils.Result;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/sms/gradeController")
public class GradeController {

    //实现带条件的分页查询功能
    @Resource
    private GradeService gradeService;

    /**
     * 分页查询
     * @param pn 当前页面
     * @param pageSize  每页显示数量
     * @param gradeName 年级 模糊查询条件
     * @return 返回查询结果page对象
     * */
    @ApiOperation("带条件的分页查询")
    @GetMapping("/getGrades/{pn}/{pageSize}")//页码和每页记录数
    public Result<Object> getGrades(
            @ApiParam("当前页码") @PathVariable("pn")Integer pn,
            @ApiParam("每页记录")@PathVariable("pageSize")Integer pageSize,
            @ApiParam("每页查询的条件")String gradeName){
                Page<Grade> page = gradeService.page(new Page<>(pn,pageSize),new LambdaQueryWrapper<Grade>().
                like(StrUtil.isNotBlank(gradeName),Grade::getName,gradeName).orderByDesc(Grade::getId));//结果按照id大小排,模糊查询

        return Result.ok(page); //return查询的封装对象
    }
    //记录删除功能
    @ApiOperation("单/批量删除功能")
    @DeleteMapping("/deleteGrade")
    public Result<Object> deleGrade(@ApiParam("请求中年级id集合")@RequestBody List<Integer> ids){//@param ids id集合
        if(ids.size() == 1){
            //单语句删除
            gradeService.removeById(ids.get(0));
        }else{
            //批量删除
            gradeService.removeBatchByIds(ids);
        }
        return Result.ok();
    }
    /**
     * 判断是否有id添加和修改
     * @param grade 封装json数据包到实体类
     * @return 返回成功数据*/
    @ApiOperation("添加或修改")
    @PostMapping("/saveOrUpdateGrade")
    public Result<Object> saveOrUpdateGrade(@ApiParam("封装请求Json到grade中")@RequestBody Grade grade){

        //判断请求中是否有id
        Integer id = grade.getId();
        if (id != null){
            gradeService.update(grade, new LambdaQueryWrapper<Grade>().eq(Grade::getId,id));
        }else {
            gradeService.save(grade);
        }
        return Result.ok();
    }

    @ApiOperation("获取所有年级的JSON")
    @GetMapping("/getGrades")
    public Result<Object> getGrades(){
        List<Grade> grades = gradeService.list(new LambdaQueryWrapper<Grade>().orderByDesc(Grade::getId));
        return Result.ok(grades);
    }
}
