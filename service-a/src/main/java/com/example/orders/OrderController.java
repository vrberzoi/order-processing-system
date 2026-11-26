package com.example.orders;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.time.Instant;
import java.util.List;
@RestController @RequestMapping("/api/orders")
public class OrderController {
 private final OrderRepository repository;
 public OrderController(OrderRepository repository){this.repository=repository;}
 public record CreateOrderRequest(@NotBlank String productName, @Min(1) int quantity) {}
 @PostMapping @ResponseStatus(HttpStatus.CREATED)
 public Order create(@Valid @RequestBody CreateOrderRequest request){
  return repository.save(new Order(request.productName(), request.quantity()));
 }
 @GetMapping public List<Order> all(){return repository.findAll();}
 @GetMapping("/{id}") public Order one(@PathVariable Long id){
  return repository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Order not found"));
 }
 @GetMapping("/health") public Object health(){return java.util.Map.of("service","service-a","status","UP","time",Instant.now().toString());}
}
