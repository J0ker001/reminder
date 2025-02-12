package ru.anton.reminder.service;

public interface EmailService {
    String sendEmail(String to, String subject, String text);
}
