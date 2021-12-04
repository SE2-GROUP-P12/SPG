package it.polito.SE2.P12.SPG.emailServer;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class Feedback {

    @NotNull
    private String name;

    @NotNull
    @Email()
    private String email;

    @NotNull
    @Min(10)
    private String feedback;


}
