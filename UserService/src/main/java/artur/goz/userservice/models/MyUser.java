package artur.goz.userservice.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import javax.validation.constraints.*;
import java.util.Date;
import java.util.Set;

@NoArgsConstructor
@Data
@Entity
public class MyUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Name cannot be null")
    @NotBlank(message = "Name is mandatory")
    @Size(min = 5, max = 25, message = "Name must be between 5 and 25 characters")
    private String name;

    @NotNull(message = "Email cannot be null")
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    private String email;


    @NotNull(message = "Password cannot be null")
    @NotBlank(message = "Password is mandatory")
    @Size(min = 5, message = "Password must be at least 5 characters long")
    private String password;

    @JsonIgnore
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> roles;

    private Integer monthlyLimit; // Поточний ліміт використання кнопки


    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    private Date registeredAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastActiveAt;

    @PrePersist
    protected void onCreate() {
        this.registeredAt = new Date();
        this.lastActiveAt = new Date();
        this.monthlyLimit = 10;
    }

    @PreUpdate
    protected void onUpdate() {
        this.lastActiveAt = new Date();
    }
}

