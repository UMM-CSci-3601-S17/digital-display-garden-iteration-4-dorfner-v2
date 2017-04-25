package umm3601.digitalDisplayGarden.Authentication;


public class Cookie {
    public String name;
    public String value;
    public int max_age;

    public Cookie(String name, String value, int max_age){
        this.name = name;
        this.value = value;
        this.max_age = max_age;
    }
}


