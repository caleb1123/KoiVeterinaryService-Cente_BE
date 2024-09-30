package com.myclass.KoiVeterinaryService.Cente_BE.service.impl;

import com.myclass.KoiVeterinaryService.Cente_BE.entity.Bill;
import com.myclass.KoiVeterinaryService.Cente_BE.exception.AppException;
import com.myclass.KoiVeterinaryService.Cente_BE.exception.ErrorCode;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.dto.BillDTO;
import com.myclass.KoiVeterinaryService.Cente_BE.repository.BillRepository;
import com.myclass.KoiVeterinaryService.Cente_BE.service.BillService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BillServiceImpl implements BillService {
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    BillRepository billRepository;

    @Override
    public BillDTO create(BillDTO billDTO) {
        Bill bill = modelMapper.map(billDTO, Bill.class);
        billRepository.save(bill);
        return modelMapper.map(bill, BillDTO.class);
    }

    @Override
    public BillDTO findById(int id) {
        Bill bill = billRepository.findById(id).orElse(null);
        return modelMapper.map(bill, BillDTO.class);
    }

    @Override
    public void deleteById(int id) {
        Bill bill = billRepository.findById(id).
                orElseThrow(() -> new AppException(ErrorCode.BILL_NOT_FOUND));
        if (bill != null) {
            bill.setStatus(false);
            billRepository.save(bill);
        }
    }

    @Override
    public List<BillDTO> findAll() {
        List<Bill> bills = billRepository.findAll();
        return bills.stream().map(bill -> modelMapper.map(bill, BillDTO.class)).toList();
    }

    @Override
    public List<BillDTO> findByRequest(int requestId) {
//        List<Bill> bills = billRepository.findBillsByServiceRequest(requestId);
        return null;
    }
}
