package by.imbanamid;

import by.imbanamid.entity.*;
import by.imbanamid.util.HibernateUtil;
import lombok.Cleanup;
import org.hibernate.Session;
import org.junit.Test;

import javax.persistence.Column;
import javax.persistence.Table;
import java.sql.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;
import java.lang.reflect.Field;
import java.util.stream.Collectors;

public class HibernateRunnerTest {
    @Test
    public void checkManyToMany(){
        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        Chat chat = session.get(Chat.class,1L);
        User user = session.get(User.class,11L);
        UserChat userChat = UserChat.builder()
                .createdAt(Instant.now())
                .createdBy("")
                .build();

        userChat.setChat(chat);
        userChat.setUser(user);

        session.save(userChat);

        session.getTransaction().commit();
    }

    @Test
    public void checkOneToOne() {
        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();
        User user = User.builder()
                .username("test11@mail.ru")
                .build();
        Profile profile = Profile.builder()
                .language("EN")
                .street("Znievska 5")
                .build();
        session.save(user);
        profile.setUser(user);

        session.getTransaction().commit();
        session.save(profile);
    }

    @Test
    public void checkOrphalRemove() {
        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        Company company = session.get(Company.class, 9);
        company.getUsers().removeIf(user -> user.getId().equals(2));

        session.getTransaction().commit();

    }

    @Test
    public void addNewUserAndCompany() {
        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();


        Company company = Company.builder()
                .name("Test")
                .build();

        User user = User.builder()
                .username("test6@mail.ru")
                .build();

        company.addUser(user);

        session.save(company);

        session.getTransaction().commit();

    }

    @Test
    public void checkOneToMany() {
        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();
        session.getTransaction().commit();

        var company = session.get(Company.class, 9);

        System.out.println(company.getUsers());

    }


    public void testHibernateApi() throws SQLException, IllegalAccessException {

    }
    /*

        var user = User.builder()
                .username("ivan2@mail.ru")
                .firstname("ivan")
                .lastname("Ivanov")
                .birthDate(new Birthday(
                        LocalDate.of(2000, 10, 23)))
                .build();

        var sql = """
                insert into
                %s
                (%s)
                values
                (%s)
                """;

        var tableName = Optional.ofNullable(user.getClass().getAnnotation(Table.class))
                .map(table -> table.schema() + "." + table.name())
                .orElse(user.getClass().getName());

        Field[] fields = user.getClass().getDeclaredFields();

        var columnName = Arrays.stream(fields)
                .map(field -> Optional.ofNullable(field.getAnnotation(Column.class))
                        .map(Column::name)
                        .orElse(field.getName())

                ).collect(Collectors.joining(", "));
        var columnValues = Arrays.stream(fields)
                .map(field -> "?")
                .collect(Collectors.joining(", "));

        Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5433/postgres",
                "postgres", "123");
        PreparedStatement preparedStatement = connection
                .prepareStatement(sql.formatted(tableName, columnName, columnValues));
        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            preparedStatement.setObject(i + 1, fields[i].get(user));
        }
        preparedStatement.executeUpdate();
        preparedStatement.close();
        connection.close();

    }

     */
}