package by.imbanamid;

import by.imbanamid.entity.Birthday;
import by.imbanamid.entity.PersonalInfo;
import by.imbanamid.entity.Role;
import by.imbanamid.entity.User;
import by.imbanamid.util.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

@Slf4j
public class HibernateRunner {


    public static void main(String[] args) {
        //Transient
        User user = User.builder()
                .username("test2@mail.ru")
                .personalInfo(PersonalInfo.builder()
                        .firstname("Dima")
                        .lastname("Ivanov")
                        .birthDate(new Birthday(LocalDate.of(2000, 10, 23)))
                        .build())
                .role(Role.USER)
                .build();


        //Transient
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            try (Session session1 = sessionFactory.openSession()) {
                session1.beginTransaction();

                //Present -> session1
                session1.saveOrUpdate(user);

                log.info("User: {} , session: {}", user, session1);


                session1.getTransaction().commit();
            }catch (Exception e){
                log.error("Exception error: ", e);
            }

        }
    }
}
