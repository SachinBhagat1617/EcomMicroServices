package com.ecommerce_micro_service.user.repositories;

import com.ecommerce_micro_service.user.models.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User,String> {
    Boolean existsByEmail(@Size(max=50) @Email String email);
}
