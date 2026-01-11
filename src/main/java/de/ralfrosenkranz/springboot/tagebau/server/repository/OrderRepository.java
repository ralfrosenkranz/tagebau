package de.ralfrosenkranz.springboot.tagebau.server.repository;

import de.ralfrosenkranz.springboot.tagebau.server.model.Order;
import de.ralfrosenkranz.springboot.tagebau.server.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    List<Order> findByUser(User user);
    
    List<Order> findByStatus(String status);
}
