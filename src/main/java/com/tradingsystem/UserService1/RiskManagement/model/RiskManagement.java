package com.tradingsystem.UserService1.RiskManagement.model;

import com.tradingsystem.UserService1.Model.Trader;
import jakarta.persistence.*;

@Entity
public class RiskManagement {
    @OneToOne
    @JoinColumn(name = "trader_id", unique = true)
    public Trader trader;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double stopLossPercentage;
    private Double takeProfitPercentage;
    private Double maxDrawdown;
    private Double lotSize;



    public RiskManagement() {}

    public RiskManagement(Trader trader, Double stopLossPercentage,
                          Double takeProfitPercentage, Double maxDrawdown, Double lotSize) {
        this.trader = trader;
        this.stopLossPercentage = stopLossPercentage;
        this.takeProfitPercentage = takeProfitPercentage;
        this.maxDrawdown = maxDrawdown;
        this.lotSize = lotSize;
    }

    // Existing getters and setters...
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Trader getTrader() {
        return trader;
    }

    public void setTrader(Trader trader) {
        this.trader = trader;
    }

    public Double getStopLossPercentage() {
        return stopLossPercentage;
    }

    public void setStopLossPercentage(Double stopLossPercentage) {
        this.stopLossPercentage = stopLossPercentage;
    }

    public Double getTakeProfitPercentage() {
        return takeProfitPercentage;
    }

    public void setTakeProfitPercentage(Double takeProfitPercentage) {
        this.takeProfitPercentage = takeProfitPercentage;
    }

    public Double getLotSize() {
        return lotSize;
    }

    public void setLotSize(Double lotSize) {
        this.lotSize = lotSize;
    }

    public Double getMaxDrawdown() {
        return maxDrawdown;
    }

    public void setMaxDrawdown(Double maxDrawdown) {
        this.maxDrawdown = maxDrawdown;
    }

    // Utility methods
    public Long getTraderId() {
        return trader != null ? trader.getId() : null;
    }

    // DTO mapping methods
    public Double getStopLoss() {
        return stopLossPercentage;
    }

    public Double getTakeProfit() {
        return takeProfitPercentage;
    }

    public void setStopLoss(Double stopLoss) {
        this.stopLossPercentage = stopLoss;
    }

    public void setTakeProfit(Double takeProfit) {
        this.takeProfitPercentage = takeProfit;
    }
}



