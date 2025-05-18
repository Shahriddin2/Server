package uz.pdp.simple_l.entity;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.CurrentTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.generator.EventType;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public class Auditable {

    @CurrentTimestamp(event = EventType.INSERT, source = SourceType.VM)
    LocalDateTime createdAt;

    @CurrentTimestamp(event = EventType.UPDATE, source = SourceType.VM)
    LocalDateTime updatedAt;
}
