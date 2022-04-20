package lombokTest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Datum {
    public Integer id;
    public String email;
    public String first_name;
    public String last_name;
    public String avatar;
}
