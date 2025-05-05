package re.ermix.school_app;

import org.springframework.boot.SpringApplication;
import re.ermix.school_app.config.TestcontainersConfiguration;

public class TestSchoolAppApplication {

	public static void main(String[] args) {
		SpringApplication.from(SchoolAppApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
