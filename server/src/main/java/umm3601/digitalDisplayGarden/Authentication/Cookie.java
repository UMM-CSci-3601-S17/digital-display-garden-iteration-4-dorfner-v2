package umm3601.digitalDisplayGarden.Authentication;

import java.util.Date;

public class Cookie {
    public String name;
    public String value;
    public int max_age;
    public Date createDate;

    public Cookie(String name, String value, int max_age){
        this.name = name;
        this.value = value;
        this.max_age = max_age;
        this.createDate = new Date();
    }
}


