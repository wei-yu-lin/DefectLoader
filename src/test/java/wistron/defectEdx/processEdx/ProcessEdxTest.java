package wistron.defectEdx.processEdx;

import java.util.List;

import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import wistron.defectEdx.db.defectEdxDataTable.DefectEdxData;
import wistron.defectEdx.repository.defectEdx.DefectEdxRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ProcessEdxTest {

//	@Autowired
//	private ProcessEdx processEdx;
	@SneakyThrows
	@Test
	public void testProcess() {
//		FTPClient ftpClient = new FTPClient();
//		ftpClient.connect("10.11.108.73");
//		ftpClient.login("wistron\\smg_arc231", "Date0717");
//		InputStream imageFile = ftpClient.retrieveFileStream("/edxImage/D042622@080252W0014329217F00000042I00K271053508.jpg");
//
//		System.out.print("妳好%n");
//		BufferedImage image = ImageIO.read(imageFile);
//		System.out.printf("妳好AAAAAAAAAAAAAAAAA%n");
//		System.out.printf("寬度="+image.getTileWidth());
//		System.out.printf("高度="+image.getTileHeight());
//		assertEquals(488, 488);
//		
//		ftpClient.logout();
//		ftpClient.disconnect();
		
	}

}
