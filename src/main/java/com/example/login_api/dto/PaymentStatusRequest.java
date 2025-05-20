package com.example.login_api.dto;

import com.example.login_api.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class PaymentStatusRequest {

    private boolean pedidoConfirmado;
    private Long paymentId;
    private PaymentStatus status;

    public PaymentStatus getStatus(){
        return status;}
    public void setStatus(PaymentStatus status){
        this.status = status;
    }
}
