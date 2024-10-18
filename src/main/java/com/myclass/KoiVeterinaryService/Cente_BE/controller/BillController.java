package com.myclass.KoiVeterinaryService.Cente_BE.controller;

import com.myclass.KoiVeterinaryService.Cente_BE.payload.dto.BillDTO;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.response.BillResponse;
import com.myclass.KoiVeterinaryService.Cente_BE.service.BillService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bill")
@Slf4j
@CrossOrigin(origins = "http://localhost:5173")
public class BillController {

    @Autowired
    private BillService billService;

    @PostMapping("/create")
    public ResponseEntity<BillDTO> createBill(@RequestBody BillDTO billDTO) {
        BillDTO createdBill = billService.create(billDTO);
        return new ResponseEntity<>(createdBill, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BillDTO> getBillById(@PathVariable int id) {
        log.info("Fetching bill with ID: {}", id);
        BillDTO bill = billService.findById(id);
        if (bill == null) {
            log.error("Bill with ID {} not found", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(bill, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBill(@PathVariable int id) {
        log.info("Deleting bill with ID: {}", id);
        billService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<BillDTO>> getAllBills() {
        log.info("Fetching all bills");
        List<BillDTO> bills = billService.findAll();
        return new ResponseEntity<>(bills, HttpStatus.OK);
    }

    @GetMapping("/request/{requestId}")
    public ResponseEntity<List<BillDTO>> getBillsByRequest(@PathVariable int requestId) {
        log.info("Fetching bills for request ID: {}", requestId);
        List<BillDTO> bills = billService.findByRequest(requestId);
        return new ResponseEntity<>(bills, HttpStatus.OK);
    }

    @GetMapping("/request/{requestId}/active")
    public ResponseEntity<List<BillDTO>> getActiveBillsByRequest(@PathVariable int requestId) {
        log.info("Fetching active bills for request ID: {}", requestId);
        List<BillDTO> bills = billService.findRequestByActive(requestId);
        return new ResponseEntity<>(bills, HttpStatus.OK);
    }

    @GetMapping("/request/{requestId}/total")
    public ResponseEntity<BillResponse> getTotalAmount(@PathVariable int requestId) {
        BillResponse bill = billService.findTotalAmount(requestId);
        return new ResponseEntity<>(bill, HttpStatus.OK);
    }
}
