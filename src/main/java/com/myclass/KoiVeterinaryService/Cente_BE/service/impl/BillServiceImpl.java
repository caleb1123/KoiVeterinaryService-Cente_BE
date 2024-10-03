package com.myclass.KoiVeterinaryService.Cente_BE.service.impl;

import com.myclass.KoiVeterinaryService.Cente_BE.entity.Bill;
import com.myclass.KoiVeterinaryService.Cente_BE.entity.EStatus;
import com.myclass.KoiVeterinaryService.Cente_BE.entity.ServiceKoi;
import com.myclass.KoiVeterinaryService.Cente_BE.entity.ServiceRequest;
import com.myclass.KoiVeterinaryService.Cente_BE.exception.AppException;
import com.myclass.KoiVeterinaryService.Cente_BE.exception.ErrorCode;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.dto.BillDTO;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.response.BillResponse;
import com.myclass.KoiVeterinaryService.Cente_BE.repository.BillRepository;
import com.myclass.KoiVeterinaryService.Cente_BE.repository.ServiceKoiRepository;
import com.myclass.KoiVeterinaryService.Cente_BE.repository.ServiceRequestRepository;
import com.myclass.KoiVeterinaryService.Cente_BE.service.BillService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@Service
public class BillServiceImpl implements BillService {
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    BillRepository billRepository;
    @Autowired
    ServiceRequestRepository serviceRequestRepository;
    @Autowired
    ServiceKoiRepository serviceKoiRepository;
    @Override
    public BillDTO create(BillDTO billDTO) {
        ServiceRequest serviceRequest = serviceRequestRepository.findById(billDTO.getRequestId())
                .orElseThrow(() -> new AppException(ErrorCode.SERVICE_REQUEST_NOT_FOUND));
        ServiceKoi serviceKoi = serviceKoiRepository.findById(billDTO.getServiceId())
                .orElseThrow(() -> new AppException(ErrorCode.SERVICE_NOT_FOUND));
        if(serviceRequest.getStatus() == EStatus.COMPLETED){
           throw new AppException(ErrorCode.SERVICE_REQUEST_COMPLETED);
        }
        Bill bill = new Bill();
        bill.setServiceRequest(serviceRequest);
        bill.setService(serviceKoi);
        bill.setQuantity(billDTO.getQuantity());
        bill.setStatus(true);
        billRepository.save(bill);
        billDTO.setBillId(bill.getBillId());
        return billDTO;
    }

    @Override
    public BillDTO findById(int id) {
        Bill bill = billRepository.findById(id).
                orElseThrow(() -> new AppException(ErrorCode.BILL_NOT_FOUND));
        BillDTO billDTO = new BillDTO();
        billDTO.setBillId(bill.getBillId());
        billDTO.setRequestId(bill.getServiceRequest().getRequestId());
        billDTO.setServiceId(bill.getService().getServiceId());
        billDTO.setStatus(bill.isStatus());
        return billDTO;
    }

    @Override
    public void deleteById(int id) {
        Bill bill = billRepository.findById(id).
                orElseThrow(() -> new AppException(ErrorCode.BILL_NOT_FOUND));
        ServiceRequest serviceRequest = serviceRequestRepository.findById(bill.getServiceRequest().getRequestId())
                .orElseThrow(() -> new AppException(ErrorCode.SERVICE_REQUEST_NOT_FOUND));
        if (serviceRequest.getStatus() == EStatus.COMPLETED) {
           throw new AppException(ErrorCode.SERVICE_REQUEST_COMPLETED);
        }
        bill.setStatus(false);
        billRepository.save(bill);
    }

    @Override
    public List<BillDTO> findAll() {
        List<Bill> bills = billRepository.findAll();
        return bills.stream().map(bill -> modelMapper.map(bill, BillDTO.class)).toList();
    }

    @Override
    public List<BillDTO> findByRequest(int requestId) {
        List<Bill> bills = billRepository.findByRequest(requestId);
        if (bills.isEmpty()) {
            throw new AppException(ErrorCode.BILL_NOT_FOUND);
        }
        return bills.stream().map(bill -> modelMapper.map(bill, BillDTO.class)).toList();
    }

    @Override
    public List<BillDTO> findRequestByActive(int requestId) {
        List<Bill> bills = billRepository.findRequestByActive(requestId);
        if (bills.isEmpty()) {
            throw new AppException(ErrorCode.BILL_NOT_FOUND);
        }
        return bills.stream().map(bill -> modelMapper.map(bill, BillDTO.class)).toList();
    }

    @Override
    public BillResponse findTotalAmount(int requestId) {
        // Validate service request
        ServiceRequest serviceRequest = serviceRequestRepository.findById(requestId)
                .orElseThrow(() -> new AppException(ErrorCode.SERVICE_REQUEST_NOT_FOUND));

        // Fetch total amount from the bill repository
        Object result = billRepository.findTotalAmount(requestId);

        // Ensure the result is not null and is an array of Object
        if (result == null || !(result instanceof Object[])) {
            throw new AppException(ErrorCode.BILL_NOT_FOUND);
        }

        // Cast the result to Object array
        Object[] resultArray = (Object[]) result;

        // Check if the result array has the expected length
        if (resultArray.length < 2) {
            throw new AppException(ErrorCode.BILL_NOT_FOUND);
        }

        // Create BillResponse object and populate it
        BillResponse billResponse = new BillResponse();

        // Cast and set the values
        billResponse.setRequestId(((Number) resultArray[0]).intValue()); // Cast to Number and get int
        billResponse.setTotal_amount(((Number) resultArray[1]).doubleValue()); // Cast to Number and get double

        return billResponse; // Return the populated BillResponse object
    }





}
