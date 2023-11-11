package fr.polytech.service;

import fr.polytech.model.Plan;
import fr.polytech.model.PlanDTO;
import fr.polytech.repository.PlanRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.UUID;

@Service
public class PlanService {
    /**
     * Initializing logger
     */
    private final Logger logger = LoggerFactory.getLogger(PlanService.class);

    @Autowired
    private PlanRepository planRepository;

    /**
     * Get all plans.
     *
     * @return List of all plans.
     */
    public List<Plan> getAllPlans() {
        logger.info("Getting all plans");
        return planRepository.findAll();
    }

    /**
     * Get plan by id.
     *
     * @param id Plan id.
     * @return Plan with the specified id.
     * @throws HttpClientErrorException If plan is not found.
     */
    public Plan getPlanById(UUID id) throws HttpClientErrorException {
        logger.info("Getting plan by id: " + id);
        Plan plan = planRepository.findById(id).orElse(null);
        if (plan == null) {
            logger.error("Plan not found");
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        } else {
            return plan;
        }
    }

    /**
     * Get plan by currency.
     *
     * @param currency Plan currency.
     * @return Plan with the specified currency.
     * @throws HttpClientErrorException If plan is not found.
     */
    public List<Plan> getPlansByCurrency(String currency) throws HttpClientErrorException {
        logger.info("Getting plan by currency: " + currency);
        List<Plan> plans = planRepository.findByCurrency(currency);
        if (plans == null) {
            logger.error("Plan not found");
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        } else {
            return plans;
        }
    }

    /**
     * Create plan.
     *
     * @param plan Plan to create.
     * @return Created plan.
     * @throws HttpClientErrorException If plan is not valid.
     */
    public Plan createPlan(PlanDTO plan) throws HttpClientErrorException {
        logger.info("Creating plan");

        plan.validateAttributes();

        Plan newPlan = new Plan();
        newPlan.setCurrency(plan.getCurrency());
        newPlan.setPrice(plan.getPrice());
        newPlan.setDescription(plan.getDescription());
        newPlan.setName(plan.getName());
        newPlan.setMonthsDuration(plan.getMonthsDuration());

        return planRepository.save(newPlan);
    }

    /**
     * Update plan.
     *
     * @param plan Plan to update.
     * @return Updated plan.
     * @throws HttpClientErrorException If plan is not found.
     */
    public Plan updatePlan(PlanDTO plan) throws HttpClientErrorException {
        logger.info("Updating plan");

        plan.validateAttributes();
        if (plan.getId() == null) {
            logger.error("Plan id is null");
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
        }

        Plan newPlan = planRepository.findById(plan.getId()).orElse(null);
        if (newPlan == null) {
            logger.error("Plan not found");
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        } else {
            newPlan.setCurrency(plan.getCurrency());
            newPlan.setPrice(plan.getPrice());
            newPlan.setDescription(plan.getDescription());
            newPlan.setName(plan.getName());
            newPlan.setMonthsDuration(plan.getMonthsDuration());

            return planRepository.save(newPlan);
        }
    }

    /**
     * Delete plan.
     *
     * @param id Plan id.
     * @throws HttpClientErrorException If plan is not found.
     */
    public void deletePlan(UUID id) throws HttpClientErrorException {
        logger.info("Deleting plan");
        Plan plan = planRepository.findById(id).orElse(null);
        if (plan == null) {
            logger.error("Plan not found");
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        } else {
            planRepository.delete(plan);
        }
    }
}
