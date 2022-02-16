package lombok;

import com.fasterxml.jackson.annotation.*;

import java.util.List;

public class ResourceForReqres {
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    private List<Integer> id;
    private List<String> name;
    private List<Integer> year;
    @JsonProperty ("pantone_value")
    private List<String> pantoneValue;
}
