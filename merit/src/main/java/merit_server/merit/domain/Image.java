package merit_server.merit.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@Getter
public class Image {

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String fileOriName;

    @Column(nullable = false)
    private String fileUrl;
}