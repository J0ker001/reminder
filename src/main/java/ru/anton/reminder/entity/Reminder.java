package ru.anton.reminder.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
@Table(name = "reminders")
public class Reminder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title")
    private String title;
    @Column(name = "description")
    private String description;
    @Column(name = "remind", nullable = false)
    private LocalDateTime remind;
    @Column(name = "user_id")
    private Long user_id;
    @Column(name = "is_sent", nullable = false)
    private boolean isSent = false;
}
