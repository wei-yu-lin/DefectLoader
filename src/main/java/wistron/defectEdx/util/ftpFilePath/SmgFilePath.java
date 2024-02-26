package wistron.defectEdx.util.ftpFilePath;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import wistron.defectEdx.db.defectEdxDataTable.DefectEdxData;

@Component("smgFilePath")
@Lazy
public class SmgFilePath {

	@Value("${wistron.smg.ftp.smgImagePath}")
	private String smgImagePath;

	public String getSmgFtpDirectoryPath(DefectEdxData defectEdxDataInfo) throws SQLException {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM/dd");
		LocalDateTime inspectionTime = defectEdxDataInfo.getDefectEdxId().inspectionTimeToLocalDateTime();
		return String.format("%s/%s/%s/%s", smgImagePath, inspectionTime.format(formatter),
				defectEdxDataInfo.getDefectEdxId().getWaferKey(), defectEdxDataInfo.getDefectEdxId().getDefectId());
	}
}
