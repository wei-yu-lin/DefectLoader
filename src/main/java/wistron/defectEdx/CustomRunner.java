package wistron.defectEdx;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

public abstract class CustomRunner implements ApplicationRunner{
	protected String getOptionValue(ApplicationArguments args, String key) {
		if (args.getOptionValues(key)==null) {
			return null;
		}
		return args.getOptionValues(key).get(0);
	}
}
