package kopo.poly.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CovidDTO {
    private String mmddhh;
    private String cnt_deaths;
    private String rate_deaths;
    private String cnt_severe_symptoms;
    private String rate_severe_symptoms;
    private String cnt_hospitalizations;
    private String rate_hospitalizations;
    private String cnt_confirmations;
    private String rate_confirmations;
}
