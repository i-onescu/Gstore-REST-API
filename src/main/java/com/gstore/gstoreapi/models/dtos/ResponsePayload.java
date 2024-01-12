package com.gstore.gstoreapi.models.dtos;

import lombok.Builder;

//record to be used when tailoring response for controllers
//contains a response and a corresponding status code
@Builder
public record ResponsePayload(Object response, String statusCode) { }