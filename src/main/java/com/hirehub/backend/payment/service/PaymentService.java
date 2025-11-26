//package com.hirehub.backend.payment.service;

//import com.culqi.api.Charge;
//import com.culqi.model.ChargeModel;
//import com.hirehub.backend.payment.dto.CreateChargeRequestDTO;
//import com.hirehub.backend.payment.dto.CreateChargeResponseDTO;
//import org.springframework.stereotype.Service;

//import java.util.HashMap;
//import java.util.Map;

//@Service
//public class PaymentService {

//   public CreateChargeResponseDTO createCharge(CreateChargeRequestDTO req) {
//       try {
//            Charge charge = new Charge();

//            Map<String, Object> map = new HashMap<>();
//           map.put("amount", req.amount);
//            map.put("currency_code", "PEN");
//           map.put("email", req.email);
//        map.put("source_id", req.sourceId);

//     ChargeModel chargeResult = charge.create(map);

//         return new CreateChargeResponseDTO(
//                   true,
//                  chargeResult.getId(),
//                   "Pago realizado correctamente."
//     );
//  } catch (Exception e) {
//      return new CreateChargeResponseDTO(
//            false,
//               null,
//                 "Error al procesar el pago: " + e.getMessage()
//         );
                    //   }
//  }
//}
