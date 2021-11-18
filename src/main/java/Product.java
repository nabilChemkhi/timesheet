

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Product implements Serializable {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id ;
	String name;
	float price;
	
	@OneToOne(mappedBy = "product")
	private CartItem cartIem;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public CartItem getCartIem() {
		return cartIem;
	}

	public void setCartIem(CartItem cartIem) {
		this.cartIem = cartIem;
	}

	public Product() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

}
