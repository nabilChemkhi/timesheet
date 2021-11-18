





import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

//import com.consomiTounsi.ChoppingTest;
import com.consomiTounsi.Repository.BillRepository;
import com.consomiTounsi.entities.Bill;
import com.consomiTounsi.services.StripeServices;

import org.apache.log4j.Logger;
import org.junit.*;

@RestController
public class StripRestController {
	
	@Autowired
	StripeServices stripeServ;
	@Autowired
	BillRepository billRepo;
	
	private static final Logger L = Logger.getLogger(StripRestController.class);

	
	//http://localhost:8081/consomiTounsi/createcharge/nbl/token/1/1234

	@RequestMapping(value="/createcharge/{email}/{token}/{companyid}/{amount}",method = RequestMethod.GET)
    @ResponseBody
    public String  createCharge(@PathVariable("email")String email,@PathVariable("token") String token, @PathVariable("companyid")Long companyid,@PathVariable("amount")  double amount)
    {
        L.info("Enter >> createCharge() ");

        if(token == null)
        {
            throw new RuntimeException("Can't create a charge, try again");
        }

        Bill billing = billRepo.findById(companyid).get(); //(Integer.parseInt(companyid));

        double billedAmount = amount * 100;

        String chargeId = stripeServ.createCharge(email,token,billedAmount);

        if(chargeId != null && !chargeId.equals(""))
        {
        	L.info("bill has been charged on consumer's account");
            billing.setDelivFees(1111111); //Status(true);
            //billing.setPaiddate(new Date());
            billRepo.save(billing);
        }

        L.info("Exit << createCharge() ");
       // return new Response(true,"Congratulations, your card has been charged, chargeId= "+chargeId);
        //return new String("Congratulations, your card has been charged, chargeId= "+chargeId);
        return new String("Congratulations! your payment was successful, chargeId= "+chargeId);

    }


}
