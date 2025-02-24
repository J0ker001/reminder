package ru.anton.reminder.service;

public interface EmailService {
    void sendEmail(String to, String subject, String text);
}
