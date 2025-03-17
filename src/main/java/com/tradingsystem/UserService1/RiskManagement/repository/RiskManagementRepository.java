package com.tradingsystem.UserService1.RiskManagement.repository;

import com.tradingsystem.UserService1.RiskManagement.model.RiskManagement;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RiskManagementRepository extends JpaRepository<RiskManagement, Long> {
    // Existing methods
    RiskManagement findByTrader_Id(Long traderId);

    @Transactional
    @Modifying
    @Query("DELETE FROM RiskManagement r WHERE r.trader.id = :traderId")
    void deleteByTrader_Id(@Param("traderId") Long traderId);

    // Query methods for maximum drawdown
    @Query("SELECT r FROM RiskManagement r WHERE r.trader.id = :traderId AND r.maxDrawdown >= :threshold")
    RiskManagement findByTraderIdAndMaxDrawdownExceedsThreshold(
            @Param("traderId") Long traderId,
            @Param("threshold") Double threshold
    );

    @Query("SELECT COUNT(r) > 0 FROM RiskManagement r WHERE r.trader.id = :traderId AND r.maxDrawdown >= :currentDrawdown")
    boolean isDrawdownExceedingLimit(
            @Param("traderId") Long traderId,
            @Param("currentDrawdown") Double currentDrawdown
    );

    @Query("SELECT r.maxDrawdown FROM RiskManagement r WHERE r.trader.id = :traderId")
    Double findMaxDrawdownByTraderId(@Param("traderId") Long traderId);
}
