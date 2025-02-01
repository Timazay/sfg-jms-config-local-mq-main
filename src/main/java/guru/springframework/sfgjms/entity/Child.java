package guru.springframework.sfgjms.entity;



import guru.springframework.sfgjms.entity.state.ChildDay;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttributes;
import javax.persistence.*;


@Data
@NoArgsConstructor // java -jar FamilyProj-0.0.1-SNAPSHOT.jar --server.port=8081
@Entity
@Table(name = "children")
public class Child {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String surname;
    private int age;
   @Enumerated(EnumType.STRING)
    public ChildDay day;

    public Attributes toAttributes() {
        Attributes attributes = new BasicAttributes();
        attributes.put("objectClass","inetOrgPerson");
        attributes.put("cn", name);
        attributes.put("sn", surname);
        return attributes;
    }
}