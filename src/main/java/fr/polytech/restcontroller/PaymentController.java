package fr.polytech.restcontroller;

import fr.polytech.annotation.IsAdmin;
import fr.polytech.annotation.IsRecruiter;
import fr.polytech.annotation.IsRecruiterOrAdmin;
import fr.polytech.model.Payment;
import fr.polytech.model.PaymentDTO;
import fr.polytech.model.PaymentMethod;
import fr.polytech.service.PaymentService;
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
@RequestMapping("/api/v1/payment")
public class PaymentController {

    private final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    private PaymentService paymentService;

    /**
     * Get all payments.
     *
     * @return List of all payments.
     */
    @GetMapping("/")
    @IsAdmin
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Payment>> getPayment() {
        try {
            List<Payment> payments = paymentService.getAllPayments();
            logger.info("Got all payments");
            return new ResponseEntity<>(payments, HttpStatus.OK);
        } catch (HttpClientErrorException e) {
            logger.error("Error while getting all payments" + e.getStatusCode(), e);
            return new ResponseEntity<>(HttpStatus.valueOf(e.getStatusCode().value()));
        } catch (Exception e) {
            logger.error("Error while getting all payments", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get payment by id.
     *
     * @param id Payment id.
     * @return Payment with the specified id.
     */
    @GetMapping("/{id}")
    @IsRecruiterOrAdmin
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Payment> getPaymentById(@PathVariable("id") UUID id) {
        try {
            Payment payment = paymentService.getPaymentById(id);
            logger.info("Got payment with id " + id);
            return new ResponseEntity<>(payment, HttpStatus.OK);
        } catch (HttpClientErrorException e) {
            logger.error("Error while getting payment with id " + id + " " + e.getStatusCode(), e);
            return new ResponseEntity<>(HttpStatus.valueOf(e.getStatusCode().value()));
        } catch (Exception e) {
            logger.error("Error while getting payment with id " + id, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get payment by user id.
     *
     * @param id User id.
     * @return Payment with the specified user id.
     */
    @GetMapping("/user/{id}")
    @IsRecruiterOrAdmin
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Payment>> getPaymentByUserId(@PathVariable("id") UUID id) {
        try {
            List<Payment> payments = paymentService.getPaymentsByUserId(id);
            logger.info("Got payment with user id " + id);
            return new ResponseEntity<>(payments, HttpStatus.OK);
        } catch (HttpClientErrorException e) {
            logger.error("Error while getting payment with user id " + id + " " + e.getStatusCode(), e);
            return new ResponseEntity<>(HttpStatus.valueOf(e.getStatusCode().value()));
        } catch (Exception e) {
            logger.error("Error while getting payment with user id " + id, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get available payment methods.
     *
     * @return List of available payment methods.
     */
    @GetMapping("/methods/")
    @IsRecruiterOrAdmin
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PaymentMethod>> getPaymentByPaymentMethod() {
        try {
            List<PaymentMethod> paymentMethods = paymentService.getPaymentMethods();
            logger.info("Got payment methods");
            return new ResponseEntity<>(paymentMethods, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error while getting payment methods", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Create a new payment.
     *
     * @param payment Payment to create.
     * @return Created payment.
     */
    @PostMapping("/")
    @IsRecruiter
    @Consumes(MediaType.APPLICATION_JSON_VALUE)
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Payment> createPayment(
            @RequestBody PaymentDTO payment,
            @RequestHeader("Authorization") String token
    ) {
        try {
            Payment createdPayment = paymentService.createPayment(payment, token);
            logger.info("Created payment with id " + createdPayment.getId());
            return new ResponseEntity<>(createdPayment, HttpStatus.CREATED);
        } catch (HttpClientErrorException e) {
            logger.error("Error while creating payment " + e.getStatusCode(), e);
            return new ResponseEntity<>(HttpStatus.valueOf(e.getStatusCode().value()));
        } catch (Exception e) {
            logger.error("Error while creating payment", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
