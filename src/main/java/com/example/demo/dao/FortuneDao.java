package com.example.demo.dao;

import com.example.demo.model.Fortune;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface FortuneDao {

    @Select("SELECT * FROM fortunes WHERE id = #{id}")
    Fortune findById(int id);
}



