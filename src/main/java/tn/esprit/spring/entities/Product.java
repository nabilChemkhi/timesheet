package tn.esprit.spring.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.consomiTounsi.entities.chopping.CartItem;

@Entity
public class Product implements Serializable {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id ;
	String name,price;
	
	@OneToOne(mappedBy = "product")
	private CartItem cartIem;

}
