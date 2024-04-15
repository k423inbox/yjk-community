package com.example.community.dao;

import org.apache.ibatis.annotations.Select;


    public interface AlphaDao {

        @Select("select * from user")
        String select();

    }

