package de.ralfrosenkranz.springboot.tagebau.server.controller.dto;

/**
 * OpenAPI: openapi-cart.yaml#/components/schemas/CheckoutRequest
 */
public class CheckoutRequestDTO {
    private String shippingAddress;
    private String billingAddress;
    private String note;

    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }

    public String getBillingAddress() { return billingAddress; }
    public void setBillingAddress(String billingAddress) { this.billingAddress = billingAddress; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
