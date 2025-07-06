package com.events.api.adapters.outbound.repositories;

import com.events.api.adapters.outbound.entities.JpaEventEntity;
import com.events.api.domain.event.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface JpaEventRepository extends JpaRepository<JpaEventEntity, UUID> {

    @Query("SELECT e FROM Event e LEFT JOIN FETCH e.address a WHERE e.date >= :currentDate")
    public Page<Event> findUpcomingEvents(@Param("currentDate")Date currentDate, Pageable pageable);

    @Query("SELECT e.id AS id, e.title AS title, e.description AS description, e.date AS date, e.imgUrl AS imgUrl, e.eventUrl AS eventUrl, e.remote AS remote, a.city AS city, a.uf AS uf " +
            "FROM Event e JOIN Address a ON e.id = a.event.id " +
            "WHERE (:city = '' OR a.city LIKE %:city%) " +
            "AND (:uf = '' OR a.uf LIKE %:uf%) " +
            "AND (e.date >= :startDate AND e.date <= :endDate)")
    Page<Event> findFilteredEvents(@Param("city") String city,
                                                    @Param("uf") String uf,
                                                    @Param("startDate") Date startDate,
                                                    @Param("endDate") Date endDate,
                                                    Pageable pageable);

    @Query("SELECT e.id AS id, e.title AS title, e.description AS description, e.date AS date, e.imgUrl AS imgUrl, e.eventUrl AS eventUrl, e.remote AS remote, a.city AS city, a.uf AS uf " +
            "FROM Event e JOIN Address a ON e.id = a.event.id " +
            "WHERE (:title = '' OR e.title LIKE %:title%)")
    List<Event> findEventsByTitle(@Param("title") String title);
}
