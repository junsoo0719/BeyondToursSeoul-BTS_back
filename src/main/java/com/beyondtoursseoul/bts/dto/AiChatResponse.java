package com.beyondtoursseoul.bts.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AiChatResponse {
    private String answer;
    private JsonNode structured;
    private String model;
}
