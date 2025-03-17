package com.tradingsystem.UserService1.RiskManagement.controller;

import com.tradingsystem.UserService1.RiskManagementDTO.RiskManagementDTO;
import com.tradingsystem.UserService1.RiskManagement.model.RiskManagement;
import com.tradingsystem.UserService1.RiskManagement.service.RiskManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/risk_management")
public class RiskManagementController {

    @Autowired
    private RiskManagementService riskManagementService;

    @PostMapping("/create")
    public ResponseEntity<String> createRiskManagement(@RequestBody RiskManagementDTO riskManagementDTO) {
        return riskManagementService.createRiskManagement(
                riskManagementDTO.getTraderId(),
                riskManagementDTO.getStopLoss(),
                riskManagementDTO.getTakeProfit(),
                riskManagementDTO.getMaxDrawdown(),
                riskManagementDTO.getLotSize()
        );
        //return ResponseEntity.ok(savedRiskManagement);
    }

    @GetMapping("/{traderId}")
    public ResponseEntity<RiskManagement> getRiskManagement(@PathVariable Long traderId) {
        RiskManagement riskManagement = riskManagementService.getRiskManagementByTraderId(traderId);
        return ResponseEntity.ok(riskManagement);
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateRiskManagement(@RequestBody RiskManagementDTO riskManagementDTO) {
        return riskManagementService.updateRiskManagement(
                riskManagementDTO.getTraderId(),
                riskManagementDTO.getStopLoss(),
                riskManagementDTO.getTakeProfit(),
                riskManagementDTO.getMaxDrawdown(),
                riskManagementDTO.getLotSize()
        );
        //return ResponseEntity.ok(updatedRiskManagement);
    }

    @DeleteMapping("/delete/{traderId}")
    public ResponseEntity<String> deleteRiskManagement(@PathVariable Long traderId) {
        return riskManagementService.deleteRiskManagementByTraderId(traderId);
        //return ResponseEntity.ok("Risk management settings deleted successfully for trader ID: " + traderId);
    }

    // Optional: Add an endpoint to specifically update maxDrawdown
    @PatchMapping("/{traderId}/max-drawdown")
    public ResponseEntity<String> updateMaxDrawdown(
            @PathVariable Long traderId,
            @RequestParam Double maxDrawdown) {
        return riskManagementService.updateMaxDrawdown(traderId, maxDrawdown);
        //return ResponseEntity.ok(updatedRiskManagement);
    }

    // Optional: endPoint to Specifically update the lotSize:

    @PatchMapping("/{traderId}/lotSize")
    public  ResponseEntity<String> UpdateLotSize(
            @PathVariable Long traderId,
            @RequestParam Double lotSize
    ){
         //riskManagementService.updateLotSize(traderId,lotSize);
        return  riskManagementService.updateLotSize(traderId,lotSize);
    }
}



