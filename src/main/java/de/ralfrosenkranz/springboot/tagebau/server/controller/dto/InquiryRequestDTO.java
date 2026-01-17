package de.ralfrosenkranz.springboot.tagebau.server.controller.dto;

public class InquiryRequestDTO {
    private String productId;
    private String email;
    private String name;
    private String company;
    private String message;

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
