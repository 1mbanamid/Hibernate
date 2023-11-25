package by.imbanamid.entity;

import by.imbanamid.converter.BirthdayConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users",schema = "public")
public class User {
    private String username;
    @AttributeOverride(name = "birthDate", column =  @Column(name = "birth_date"))
    @EmbeddedId
    private PersonalInfo personalInfo;


    @Enumerated(EnumType.STRING)
    private Role role;

}
