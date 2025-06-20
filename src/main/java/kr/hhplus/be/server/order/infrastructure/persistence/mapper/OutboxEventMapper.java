package kr.hhplus.be.server.order.infrastructure.persistence.mapper;

import kr.hhplus.be.server.order.domain.model.OutboxEvent;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OutboxEventMapper {
    int insertOutboxEvent(OutboxEvent outboxEvent);
    void updateOutboxEventStatus(OutboxEvent outboxEvent);
}
