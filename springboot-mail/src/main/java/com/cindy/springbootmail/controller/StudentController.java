package com.cindy.springbootmail.controller;

import com.cindy.springbootmail.Student;
import com.cindy.springbootmail.StudentRowMapper;
import com.cindy.springbootmail.dao.StudentDao;
import com.cindy.springbootmail.service.StudentService;
import com.mysql.cj.jdbc.MysqlParameterMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class StudentController {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private StudentService studentService;

    @PostMapping("/students")
    public String insert(@RequestBody Student student){
        String sql = "INSERT INTO student( name) values (:studentName)";

        Map<String , Object> map = new HashMap<>();
        //map.put("studentId", student.getId());
        map.put("studentName",student.getName());

        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(sql,new MapSqlParameterSource(map),keyHolder);
        int id = keyHolder.getKey().intValue();
        System.out.println("mysql 自動生成的id = " + id );

        return "執行INSERT sql";
    }

    @PostMapping("/students/batch")
    public String insertList(@RequestBody List<Student> studentList){
        String sql = "INSERT INTO student(name) values (:studentName)";

        MapSqlParameterSource[] parameterSources = new MapSqlParameterSource[studentList.size()];

        for(int i = 0 ; i < studentList.size() ; i++ ){
            Student student = studentList.get(i);
            parameterSources[i] = new MapSqlParameterSource();
            parameterSources[i].addValue("studentName",student.getName());
        }

        namedParameterJdbcTemplate.batchUpdate(sql , parameterSources);

        return "執行一批 INSERT sql";
    }

    @DeleteMapping("/students/{studentId}")
    public String delete(@PathVariable Integer studentId){
        String sql = "DELETE FROM student WHERE id = :studentId ";

        Map<String , Object> map = new HashMap<>();
        map.put("studentId",studentId);

        namedParameterJdbcTemplate.update(sql,map);

        return "刪除 DELETE sql";
    }

    @GetMapping("/students")
    public List<Student> select(){
        String sql = "SELECT id , name FROM student  ";
        Map<String , Object> map = new HashMap<>();

        List<Student> list = namedParameterJdbcTemplate.query(sql , map , new StudentRowMapper());
        return  list;
    }

    @GetMapping("/students/{studentId}")
    public Student select(@PathVariable Integer studentId){
        return studentService.getById(studentId);
    }

}
