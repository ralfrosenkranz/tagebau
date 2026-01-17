package de.ralfrosenkranz.springboot.tagebau.server.controller;

import de.ralfrosenkranz.springboot.tagebau.server.controller.dto.*;
import de.ralfrosenkranz.springboot.tagebau.server.model.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.SessionScope;

@RestController
@RequestMapping("/api")
@SessionScope
public class CartController {

    // ---- Cart ----
    @GetMapping("/cart")
    public ResponseEntity<CartDTO> getCart() {
        // TODO: Session-basierten Cart (Order status=PENDING) laden/erzeugen
        return ResponseEntity.ok(new CartDTO());
    }

    @DeleteMapping("/cart")
    public ResponseEntity<Void> clearCart() {
        // TODO: Cart leeren
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/cart/items")
    public ResponseEntity<CartDTO> addCartItem(@RequestBody CartItemAddRequestDTO body) {
        // TODO: Item hinzufügen
        return ResponseEntity.ok(new CartDTO());
    }

    @PatchMapping("/cart/items/{itemId}")
    public ResponseEntity<CartDTO> updateCartItem(@PathVariable("itemId") Long itemId,
                                                  @RequestBody CartItemUpdateRequestDTO body) {
        // TODO: Menge ändern
        return ResponseEntity.ok(new CartDTO());
    }

    @DeleteMapping("/cart/items/{itemId}")
    public ResponseEntity<CartDTO> removeCartItem(@PathVariable("itemId") Long itemId) {
        // TODO: Item entfernen
        return ResponseEntity.ok(new CartDTO());
    }

    @PostMapping("/cart/checkout")
    public ResponseEntity<Order> checkout(@RequestBody CheckoutRequestDTO body) {
        // TODO: Pending-Order finalisieren, Status setzen, totals berechnen
        return ResponseEntity.status(201).body(new Order());
    }

    // ---- Orders ----
    @GetMapping("/orders")
    public ResponseEntity<PagedOrderDTO> listOrders(
            @RequestParam(name = "status", required = false) String status,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        // TODO: Bestellungen des Users ermitteln
        PagedOrderDTO resp = new PagedOrderDTO();
        resp.setContent(java.util.Collections.emptyList());
        resp.setPage(page);
        resp.setSize(size);
        resp.setTotalElements(0);
        resp.setTotalPages(0);
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<Order> getOrder(@PathVariable("orderId") Long orderId) {
        // TODO: Order laden
        return ResponseEntity.notFound().build();
    }

    // ---- Users (Demo) ----
    @PostMapping("/users/register")
    public ResponseEntity<UserPublicDTO> register(@RequestBody UserRegisterRequestDTO body) {
        // TODO: User anlegen
        UserPublicDTO u = new UserPublicDTO();
        u.setId(1L);
        u.setEmail(body.getEmail());
        u.setDisplayName(body.getDisplayName());
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
