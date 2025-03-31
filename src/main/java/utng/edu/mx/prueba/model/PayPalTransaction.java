package utng.edu.mx.prueba.model;


import lombok.Getter;
import lombok.Setter;
import java.util.Objects;
import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
@Setter
public class PayPalTransaction {
    private String transactionId;
    private double amount;
    private String currency;
    private String status;
    private Date createdAt;

    // Constructor vacío existente está bien
    public PayPalTransaction() {}

    // Constructor adicional para facilitar la creación
    public PayPalTransaction(String transactionId, double amount, String currency, String status) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.currency = currency;
        this.status = status;
    }

    @Override
    public String toString() {
        // Mejorar el formato del toString
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return "PayPalTransaction{" +
                "transactionId='" + transactionId + '\'' +
                ", amount=" + String.format("%.2f", amount) +
                ", currency='" + currency + '\'' +
                ", status='" + status + '\'' +
                ", createdAt=" + (createdAt != null ? sdf.format(createdAt) : "N/A") +
                '}';
    }

    // Método equals para comparación
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PayPalTransaction that = (PayPalTransaction) o;
        return Double.compare(that.amount, amount) == 0 &&
                Objects.equals(transactionId, that.transactionId) &&
                Objects.equals(currency, that.currency) &&
                Objects.equals(status, that.status);
    }

    // Método hashCode para uso en colecciones
    @Override
    public int hashCode() {
        return Objects.hash(transactionId, amount, currency, status);
    }
}