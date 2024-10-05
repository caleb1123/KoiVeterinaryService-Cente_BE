package com.myclass.KoiVeterinaryService.Cente_BE.config;

import com.myclass.KoiVeterinaryService.Cente_BE.entity.*;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.dto.*;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.dto.AccountDTO;
import com.myclass.KoiVeterinaryService.Cente_BE.dto.PaymentDTO;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.dto.ServiceRequestDTO;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        // Create ModelMapper instance and configure matching strategy
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        modelMapper.typeMap(Account.class, AccountDTO.class).addMappings(mapper -> {
            mapper.map(src -> src.getRole().getRoleId(), AccountDTO::setRoleId);
        });

        modelMapper.typeMap(Bill.class, BillDTO.class).addMappings(mapper -> {
            mapper.map(src -> src.getServiceRequest().getRequestId(), BillDTO::setRequestId);
            mapper.map(src -> src.getService().getServiceId(), BillDTO::setServiceId);
        });

        modelMapper.typeMap(Feedback.class, FeedbackDTO.class).addMappings(mapper -> {
            mapper.map(src -> src.getCustomer().getAccountId(), FeedbackDTO::setCustomerId);
            mapper.map(src -> src.getBill().getBillId(), FeedbackDTO::setBillId);});

        modelMapper.typeMap(ServiceRequest.class, ServiceRequestDTO.class).addMappings(mapper -> {
            mapper.map(src -> src.getCustomer().getAccountId(), ServiceRequestDTO::setCustomerId);
            mapper.map(src -> src.getShift().getShiftId(), ServiceRequestDTO::setShiftId);
            mapper.map(src -> src.getVeterinarian().getAccountId(), ServiceRequestDTO::setVeterinarianId);
        });

        modelMapper.typeMap(ServiceImage.class, ServiceImageDTO.class).addMappings(mapper -> {
            mapper.map(src -> src.getService().getServiceId(), ServiceImageDTO::setServiceId);
        });

        modelMapper.typeMap(Post.class, PostDTO.class).addMappings(mapper -> {
            mapper.map(src -> src.getAccount().getAccountId(), PostDTO::setAuthorId);
        });

        modelMapper.typeMap(PostImage.class, PostImageDTO.class).addMappings(mapper -> {
            mapper.map(src -> src.getPost().getPostId(), PostImageDTO::setPostId);
        });

        modelMapper.typeMap(Payment.class, com.myclass.KoiVeterinaryService.Cente_BE.dto.PaymentDTO.class).addMappings(mapper -> {
            mapper.map(src -> src.getServiceRequest().getRequestId(), PaymentDTO::setRequestId);
            mapper.map(src -> src.getAccount().getAccountId(), PaymentDTO::setCustomerId);
        });
        return modelMapper;
    }
}
