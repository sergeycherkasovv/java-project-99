package hexlet.code.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import org.openapitools.jackson.nullable.JsonNullable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateDTO {

    private JsonNullable<String> firstName;


    private JsonNullable<String> lastName;

    @Email
    private JsonNullable<String> email;

    @Size(min = 3, max = 100)
    private JsonNullable<String> password;

}
