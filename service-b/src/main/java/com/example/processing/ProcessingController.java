package com.example.processing;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.Map;
@RestController @RequestMapping("/api/process")
public class ProcessingController {
 public record ProcessRequest(Long orderId) {}
 @PostMapping public Map<String,Object> process(@RequestBody ProcessRequest request) {
  if(request.orderId()==null || request.orderId()<1) throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST,"orderId must be positive");
  return Map.of("orderId",request.orderId(),"status","PROCESSING","processedBy",System.getenv().getOrDefault("INSTANCE_NAME","service-b"),"processedAt",Instant.now().toString());
 }
 @GetMapping("/health") public Map<String,String> health(){return Map.of("service",System.getenv().getOrDefault("INSTANCE_NAME","service-b"),"status","UP");}
}
