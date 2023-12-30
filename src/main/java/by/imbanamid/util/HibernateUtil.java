package by.imbanamid.util;

import by.imbanamid.converter.BirthdayConverter;
import org.hibernate.SessionFactory;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.cfg.Configuration;


public class HibernateUtil {
    public static SessionFactory buildSessionFactory() {
        Configuration configuration = new Configuration().configure();
        configuration.configure();
        configuration.addAttributeConverter(new BirthdayConverter());
        configuration.setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy()); //conversion from user_id to userId
        return configuration.buildSessionFactory();
    }

}
