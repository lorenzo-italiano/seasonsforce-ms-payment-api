package fr.polytech.repository;

import fr.polytech.model.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PlanRepository extends JpaRepository<Plan, UUID> {
    @Query("SELECT p FROM Plan p WHERE p.currency = :currency")
    List<Plan> findByCurrency(String currency);
}
