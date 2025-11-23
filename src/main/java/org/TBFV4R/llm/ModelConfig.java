package org.TBFV4R.llm;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
public class ModelConfig {
    private String model;
    private Map<String, Object> reasoning;
    private String apiKey;

    private int maxRetries;


}
