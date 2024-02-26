package wistron.defectEdx.util.ftpFilePath;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import wistron.defectEdx.util.ftpFilePath.ItFilePath;

public class ItFilePathTest {
		
	@Test
	public void testFileName() {
		String fileName = ItFilePath.getFileName(
				"/images04/f12admdb02/20210124/23/2719_3058063/D012421@232719W0003058063F00000031I02K258736344.jpg");
		assertEquals("D012421@232719W0003058063F00000031I02K258736344.jpg", fileName);
	}

	@Test
	public void testFileDirectory() {
		String fileDirectory = ItFilePath.getFileDirectory("/images04/f12admdb02/20210124/23/2719_3058063/D012421@232719W0003058063F00000031I02K258736344.jpg");
		assertEquals("/images04/f12admdb02/20210124/23/2719_3058063/", fileDirectory);
	}

}
