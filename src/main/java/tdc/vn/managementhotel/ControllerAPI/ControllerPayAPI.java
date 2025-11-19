package tdc.vn.managementhotel.controllerAPI;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.websocket.server.PathParam;
import org.springframework.web.servlet.view.RedirectView;
import tdc.vn.managementhotel.config.GlobalStore;
import tdc.vn.managementhotel.config.ZaloPayConfig;
import tdc.vn.managementhotel.dto.ApiResponse;
import tdc.vn.managementhotel.dto.BookingDTO.ChangeBookingStatusRequestDTO;
import tdc.vn.managementhotel.dto.PaymentDTO.PaymentResponseDTO;
import tdc.vn.managementhotel.entity.HostHotel;
import tdc.vn.managementhotel.entity.Hotel;
import tdc.vn.managementhotel.enums.BookingStatus;
import tdc.vn.managementhotel.repository.HostHotelRepository;
import tdc.vn.managementhotel.repository.HotelRepository;
import tdc.vn.managementhotel.service.BookingService;
import tdc.vn.managementhotel.service.PaymentService;
import tdc.vn.managementhotel.service.VNPayService;


@RestController
@RequestMapping("/api/pay")
@RequiredArgsConstructor
@CrossOrigin(originPatterns = "*", allowedHeaders = "*")
public class ControllerPayAPI {

    private final GlobalStore globalStore;

    private final ZaloPayConfig zaloPayService;


    private final VNPayService vnPayService;

    private  final PaymentService  paymentService;

    private final BookingService bookingService;

    private final SimpMessagingTemplate messagingTemplate;
    private final HotelRepository hotelRepository;
    private final HostHotelRepository hostHotelRepository;


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
                                       @RequestParam("method") String method,
                                       @RequestParam("ip") String ip, HttpServletRequest request) {
	    String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
		String vnpayUrl = vnPayService.createOrder(orderTotal, orderInfo, baseUrl);
        PaymentResponseDTO paymentResponseDTO = new PaymentResponseDTO(null ,method.toUpperCase(Locale.ROOT),(long)orderTotal,"Wait for payment",Long.parseLong(orderInfo),null);
        paymentService.createPay(paymentResponseDTO);

        globalStore.setValue(String.valueOf(orderInfo),ip);
        System.out.println("test"+globalStore.getValue(orderInfo));
		return ResponseEntity.ok(Map.of("paymentUrl", vnpayUrl));

	}

    @GetMapping("/successpay")
    public RedirectView successPay(
            HttpServletRequest request, HttpServletResponse response) throws IOException {
//        String fullUrl = request.getRequestURL().toString();
//        String queryString = request.getQueryString();
//
//        if (queryString != null) {
//            fullUrl += "?" + queryString;
//        }
//        System.out.println("Full URL: " + fullUrl);

        String paymentStatus =vnPayService.orderReturn(request);
        String orderInfo = request.getParameter("vnp_OrderInfo");
        String paymentTime = request.getParameter("vnp_PayDate");
        String transactionId = request.getParameter("vnp_TransactionNo");
        String totalPrice = request.getParameter("vnp_Amount");

        String deepLink = "exp://"+ globalStore.getValueAndClear(orderInfo) +":8081?status=" + paymentStatus;
//        System.out.println(deepLink);
//        System.out.println(globalStore.getValue(orderInfo));
        String status = "fail";
        if (paymentStatus.equals("00")){
            ChangeBookingStatusRequestDTO dto= new ChangeBookingStatusRequestDTO(Long.parseLong(orderInfo), BookingStatus.DA_THANH_TOAN,null);
            bookingService.updateStatus(dto);
            System.out.println("cập nhật thành công");
            status = "success";
            Map<String, Object> payload = new HashMap<>();
            payload.put("message", "Payment success booking");
            payload.put("bookingId", orderInfo);

            messagingTemplate.convertAndSend("/topic/booking", payload);
        }
        PaymentResponseDTO paymentResponseDTO = new PaymentResponseDTO(null , null, Long.parseLong(totalPrice), status, Long.parseLong(orderInfo), String.valueOf(paymentStatus));
        paymentService.updatePay(paymentResponseDTO);

        return new RedirectView (deepLink);

    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.getById(id));
    }

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<List<PaymentResponseDTO>> getByBooking(@PathVariable Long bookingId) {
        return ResponseEntity.ok(paymentService.getByBookingId(bookingId));
    }

    @GetMapping
    public ResponseEntity<List<PaymentResponseDTO>> getAll() {
        return ResponseEntity.ok(paymentService.getAll());
    }

    @PostMapping("/createpaymanual")
    public ResponseEntity<ApiResponse<PaymentResponseDTO>> manualpayment(@RequestParam("amount") int orderTotal,
                                                                         @RequestParam("orderInfo") String orderInfo,
                                                                         @RequestParam("method") String method){
        PaymentResponseDTO paymentResponseDTO = new PaymentResponseDTO(null ,method,(long)orderTotal,"success",Long.parseLong(orderInfo),null);
        paymentService.createPay(paymentResponseDTO);

        return ResponseEntity.ok(ApiResponse.success("Thanh toán thành công",paymentResponseDTO));
    }

    @PostMapping("/createpayqr")
    public ResponseEntity<ApiResponse<String>> qrpayment(@RequestParam("amount") int orderTotal,
                                                                         @RequestParam("orderInfo") String orderInfo,
                                                                         @RequestParam("method") String method,
                                                                     @RequestParam("hotelId") Long hotelId){
        Hotel hotel= hotelRepository.findById(hotelId).orElseThrow(()-> new EntityNotFoundException("Hotel not found"));
        HostHotel hostHotel= hostHotelRepository.findByUserId(hotel.getUser().getId()).orElseThrow(()-> new EntityNotFoundException("Host hotel not found"));
        String url = "https://img.vietqr.io/image/"+hostHotel.getNganHang()+"-"+hostHotel.getStk()+"-compact2.png?amount="+orderTotal+"&addInfo="+orderInfo+"&accountName="+hostHotel.getUser().getFullName();

//
        PaymentResponseDTO paymentResponseDTO = new PaymentResponseDTO(null ,method,(long)orderTotal,"Wait for payment",Long.parseLong(orderInfo),null);
        paymentService.createPay(paymentResponseDTO);



        return ResponseEntity.ok(ApiResponse.success("Thanh toán thành công",url));
    }
}
