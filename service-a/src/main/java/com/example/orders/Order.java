package com.example.orders;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.time.Instant;
@Entity @Table(name="orders")
public class Order {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @NotBlank private String productName;
 @Min(1) private int quantity;
 private String status = "NEW";
 private Instant createdAt = Instant.now();
 protected Order() {}
 public Order(String productName, int quantity) { this.productName=productName; this.quantity=quantity; }
 public Long getId(){return id;} public String getProductName(){return productName;}
 public int getQuantity(){return quantity;} public String getStatus(){return status;}
 public Instant getCreatedAt(){return createdAt;}
 public void setStatus(String status){this.status=status;}
}
