package de.youthclubstage.authserver.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleResponse {
    private Boolean success;

    @JsonProperty("error-codes")
    private String[] errorCodes;
}
