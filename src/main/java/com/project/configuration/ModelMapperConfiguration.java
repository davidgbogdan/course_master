package com.project.configuration;

import com.project.dto.request.course.CourseRequestDto;
import com.project.entity.Course;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfiguration {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.addMappings(new PropertyMap<CourseRequestDto, Course>() {
            @Override
            protected void configure() {
                skip(destination.getSchedules());
            }
        });

        return modelMapper;
    }

}
