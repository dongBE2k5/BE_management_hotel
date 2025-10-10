package tdc.vn.managementhotel.controllerAPI;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.websocket.server.PathParam;
import tdc.vn.managementhotel.config.ZaloPayConfig;
import tdc.vn.managementhotel.dto.PaymentDTO.PaymentResponseDTO;
import tdc.vn.managementhotel.service.PaymentService;
import tdc.vn.managementhotel.service.VNPayService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/pay")
public class ControllerPayAPI {

    private final ZaloPayConfig zaloPayService;

    @Autowired
    private VNPayService vnPayService;
    @Autowired
    private PaymentService  paymentService;

    public ControllerPayAPI(ZaloPayConfig zaloPayService) {
        this.zaloPayService = zaloPayService;
    }

    @PostMapping("/zalo")
    public ResponseEntity<String> createOrder(@RequestParam long amount) {
        try {
            String response = zaloPayService.createOrder(amount);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }



	@GetMapping("/index")
	public ResponseEntity<?> get (){
		return ResponseEntity.ok("ok");
	}
	
	@PostMapping("/createpay")
	public ResponseEntity<?> createPay(@RequestParam("amount") int orderTotal,
			@RequestParam("orderInfo") String orderInfo,
                                       @RequestParam("method") String method, HttpServletRequest request) {
	    String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
		String vnpayUrl = vnPayService.createOrder(orderTotal, orderInfo, baseUrl);
        PaymentResponseDTO paymentResponseDTO = new PaymentResponseDTO(null ,method,(long)orderTotal,"Wait for payment",Long.parseLong(orderInfo),null);
        paymentService.createPay(paymentResponseDTO);
		return ResponseEntity.ok(Map.of("paymentUrl", vnpayUrl));

	}

    @GetMapping("/successpay")
    public ResponseEntity<?> successPay(
            HttpServletRequest request) {
        int paymentStatus =vnPayService.orderReturn(request);
        String orderInfo = request.getParameter("vnp_OrderInfo");
        String paymentTime = request.getParameter("vnp_PayDate");
        String transactionId = request.getParameter("vnp_TransactionNo");
        String totalPrice = request.getParameter("vnp_Amount");
        PaymentResponseDTO paymentResponseDTO = new PaymentResponseDTO(null , null, Long.parseLong(totalPrice), "Wait for payment", Long.parseLong(orderInfo), String.valueOf(paymentStatus));

        return paymentService.updatePay(paymentResponseDTO).hasBody() ? ResponseEntity.ok(paymentStatus): ResponseEntity.status(500).body(paymentResponseDTO);

    }


}
