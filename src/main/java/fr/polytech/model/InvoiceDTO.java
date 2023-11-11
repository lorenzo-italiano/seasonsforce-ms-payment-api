package fr.polytech.model;

import java.util.Date;
import java.util.UUID;

public class InvoiceDTO {
    private UUID id;

    private Date creationDate;

    private String pdfUrl;

    public InvoiceDTO() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }
}
