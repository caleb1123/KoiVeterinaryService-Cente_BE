package com.myclass.KoiVeterinaryService.Cente_BE.config;

import com.myclass.KoiVeterinaryService.Cente_BE.payload.dto.ServiceRequestDTO;
import com.myclass.KoiVeterinaryService.Cente_BE.entity.ServiceRequest;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        // Tạo đối tượng ModelMapper và cấu hình matching strategy
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);


        return modelMapper;
    }
}
