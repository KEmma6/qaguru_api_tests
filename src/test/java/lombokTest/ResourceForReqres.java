package lombokTest;

import com.fasterxml.jackson.annotation.*;

import java.util.List;

@lombok.Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResourceForReqres {
    private List<Datum> data;
    @JsonProperty ("pantone_value")
    private List<String> pantoneValue;
}



