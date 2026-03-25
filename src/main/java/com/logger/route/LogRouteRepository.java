package com.logger.route;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRouteRepository extends JpaRepository<LogRoute, Long> {

    List<LogRoute> findByLevelsContaining(LogLevel level);
}
