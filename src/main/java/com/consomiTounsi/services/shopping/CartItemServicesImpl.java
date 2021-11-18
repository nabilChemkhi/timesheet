package com.consomiTounsi.services.shopping;

import org.springframework.stereotype.Service;

import com.consomiTounsi.entities.shopping.CartItem;

@Service
public class CartItemServicesImpl implements ICartItemServices{
	
	public void addToCart(Long carId, CartItem cartItems, String username){

		Product product = productRepository.findById(carId).orElse(null);
		    cartItems.setProduct(product);

		        cartItems.setSubTotal(product.getPrice());
		        cartItems.setUsername(username);

		        cartItemsRepository.save(cartItems);

		}
		public List<CartItems> myCart(String userName){

		    List<CartItems> cartItems = new ArrayList<>();
		    cartItemsRepository.findByUsername(userName).forEach(cartItems::add);

		    return cartItems;
		}

}
