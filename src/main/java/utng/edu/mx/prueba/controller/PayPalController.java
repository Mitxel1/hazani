package utng.edu.mx.prueba.controller;

import utng.edu.mx.prueba.model.PayPalTransaction;
import utng.edu.mx.prueba.service.PayPalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/paypal")
public class PayPalController {

    @Autowired
    private PayPalService payPalService;

    @PostMapping("/create-transaction")
    public ResponseEntity<PayPalTransaction> createTransaction(
            @RequestParam double amount,
            @RequestParam String currency
    ) {
        PayPalTransaction transaction = payPalService.createPayPalTransaction(amount, currency);
        return transaction != null
                ? ResponseEntity.ok(transaction)
                : ResponseEntity.badRequest().build();
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<PayPalTransaction>> listTransactions() {
        List<PayPalTransaction> transactions = payPalService.listPayPalTransactions();
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/transactions/{id}")
    public ResponseEntity<PayPalTransaction> getTransaction(@PathVariable String id) {
        PayPalTransaction transaction = payPalService.getTransactionDetails(id);
        return transaction != null
                ? ResponseEntity.ok(transaction)
                : ResponseEntity.notFound().build();
    }
}
