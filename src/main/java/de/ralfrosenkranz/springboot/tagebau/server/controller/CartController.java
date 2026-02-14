package de.ralfrosenkranz.springboot.tagebau.server.controller;

import de.ralfrosenkranz.springboot.tagebau.server.controller.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.SessionScope;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Collections;

@RestController
@RequestMapping("/api")
@SessionScope
public class CartController {

    // ---- Cart ----
    @GetMapping("/cart")
    public ResponseEntity<OrderDTO> getCart() {
        // TODO: Session-basierten Cart (Order status=PENDING) laden/erzeugen
        OrderDTO cart = new OrderDTO();
        cart.setId(0L);
        cart.setStatus("PENDING");
        cart.setTotalAmount("0.00");
        cart.setCreatedAt(OffsetDateTime.now(ZoneOffset.UTC));
        cart.setUpdatedAt(OffsetDateTime.now(ZoneOffset.UTC));
        cart.setOrderItems(Collections.emptyList());
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/cart")
    public ResponseEntity<Void> clearCart() {
        // TODO: Cart leeren
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/cart/items")
    public ResponseEntity<OrderDTO> addCartItem(@RequestBody CartItemAddRequestDTO body) {
        // TODO: Item hinzufügen
        return ResponseEntity.ok(getCart().getBody());
    }

    @PatchMapping("/cart/items/{itemId}")
    public ResponseEntity<OrderDTO> updateCartItem(@PathVariable("itemId") Long itemId,
                                                  @RequestBody CartItemUpdateRequestDTO body) {
        // TODO: Menge ändern
        return ResponseEntity.ok(getCart().getBody());
    }

    @DeleteMapping("/cart/items/{itemId}")
    public ResponseEntity<OrderDTO> removeCartItem(@PathVariable("itemId") Long itemId) {
        // TODO: Item entfernen
        return ResponseEntity.ok(getCart().getBody());
    }

    @PostMapping("/cart/checkout")
    public ResponseEntity<OrderDTO> checkout(@RequestBody CheckoutRequestDTO body) {
        // TODO: Pending-Order finalisieren, Status setzen, totals berechnen, persistieren
        OrderDTO order = getCart().getBody();
        if (order != null) {
            order.setId(1L);
            order.setShippingAddress(body.getShippingAddress());
            order.setBillingAddress(body.getBillingAddress());
            order.setStatus("PAID"); // Demo
            order.setUpdatedAt(OffsetDateTime.now(ZoneOffset.UTC));
        }
        return ResponseEntity.status(201).body(order);
    }

    // ---- Orders ----
    @GetMapping("/orders")
    public ResponseEntity<PagedOrderDTO> listOrders(
            @RequestParam(name = "status", required = false) String status,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        // TODO: Bestellungen des Users ermitteln (status/page/size)
        PagedOrderDTO resp = new PagedOrderDTO();
        resp.setItems(Collections.emptyList());
        resp.setPage(page);
        resp.setSize(size);
        resp.setTotalItems(0);
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<OrderDTO> getOrder(@PathVariable("orderId") Long orderId) {
        // TODO: Order laden
        return ResponseEntity.notFound().build();
    }

    // ---- Users (Demo) ----
    @PostMapping("/users/register")
    public ResponseEntity<UserPublicDTO> register(@RequestBody UserRegisterRequestDTO body) {
        // TODO: User anlegen (persistieren, Passwort hashen)
        UserPublicDTO u = new UserPublicDTO();
        u.setId(1L);
        u.setUsername(body.getUsername());
        u.setEmail(body.getEmail());
        u.setRole("USER");
        return ResponseEntity.status(201).body(u);
    }

    @PostMapping("/users/login")
    public ResponseEntity<Void> login(@RequestBody UserLoginRequestDTO body) {
        // TODO: Session setzen
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/users/me")
    public ResponseEntity<UserPublicDTO> me() {
        // TODO: aktuellen User aus Session
        return ResponseEntity.status(401).build();
    }
}
