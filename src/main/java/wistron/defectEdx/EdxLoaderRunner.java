package wistron.defectEdx;


import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import wistron.defectEdx.processEdx.ProcessEdx;
import wistron.defectEdx.repository.defectEdx.DefectEdxPreDataRepository;

@Component
@Lazy
@ConditionalOnProperty(name = { "task" }, havingValue = "EDXLoaderRunner")
@Slf4j
public class EdxLoaderRunner extends CustomRunner {
	
	@Value ("${app.version:unknown}")
	private String version;
	@Autowired
	private ProcessEdx processEdx;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		
		log.info("Start DefectSsaMapRunner, the version is {}", version);
		processEdx.process();
		
	}
}
