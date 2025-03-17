package com.tradingsystem.UserService1.RiskManagement.service;

import com.tradingsystem.UserService1.RiskManagement.model.RiskManagement;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

public interface RiskManagementServiceInterface {
    // Existing methods
    RiskManagement getRiskManagementByTraderId(Long traderId);
    RiskManagement getRiskManagementByTrader_Id(Long traderId);

    // New methods
    ResponseEntity<String> createRiskManagement(Long traderId,
                                        Double stopLoss, Double takeProfit, Double maxDrawdown , Double lotSize);

    ResponseEntity<String> updateRiskManagement(Long traderId,
                                        Double stopLoss, Double takeProfit, Double maxDrawdown, Double lotSize);

    ResponseEntity<String> updateMaxDrawdown(Long traderId, Double maxDrawdown);

    HttpEntity<String> updateLotSize(Long traderId, Double lotSize);

    //@Override
    //RiskManagementServiceInterface(Long traderId, Double lotSize);

    ResponseEntity<String> deleteRiskManagementByTraderId(Long traderId);
}
