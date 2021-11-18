package tn.esprit.spring.entities;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.consomiTounsi.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

}
