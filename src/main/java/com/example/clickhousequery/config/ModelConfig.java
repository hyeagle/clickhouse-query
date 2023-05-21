package com.example.clickhousequery.config;

import com.example.clickhousequery.entity.DataModel;
import com.example.clickhousequery.repository.impl.ModelRowMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelConfig {

    @Bean
    @Qualifier("userModel")
    public DataModel userModel() throws Exception {
        return DataModel.fromYaml("data/userInfo.yml");
    }

    @Bean
    @Qualifier("userMapper")
    public ModelRowMapper userMapper(@Qualifier("userModel") DataModel dataModel) {
        return new ModelRowMapper(dataModel);
    }
}
