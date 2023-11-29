package fr.polytech.restcontroller;

import fr.polytech.annotation.IsAdmin;
import fr.polytech.annotation.IsRecruiterOrAdmin;
import fr.polytech.model.Plan;
import fr.polytech.model.PlanDTO;
import fr.polytech.service.PlanService;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/plan")
public class PlanController {

    private final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    private PlanService planService;

    /**
     * Get all plans.
     *
     * @return List of all plans.
     */
    @GetMapping("/")
    @IsRecruiterOrAdmin
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Plan>> getAllPlans() {
        try {
            List<Plan> plans = planService.getAllPlans();
            logger.info("Got all plans");
            return new ResponseEntity<>(plans, HttpStatus.OK);
        } catch (HttpClientErrorException e) {
            logger.error("Error while getting all plans" + e.getStatusCode(), e);
            return new ResponseEntity<>(HttpStatus.valueOf(e.getStatusCode().value()));
        } catch (Exception e) {
            logger.error("Error while getting all plans", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get plan by id.
     *
     * @param id Plan id.
     * @return Plan with the specified id.
     */
    @GetMapping("/{id}")
    @IsRecruiterOrAdmin
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Plan> getPlanById(@PathVariable("id") UUID id) {
        try {
            Plan plan = planService.getPlanById(id);
            logger.info("Got plan by id: " + id);
            return new ResponseEntity<>(plan, HttpStatus.OK);
        } catch (HttpClientErrorException e) {
            logger.error("Error while getting plan by id: " + id + " " + e.getStatusCode(), e);
            return new ResponseEntity<>(HttpStatus.valueOf(e.getStatusCode().value()));
        } catch (Exception e) {
            logger.error("Error while getting plan by id: " + id, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get plan by currency.
     *
     * @param currency Plan currency.
     * @return Plan with the specified currency.
     */
    @GetMapping("/currency/{currency}")
    @IsRecruiterOrAdmin
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Plan>> getPlanByCurrency(@PathVariable("currency") String currency) {
        try {
            List<Plan> plans = planService.getPlansByCurrency(currency);
            logger.info("Got plan by currency: " + currency);
            return new ResponseEntity<>(plans, HttpStatus.OK);
        } catch (HttpClientErrorException e) {
            logger.error("Error while getting plan by currency: " + currency + " " + e.getStatusCode(), e);
            return new ResponseEntity<>(HttpStatus.valueOf(e.getStatusCode().value()));
        } catch (Exception e) {
            logger.error("Error while getting plan by currency: " + currency, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Update plan.
     *
     * @param plan Plan to update.
     * @return Updated plan.
     */
    @PutMapping("/")
    @IsAdmin
    @Consumes(MediaType.APPLICATION_JSON_VALUE)
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Plan> updatePlan(@RequestBody PlanDTO plan) {
        try {
            Plan updatedPlan = planService.updatePlan(plan);
            logger.info("Updated plan with id: " + plan.getId());
            return new ResponseEntity<>(updatedPlan, HttpStatus.OK);
        } catch (HttpClientErrorException e) {
            logger.error("Error while updating plan with id: " + plan.getId() + " " + e.getStatusCode(), e);
            return new ResponseEntity<>(HttpStatus.valueOf(e.getStatusCode().value()));
        } catch (Exception e) {
            logger.error("Error while updating plan with id: " + plan.getId(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Create a new plan.
     *
     * @param plan Plan to create.
     * @return Created plan.
     */
    @PostMapping("/")
    @IsAdmin
    @Consumes(MediaType.APPLICATION_JSON_VALUE)
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Plan> createPlan(@RequestBody PlanDTO plan) {
        try {
            Plan createdPlan = planService.createPlan(plan);
            logger.info("Created plan with id: " + plan.getId());
            return new ResponseEntity<>(createdPlan, HttpStatus.OK);
        } catch (HttpClientErrorException e) {
            logger.error("Error while creating plan with id: " + plan.getId() + " " + e.getStatusCode(), e);
            return new ResponseEntity<>(HttpStatus.valueOf(e.getStatusCode().value()));
        } catch (Exception e) {
            logger.error("Error while creating plan with id: " + plan.getId(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Delete a plan.
     *
     * @param id Plan id.
     * @return True if the plan has been deleted, false otherwise.
     */
    @DeleteMapping("/{id}")
    @IsAdmin
    @Produces(MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<Boolean> deletePlan(@PathVariable("id") UUID id) {
        try {
            planService.deletePlan(id);
            logger.info("Deleted plan with id: " + id);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (HttpClientErrorException e) {
            logger.error("Error while deleting plan with id: " + id + " " + e.getStatusCode(), e);
            return new ResponseEntity<>(HttpStatus.valueOf(e.getStatusCode().value()));
        } catch (Exception e) {
            logger.error("Error while deleting plan with id: " + id, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
