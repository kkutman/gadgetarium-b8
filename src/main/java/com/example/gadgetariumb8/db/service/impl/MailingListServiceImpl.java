package com.example.gadgetariumb8.db.service.impl;

import com.example.gadgetariumb8.db.dto.request.MailingListRequest;
import com.example.gadgetariumb8.db.dto.request.MailingListSubscriberRequest;
import com.example.gadgetariumb8.db.dto.response.SimpleResponse;
import com.example.gadgetariumb8.db.exception.exceptions.MessageSendingException;
import com.example.gadgetariumb8.db.model.MallingList;
import com.example.gadgetariumb8.db.model.MallingListSubscriber;
import com.example.gadgetariumb8.db.repository.MailingListRepository;
import com.example.gadgetariumb8.db.repository.MailingListSubscriberRepository;
import com.example.gadgetariumb8.db.service.MailingListService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MailingListServiceImpl implements MailingListService {

    private final MailingListRepository mailingListRepository;
    private final MailingListSubscriberRepository subscriberRepository;
    private final JavaMailSender javaMailSender;
    private final Configuration config;

    @Override
    public SimpleResponse sendEmail(MailingListRequest mail) {
        List<MallingListSubscriber> subscriberList = subscriberRepository.findAll();

        Map<String, Object> model = new HashMap<>();
        model.put("description", mail.description());
        model.put("image", mail.image());
        model.put("name", mail.name());
        model.put("start", mail.dateOfStart());
        model.put("finish", mail.dateOfFinish());

        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            Template template = config.getTemplate("email-template.html");
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);

            for (MallingListSubscriber subscriber : subscriberList) {
                mimeMessageHelper.setTo(subscriber.getUserEmail());
                mimeMessageHelper.setText(html, true);
                mimeMessageHelper.setSubject("Gadgetarium");
                mimeMessageHelper.setFrom("ilyazovorozali08@gmail.com");
                javaMailSender.send(message);
            }
        } catch (IOException | TemplateException | MessagingException e) {
            throw new MessageSendingException("Ошибка при отправке сообщения!");
        }

        MallingList mallingList = MallingList.builder()
                .image(mail.image())
                .name(mail.name())
                .description(mail.description())
                .dateOfStart(mail.dateOfStart())
                .dateOfFinish(mail.dateOfFinish())
                .build();

        mailingListRepository.save(mallingList);
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("Сообщение успешно отправлено всем подписчикам.")
                .build();
    }

    @Override
    public SimpleResponse subscribe(MailingListSubscriberRequest subscriber) {
        MallingListSubscriber mallingListSubscriber = MallingListSubscriber.builder()
                .userEmail(subscriber.userEmail())
                .build();

        subscriberRepository.save(mallingListSubscriber);
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.CREATED)
                .message("Подписчик успешно добавлен в базу данных.")
                .build();
    }

}