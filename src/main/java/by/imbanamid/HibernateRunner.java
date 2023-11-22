package by.imbanamid;

import by.imbanamid.entity.Birthday;
import by.imbanamid.entity.Role;
import by.imbanamid.entity.User;
import by.imbanamid.converter.BirthdayConverter;
import org.hibernate.cfg.Configuration;

import java.time.LocalDate;

public class HibernateRunner {
    public static void main(String[] args) {
        Configuration configuration = new Configuration();
        configuration.configure();
        configuration.addAttributeConverter(new BirthdayConverter(), true);
        try (var sessionFactory = configuration.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();
            User user = User.builder()
                    .username("ivan@mail.ru")
                    .firstname("ivan")
                    .lastname("Ivanov")
                    .birthDate(
                            new Birthday( LocalDate.of(2000, 10, 23)))
                    .role(Role.USER)
                    .build();

            session.save(user);

            session.getTransaction().commit();
        }
    }
}
