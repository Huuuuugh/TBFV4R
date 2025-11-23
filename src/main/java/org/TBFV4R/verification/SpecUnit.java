package org.TBFV4R.verification;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.TBFV4R.utils.FileUtil;

import java.io.IOException;
import java.util.List;

public class SpecUnit {

    // Getter & Setter
    @Getter
    @Setter
    private String program = "";

    // TDs T&D
    @Getter
    @Setter
    @JsonProperty("T")
    private String T;

    @Getter
    @Setter
    @JsonProperty("D")
    private String D;

    @Setter
    @Getter
    @JsonProperty("pre_constrains")
    private List<String> preConstrains;

    @JsonIgnore
    private final ObjectMapper MAPPER = new ObjectMapper();

    // Full constructor
    public SpecUnit(String program, String T, String D, List<String> preConstrains) {
        this.program = program;
        this.T = T;
        this.D = D;
        this.preConstrains = preConstrains;
    }

    // Constructor: only codePath
    public SpecUnit(String codePath) {
        try {
            this.program = FileUtil.readLinesAsString(codePath,"\n");
        } catch (IOException e) {
            throw new RuntimeException("File "+ codePath + " Not Found!");
        }
    }

    // No-arg constructor
    public SpecUnit() {
    }

    // Provided method for compatibility
    public List<String> getPreconditions() {
        return preConstrains;
    }

    // Serialize to JSON
    public String toJson() {
        try {
            return MAPPER.writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
