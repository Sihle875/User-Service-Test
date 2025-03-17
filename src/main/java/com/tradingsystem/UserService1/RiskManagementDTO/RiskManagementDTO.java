package com.tradingsystem.UserService1.RiskManagementDTO;

public class RiskManagementDTO {
    private Long id;
    private Double maxTradeAmount;
    private Double stopLoss;
    private Double takeProfit;
    private Double maxDrawdown;
    private Double lotSize;


    // Default constructor
    public RiskManagementDTO() {}

    // Constructor with all fields
    public RiskManagementDTO(Long traderId, Double maxTradeAmount, Double stopLoss,
                             Double takeProfit, Double maxDrawdown,Double lotSize) {
        this.id = traderId;
        this.stopLoss = stopLoss;
        this.takeProfit = takeProfit;
        this.maxDrawdown = maxDrawdown;
        this.lotSize = lotSize;
    }

//    // Keeping the old constructor for backward compatibility
//    public RiskManagementDTO(Long traderId, Double maxTradeAmount, Double stopLoss, Double takeProfit,Double lotSize) {
//        this(traderId, maxTradeAmount, stopLoss, takeProfit,ma,lotSize);
//    }

    // Getters and Setters
    public Long getTraderId() {
        return id;
    }

    public void setTraderId(Long traderId) {
        this.id = traderId;
    }

    public Double getStopLoss() {
        return stopLoss;
    }

    public void setStopLoss(Double stopLoss) {
        this.stopLoss = stopLoss;
    }

    public Double getTakeProfit() {
        return takeProfit;
    }

    public void setTakeProfit(Double takeProfit) {
        this.takeProfit = takeProfit;
    }

    public Double getMaxDrawdown() {
        return maxDrawdown;
    }

    public void setMaxDrawdown(Double maxDrawdown) {
        this.maxDrawdown = maxDrawdown;
    }

    public Double getLotSize() {
        return lotSize;
    }

    public void setLotSize(Double lotSize) {
        this.lotSize = lotSize;
    }



    // validation method
    public boolean isValid() {
        return id != null
                && maxTradeAmount != null && maxTradeAmount > 0
                && stopLoss != null && stopLoss >= 0 && stopLoss <= 100
                && takeProfit != null && takeProfit >= 0
                && (maxDrawdown == null || (maxDrawdown >= 0 && maxDrawdown <= 100));
    }
}
