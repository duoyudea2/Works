package com.example.demo.dao;

import com.example.demo.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserDao {

    @Insert("INSERT INTO users (username, password, place_of_birth, birth_datetime, bazi) " +
            "VALUES (#{username}, #{password}, #{placeOfBirth}, #{birthDatetime}, #{bazi})")
    int register(User user);

    @Select("SELECT * FROM users WHERE username = #{username}")
    @Results({
        @Result(property = "placeOfBirth", column = "place_of_birth"),
        @Result(property = "birthDatetime", column = "birth_datetime")
    })
    User findByUsername(String username);

    @Update("UPDATE users SET bazi = #{bazi} WHERE id = #{id}")
    int updateBazi(@Param("id") int id, @Param("bazi") String bazi);
}

