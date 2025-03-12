package com.tradingsystem.UserService1.RiskManagement.service;

import com.tradingsystem.UserService1.RiskManagement.model.RiskManagement;
import com.tradingsystem.UserService1.Model.Trader;
import com.tradingsystem.UserService1.RiskManagement.repository.RiskManagementRepository;
import com.tradingsystem.UserService1.Repository.TraderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
public class RiskManagementService implements RiskManagementServiceInterface {

    @Autowired
    private RiskManagementRepository riskManagementRepository;

    @Autowired
    private TraderRepository traderRepository;

    @Override
    public RiskManagement getRiskManagementByTraderId(Long traderId) {
        return riskManagementRepository.findByTrader_Id(traderId);
    }

    public ResponseEntity<String> createRiskManagement(Long traderId,
                                               Double stopLoss,
                                               Double takeProfit,
                                               Double maxDrawdown,
                                               Double lotSize) {
        Optional<Trader> traderOptional = traderRepository.findById(traderId);

        if ((lotSize >= 0.01 && lotSize <= 50) && (stopLoss >= 0.1 && stopLoss <= 20) && (takeProfit >= 0.01 && takeProfit <= 20 ) && (maxDrawdown > 0.01 && maxDrawdown <= 20)) {
            if (traderOptional.isPresent()) {
                Trader trader = traderOptional.get();
                RiskManagement riskManagement = new RiskManagement(trader,
                        stopLoss, takeProfit, maxDrawdown, lotSize);
                 riskManagementRepository.save(riskManagement);
                 return new ResponseEntity<>("User Setting was Created successfully ",HttpStatus.OK);
            } else {

                return new ResponseEntity<>("The Trader was not found",HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
            }

        } else {
            return new ResponseEntity<>("Please make sure you enter correct values",HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);

        }


    }

    public ResponseEntity<String> updateRiskManagement(Long traderId,
                                               Double stopLoss, Double takeProfit, Double maxDrawdown,Double lotsize) {
        RiskManagement riskManagement = riskManagementRepository.findByTrader_Id(traderId);

        if (riskManagement != null) {
            riskManagement.setStopLossPercentage(stopLoss);
            riskManagement.setTakeProfitPercentage(takeProfit);
            riskManagement.setMaxDrawdown(maxDrawdown);
             riskManagementRepository.save(riskManagement);

             return new ResponseEntity<>("You have successfully updated the Setting ",HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Unfortunately The Risk setting were not found for this particular user",HttpStatus.NOT_FOUND);
        }
    }




    // New method to specifically update maxDrawdown
    public ResponseEntity<String> updateMaxDrawdown(Long traderId, Double maxDrawdown) {
        RiskManagement riskManagement = riskManagementRepository.findByTrader_Id(traderId);

        if (maxDrawdown > 0.01 && maxDrawdown <= 20){

            riskManagement.setMaxDrawdown(maxDrawdown);
            riskManagementRepository.save(riskManagement);
            return new ResponseEntity<>("Maximum Draw Down was successfully updated",HttpStatus.OK);
        } else {
            return new ResponseEntity<>("please ensure that you Enter a value between 0.1 and 20",HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<String> updateLotSize(Long traderId, Double lotSize) {
        RiskManagement riskManagement = riskManagementRepository.findByTrader_Id(traderId);

        if ((lotSize >= 0.01 && lotSize <= 50)){

            riskManagement.setLotSize(lotSize);
             riskManagementRepository.save(riskManagement);
             return new ResponseEntity<>("You were successful in update your lot size", HttpStatus.OK) ;
        }else {
            return new ResponseEntity<>("Please make sure you enter a value between 0.1 and 50", HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    public ResponseEntity<String> deleteRiskManagementByTraderId(Long traderId) {
        RiskManagement riskManagement = riskManagementRepository.findByTrader_Id(traderId);
        if (riskManagement != null) {
            System.out.println("proceeding with deletion ");
            riskManagementRepository.deleteByTrader_Id(traderId);
            return new ResponseEntity<>("you were successful in Deleting the user",HttpStatus.OK);
        } else {
            System.out.println("did not find anything ");
            //throw new RuntimeException("Risk management settings not found for trader ID: " + traderId);
            return new ResponseEntity<>("Trader not found with Id: " + traderId,HttpStatus.OK);
        }


    }

    public RiskManagement getRiskManagementByTrader_Id(Long traderId) {
        return riskManagementRepository.findByTrader_Id(traderId);
    }
}


