package ru.anton.reminder.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.anton.reminder.entity.Reminder;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder, Long> {
    List<Reminder> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrRemind(
            String title, String description, LocalDateTime remind);

    List<Reminder> findAllByOrderByTitleAsc();

    @Query("SELECT e FROM Reminder e ORDER BY CAST(e.remind as localtime ) ASC")
    List<Reminder> findAllByOrderByRemindAsc();

    @Query("SELECT e FROM Reminder e ORDER BY FUNCTION('DATE', e.remind) ASC")
    List<Reminder> findAllByOrderByRemindDateAsc();

    @Query("SELECT e FROM Reminder e WHERE DATE(e.remind) BETWEEN :startDate AND :endDate")
    List<Reminder> findByDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT e FROM Reminder e WHERE CAST(e.remind as localtime) BETWEEN :startTime AND :endTime")
    List<Reminder> findByTimeBetween(@Param("startTime") LocalTime start, @Param("endTime") LocalTime end);

    Page<Reminder> findAll(Pageable pageable);

    @Query("SELECT e FROM Reminder e WHERE DATE(e.remind) =:date")
    Page<Reminder> findByRemindDay(@Param("date") LocalDate date, Pageable pageable);

    List<Reminder> findByRemindBefore(LocalDateTime now);
}
