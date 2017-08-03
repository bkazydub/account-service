import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import test.pack.AppConfig;

public class Main {

    public static void main(String[] args) throws Exception {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
    }
}
