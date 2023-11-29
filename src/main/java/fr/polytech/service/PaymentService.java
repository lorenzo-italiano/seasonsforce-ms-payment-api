package fr.polytech.service;

import fr.polytech.model.*;
import fr.polytech.repository.PaymentRepository;
import fr.polytech.repository.PlanRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class PaymentService {

    /**
     * Initializing logger
     */
    private final Logger logger = LoggerFactory.getLogger(PaymentService.class);
    private static final String USER_API_URL = Optional.ofNullable(System.getenv("USER_API_URL")).orElse("lb://user-api/api/v1/user");
    private static final String ADDRESS_API_URL = Optional.ofNullable(System.getenv("ADDRESS_API_URL")).orElse("lb://address-api/api/v1/address");
    private static final String INVOICE_API_URL = Optional.ofNullable(System.getenv("INVOICE_API_URL")).orElse("lb://invoice-api/api/v1/invoice");

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * Get all payments.
     *
     * @return List of all payments.
     */
    public List<Payment> getAllPayments() {
        logger.info("Getting all payments");
        return paymentRepository.findAll();
    }

    /**
     * Get payment by id.
     *
     * @param id Payment id.
     * @return Payment with the specified id.
     * @throws HttpClientErrorException If payment is not found.
     */
    public Payment getPaymentById(UUID id) throws HttpClientErrorException {
        logger.info("Getting payment by id: " + id);
        Payment payment = paymentRepository.findById(id).orElse(null);
        if (payment == null) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }
        return payment;
    }

    /**
     * Get payments by user id.
     *
     * @param userId User id.
     * @return List of payments.
     */
    public List<Payment> getPaymentsByUserId(UUID userId) {
        logger.info("Getting payments by user id: " + userId);
        return paymentRepository.getPaymentsByUserId(userId);
    }

    /**
     * Create a payment.
     *
     * @param payment     Payment to create.
     * @param bearerToken Bearer token.
     * @return Created payment.
     * @throws HttpClientErrorException If payment creation failed.
     */
    public Payment createPayment(PaymentDTO payment, String bearerToken) throws HttpClientErrorException {
        logger.info("Creating payment");
        validateAttributes(payment);

        String token = extractToken(bearerToken);
        HttpHeaders headers = createHeaders(token);

        RecruiterDTO recruiter = fetchRecruiter(payment.getRecruiterId(), headers);
        AddressDTO address = fetchAddress(recruiter.getAddressId(), headers);

        Plan plan = planRepository.findById(payment.getPlanId()).orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "Plan not found"));
        InvoiceDataDTO invoiceData = buildInvoiceData(payment, recruiter, address, plan);
        validateInvoiceData(invoiceData);

        InvoiceDTO invoice = createInvoice(invoiceData, headers);
        return savePayment(payment, invoice.getId(), plan.getMonthsDuration());
    }

    /**
     * Extract token from bearer token.
     *
     * @param bearerToken Bearer token.
     * @return Token.
     */
    private String extractToken(String bearerToken) throws HttpClientErrorException {
        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "Invalid token");
        }
        return bearerToken.split(" ")[1];
    }

    /**
     * Create headers with token.
     *
     * @param token Token.
     * @return Headers.
     */
    private HttpHeaders createHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return headers;
    }

    /**
     * Fetch recruiter from user-api.
     *
     * @param recruiterId Recruiter id.
     * @param headers     Headers.
     * @return Recruiter.
     */
    private RecruiterDTO fetchRecruiter(UUID recruiterId, HttpHeaders headers) {
        logger.info("Fetching recruiter");
        String url = USER_API_URL + "/" + recruiterId;
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ResponseEntity<RecruiterDTO> response = restTemplate.exchange(url, HttpMethod.GET, entity, RecruiterDTO.class);
        RecruiterDTO recruiter = response.getBody();
        if (recruiter == null || !recruiter.getRole().equals("recruiter")) {
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN, "User is not a recruiter");
        }
        return recruiter;
    }

    /**
     * Fetch address from address-api.
     *
     * @param addressId Address id.
     * @param headers   Headers.
     * @return Address.
     */
    private AddressDTO fetchAddress(UUID addressId, HttpHeaders headers) {
        logger.info("Fetching address");
        if (addressId == null) {
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN, "User is not registred");
        }
        String url = ADDRESS_API_URL + "/" + addressId;
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ResponseEntity<AddressDTO> response = restTemplate.exchange(url, HttpMethod.GET, entity, AddressDTO.class);
        AddressDTO address = response.getBody();
        if (address == null) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Address not found");
        }
        return address;
    }

    /**
     * Build invoice data from payment, recruiter and address.
     *
     * @param payment   Payment.
     * @param recruiter Recruiter.
     * @param address   Address.
     * @return Invoice data.
     */
    private InvoiceDataDTO buildInvoiceData(PaymentDTO payment, RecruiterDTO recruiter, AddressDTO address, Plan plan) {
        InvoiceDataDTO invoiceData = new InvoiceDataDTO();
        invoiceData.setCreationDate(payment.getPaymentDate());
        invoiceData.setName(recruiter.getLastName());
        invoiceData.setSurname(recruiter.getFirstName());
        invoiceData.setAddress(address.toString());

        invoiceData.setPlan(plan.getName());
        invoiceData.setPrice(plan.getPrice());

        return invoiceData;
    }

    /**
     * Validate invoice data.
     *
     * @param invoiceData Invoice data to validate.
     * @throws HttpClientErrorException If invoice data is not valid.
     */
    private void validateInvoiceData(InvoiceDataDTO invoiceData) {
        if (invoiceData.hasInvalidFields()) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Invalid invoice data");
        }
    }

    /**
     * Create invoice.
     *
     * @param invoiceData Invoice data.
     * @param headers     Headers.
     * @return Created invoice.
     * @throws HttpClientErrorException If invoice creation failed.
     */
    private InvoiceDTO createInvoice(InvoiceDataDTO invoiceData, HttpHeaders headers) {
        logger.info("Creating invoice");
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<InvoiceDataDTO> request = new HttpEntity<>(invoiceData, headers);
        ResponseEntity<InvoiceDTO> response = restTemplate.exchange(INVOICE_API_URL + "/", HttpMethod.POST, request, InvoiceDTO.class);
        InvoiceDTO invoice = response.getBody();
        if (invoice == null) {
            throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Invoice creation failed");
        }
        return invoice;
    }

    /**
     * Save payment.
     *
     * @param paymentDTO Payment to save.
     * @param invoiceId  Invoice id.
     * @return Saved payment.
     */
    private Payment savePayment(PaymentDTO paymentDTO, UUID invoiceId, Integer duration) {
        Payment payment = new Payment();

        // expireDate = paymentDTO.getPaymentDate() + duration in months
        Date expireDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(expireDate);
        c.add(Calendar.MONTH, duration);
        expireDate = c.getTime();

        payment.setPaymentDate(paymentDTO.getPaymentDate());
        payment.setExpiresOn(expireDate);
        payment.setPaymentMethod(paymentDTO.getPaymentMethod());
        payment.setPlanId(paymentDTO.getPlanId());
        payment.setInvoiceId(invoiceId);
        payment.setRecruiterId(paymentDTO.getRecruiterId());

        return paymentRepository.save(payment);
    }

    /**
     * Validate payment attributes.
     *
     * @param payment Payment to validate.
     * @throws HttpClientErrorException If attributes are not valid.
     */
    private static void validateAttributes(PaymentDTO payment) throws HttpClientErrorException {
        if (payment.getRecruiterId() == null ||
                payment.getPaymentMethod() == null ||
                payment.getPaymentDate() == null ||
                payment.getPlanId() == null
        ) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Get payment methods.
     *
     * @return List of payment methods.
     */
    public List<PaymentMethod> getPaymentMethods() {
        return List.of(PaymentMethod.values());
    }
}
