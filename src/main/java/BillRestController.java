

	import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.springframework.core.io.InputStreamResource;


import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.http.HttpHeaders;
	import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
	import org.springframework.web.bind.annotation.PathVariable;
	import org.springframework.web.bind.annotation.RequestMapping;
	import org.springframework.web.bind.annotation.RequestMethod;
	import org.springframework.web.bind.annotation.RestController;

import com.consomiTounsi.Repository.OrderRepository;
import com.consomiTounsi.entities.Bill;
import com.consomiTounsi.entities.Order;
import com.consomiTounsi.services.BillServicesImpl;
import com.consomiTounsi.services.OrederServicesImpl;
	import com.consomiTounsi.util.GeneratePdfReport;



@RestController
public class BillRestController {
	
	
	
	@Autowired
    private BillServicesImpl billServ;
	@Autowired
	private OrederServicesImpl orderService;
	@Autowired
	private OrderRepository orderRepo;
	
	//@Autowired
	//ServletContext context;
	
	//http://localhost:8081/consomiTounsi/generateBillPdf/1
	@PreAuthorize("hasRolr('USER')")
	@RequestMapping( value ="/generateBillPdf/{idOrder}", method = RequestMethod.GET,produces = MediaType.APPLICATION_PDF_VALUE)
	//@GetMapping(value ="/generateBillPdf/{idOrder}",produces = MediaType.APPLICATION_PDF_VALUE)
	//*@GetMapping("/generateBillPdf/{idOrder}")
   //* public String FactureReport(@PathVariable("idOrder") long idOrder)
    public ResponseEntity<InputStreamResource>  FactureReport(@PathVariable("idOrder") long idOrder)                         
	{
		 Order order  = orderRepo.findById(idOrder).get();
         Bill bill = billServ.findBillByOrder(order);
         ByteArrayInputStream bis = GeneratePdfReport.FactureReport(bill);
         HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=Bill.pdf");
        
         int size = bis.available();
        char[] theChars = new char[size];
        byte[] bytes = new byte[size];
        bis.read(bytes, 0, size);
        for (int i = 0; i < size;)
          theChars[i] = (char) (bytes[i++] & 0xff);
   
        File convertFile = new File("./uploads//Bill.pdf");
		try {
			convertFile.createNewFile();
			FileOutputStream fout = new FileOutputStream(convertFile);
			fout.write(bytes);
			fout.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
       // return new String(theChars);
        
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));//bis
       
    }
	

}
