package fyi.shamim.postureservice.model;

import fyi.shamim.postureservice.converter.MapJsonbConverter;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.Map;

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
@Entity
@Table(name = "security_posture")
@IdClass(SecurityPostureId.class)
public class SecurityPostureEntity {

    @Id
    @Column(name = "service_id", nullable = false, length = 80)
    private String serviceId;

    @Id
    @Column(name = "environment", nullable = false, length = 16)
    private String environment; // dev|stage|prod

    @Column(name = "internet_facing", nullable = false)
    private boolean internetFacing;

    @Column(name = "authn", columnDefinition = "jsonb")
    @Convert(converter = MapJsonbConverter.class)
    private Map<String, Object> authn;

    @Column(name = "data", columnDefinition = "jsonb")
    @Convert(converter = MapJsonbConverter.class)
    private Map<String, Object> data;

    @Column(name = "tls", columnDefinition = "jsonb")
    @Convert(converter = MapJsonbConverter.class)
    private Map<String, Object> tls;

    @Column(name = "network", columnDefinition = "jsonb")
    @Convert(converter = MapJsonbConverter.class)
    private Map<String, Object> network;

    @Column(name = "secrets", columnDefinition = "jsonb")
    @Convert(converter = MapJsonbConverter.class)
    private Map<String, Object> secrets;

    @Column(name = "vulns", columnDefinition = "jsonb")
    @Convert(converter = MapJsonbConverter.class)
    private Map<String, Object> vulns;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt = Instant.now();

}