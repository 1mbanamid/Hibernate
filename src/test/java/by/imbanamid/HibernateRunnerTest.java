package by.imbanamid;

import by.imbanamid.entity.Birthday;
import by.imbanamid.entity.User;
import org.junit.Test;

import javax.persistence.Column;
import javax.persistence.Table;
import java.sql.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;
import java.lang.reflect.Field;
import java.util.stream.Collectors;
public class HibernateRunnerTest {
    @Test
    public void testHibernateApi() throws SQLException, IllegalAccessException {

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

}