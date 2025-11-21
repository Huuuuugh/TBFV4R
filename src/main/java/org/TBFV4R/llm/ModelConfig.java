package org.TBFV4R.llm;

import java.util.Map;

public class ModelConfig {
    private String model;
    private Map<String, Object> reasoning;
    private String apiKey;

    public int getMaxRetries() {
        return maxRetries;
    }

    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    private int maxRetries;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public Map<String, Object> getReasoning() {
        return reasoning;
    }

    public void setReasoning(Map<String, Object> reasoning) {
        this.reasoning = reasoning;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }


}
