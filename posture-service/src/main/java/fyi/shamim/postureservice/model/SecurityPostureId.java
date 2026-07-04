package fyi.shamim.postureservice.model;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * Created by IntelliJ IDEA.
 * User: Md Shamim
 * Date: 6/27/26
 * Email: mdshamim723@gmail.com
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class SecurityPostureId implements Serializable {

    @Serial
    private static final long serialVersionUID = 123456789023412L;

    private String serviceId;
    private String environment;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SecurityPostureId id)) return false;
        return Objects.equals(serviceId, id.serviceId) && Objects.equals(environment, id.environment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceId, environment);
    }

}