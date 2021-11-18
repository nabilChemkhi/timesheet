

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.consomiTounsi.Repository.OrderRepository;
import com.consomiTounsi.Repository.UserRepository;
import com.consomiTounsi.entities.ChargeRequest;
import com.consomiTounsi.entities.CreatePaymentResponse;
import com.consomiTounsi.entities.Order;
import com.consomiTounsi.entities.PaymentMethod;
import com.consomiTounsi.entities.Users;
import com.stripe.Stripe;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Token;
import com.stripe.param.PaymentIntentCreateParams;









@Service
public class StripeServices {
	
	@Autowired
	UserRepository userRepo;
	@Autowired
	OrderRepository
	orderRepo;
	
	@Value("${stripe.keys.secret}")
	private String secretKey;
	
	@PostConstruct
	public void init() {
		Stripe.apiKey = secretKey;
	}
	
	
	
	public String createCharge(String email, String token, double amount)
    {
        String id = null;
        try
        {
            //Stripe.apiKey = secretKey;
            Map chargeParams = new HashMap<>();
            chargeParams.put("amount", (int)(amount*100));
            chargeParams.put("currency", "USD");
            chargeParams.put("description", "Charge for " + email);
            chargeParams.put("source", token);
            Charge charge = Charge.create(chargeParams);
            id = charge.getId();
            
           // commandeRepository.PayerEnLigne(orders.getId());
        }
        catch(StripeException e)
        {
            throw new RuntimeException("Unable to process the charge", e);
        }
        return id;
    }
	
	
	/*****************************/
	public String createStripeCustomer(int idUser) {
		
		// stripe key
		Stripe.apiKey = secretKey;

		Users user = userRepo.findById((long) idUser).get();
		Map<String, Object> params = new HashMap<>();
		params.put("description", " descption ");
		params.put("email", user.getEmail());

		// affichage id du customer
		try {
			Customer customer = Customer.create(params);

			System.out.println("create customer id: {}");
			return customer.getId();
		} catch (StripeException e) {

			throw new RuntimeException(e);
		}
		// TODO Auto-generated method stub
//		return null;
	}
	/**************************/
	//public String createCustumorStripe(String customerId, String carta, String expMonth, String expYear, String cvc)
	public String createCustumorStripe(int idUser, String carta, String expMonth, String expYear, String cvc)
			throws StripeException {
		// TODO Auto-generated method stub
		//return null;
		// stripe key
		Stripe.apiKey = secretKey;
		String customerId;
		
		Users user = userRepo.findById((long) idUser).get();
		Map<String, Object> params = new HashMap<>();
		params.put("description", " descption ");
		params.put("email", user.getEmail());

		// affichage id du customer
		try {
			Customer customer = Customer.create(params);

			System.out.println("create customer id: {}");
			//return customer.getId();
			 customerId= new String(customer.getId());
		} catch (StripeException e) {

			throw new RuntimeException(e);
		}
		
		
		

		Customer customer = Customer.retrieve(customerId);

		Map<String, Object> cardParam = new HashMap<String, Object>();
		cardParam.put("number", carta);
		cardParam.put("exp_month", expMonth);
		cardParam.put("exp_year", expYear);
		cardParam.put("cvc", cvc);

		Map<String, Object> tokenParam = new HashMap<String, Object>();
		tokenParam.put("card", cardParam);

		Token token = Token.create(tokenParam);

		Map<String, Object> source = new HashMap<String, Object>();
		source.put("source", token.getId());

		customer.getSources().create(source);
		return token.getId();
	}
	
	/************************/
	public String paymentIntent(ChargeRequest chargerequest)throws StripeException{
		// TODO Auto-generated method stub
		//return null;
		// stripe key
		Stripe.apiKey = secretKey;

		// `source` is obtained with Stripe.js; see
		// https://stripe.com/docs/payments/accept-a-payment-charges#web-create-token
		List<String> paymentMethodTypes = new ArrayList();
		paymentMethodTypes.add("card");
		
		
		Map<String, Object> params = new HashMap<>();
		params.put("amount",chargerequest.getAmount());
		params.put("currency", chargerequest.getCurrency());
		params.put("description", chargerequest.getDescription());
		params.put("payment_method_types", paymentMethodTypes);
		
		PaymentIntent p = PaymentIntent.create(params);
		p.getId();
		//Charge charge = Charge.create(params);
		return p.getId();
		
		
		/*PaymentIntentCreateParams creatparm = new PaymentIntentCreateParams.Builder()
				.setCurrency("usd")
				.setAmount(10*100L)
				.build();
		PaymentIntent intent= PaymentIntent.create(creatparm);
		*///return new CreatePaymentResponse(intent.getClientSecret());
		
	}
	
	/**********************/
	/*
	 * this methode is to confirm that your customer intends to pay with current
	 * or provided payment method. Upon confirmation, the PaymentIntent will
	 * attempt to initiate a payment
	 */
	public PaymentIntent confirm(String id,long idCommande,int iduser) throws StripeException {
		Stripe.apiKey = secretKey;
		PaymentIntent paymentIntent = PaymentIntent.retrieve(id);
		
		Map<String, Object> params = new HashMap<>();
		params.put("payment_method", "pm_card_visa");
		// params.put("customer", "cus_H1OvsnwEn1KX36");
		Order c =orderRepo.getOne((long) idCommande);
		if(c.getUser().getId()==iduser)
		{
		paymentIntent.confirm(params);
		// commadeDAO.PayerEnLigne(idCommande, iduser);
		
		return paymentIntent;
		}
		return null;
		
	}
	/************************/
	
	
	
	@Transactional
	public void Pay(int idUser, String carta, int expMonth, int expYear, String cvc) throws AuthenticationException, InvalidRequestException, CardException, StripeException{
		//Order orders = orderRepo.CommandeencoursparClient(idUser);
		Order orders = orderRepo.findById((long) idUser).get();
		int nMontant = 0;
			if(//orders.getStatus().contentEquals("en cours"))
					orders.getOrderStatus().equals("NEW")); 
					{
				Map<String, Object> params = new HashMap<>();
		        Map<String, Object> tokenParams = new HashMap<>();
		        Map<String, Object> cardParams = new HashMap<>();
		        Stripe.apiKey = secretKey;
		        cardParams.put("number", carta);
		        cardParams.put("exp_month", expMonth);
		        cardParams.put("exp_year", expYear);
		        cardParams.put("cvc", cvc);
		        
		       
		        nMontant = (int) (orders.getAmount()*100);
		       
		        tokenParams.put("card", cardParams);
		        Token token =Token.create(tokenParams);
		      //  System.out.println(token.getCard().getId());
		        if (token.getId()!=null){
		        params.put("amount", nMontant);
		        params.put("description", "test de stipe");
		        params.put("currency", "eur");
		        params.put("source", token.getId());
		        Charge charge = Charge.create(params);
		       // orderRepo.PayerEnLigne(orders.getId());
		     
		        orders.setPaymentMethod(PaymentMethod.ONLINE);
		        orderRepo.save(orders);
	
}
			}
	}
	


}
