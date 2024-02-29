package com.gstore.gstoreapi.controllers.util;

import com.gstore.gstoreapi.models.dtos.ResponsePayload;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@UtilityClass
public final class ResponseBuilderHelper {

    public static ResponseEntity<ResponsePayload> buildResponsePayload(Object response, HttpStatus status) {
        return new ResponseEntity<>(ResponsePayload.builder()
                .response(response)
                .statusCode(status.name())
                .build(), status);
    }

}
