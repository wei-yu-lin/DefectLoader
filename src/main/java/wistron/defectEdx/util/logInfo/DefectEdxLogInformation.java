package wistron.defectEdx.util.logInfo;

import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;
import wistron.defectEdx.db.defectEdxDataTable.DefectEdxData;

@Configuration
@Slf4j
public class DefectEdxLogInformation {
	public void logInfo(String information, DefectEdxData defectEdxData) {
		log.info(information);
		log.info(defectEdxData.getDefectEdxId().toString());
	}

	public void logWarn(String information, DefectEdxData defectEdxData) {
		log.warn(information);
		log.warn(defectEdxData.getDefectEdxId().toString());
	}

	public void logError(String information, DefectEdxData defectEdxData) {
		log.error(information);
		log.error(defectEdxData.getDefectEdxId().toString());
	}
}
