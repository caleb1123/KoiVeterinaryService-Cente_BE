package com.myclass.KoiVeterinaryService.Cente_BE.service;

import com.myclass.KoiVeterinaryService.Cente_BE.payload.dto.BillDTO;

import java.util.List;

public interface BillService {
    BillDTO create(BillDTO billDTO);
    BillDTO findById(int id);
    void deleteById(int id);
    List<BillDTO> findAll();
    List<BillDTO> findByRequest(int requestId);
    List<BillDTO> findRequestByActive(int requestId);
}
