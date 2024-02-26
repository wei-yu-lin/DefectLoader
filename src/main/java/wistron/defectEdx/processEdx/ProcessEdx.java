package wistron.defectEdx.processEdx;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.wistron.mail.sender.MailType;

import lombok.extern.slf4j.Slf4j;
import wistron.LoaderControl.LoaderControl;
import wistron.LoaderControl.Exception.umcLoaderControlException;
import wistron.LoaderControl.Exception.umcLoaderControlNotRunException;
import wistron.defectEdx.core.EdxPreDataGroup;
import wistron.defectEdx.core.PreDataFilterHandler;
import wistron.defectEdx.db.defectEdxDataTable.DefectEdxData;
import wistron.defectEdx.db.defectEdxDataTable.DefectEdxPreData;
import wistron.defectEdx.repository.defectEdx.DefectEdxPreDataRepository;
import wistron.defectEdx.repository.defectEdx.DefectEdxRepository;
import wistron.defectEdx.util.db.DbUtils;
import wistron.defectEdx.util.ftpFilePath.ItFilePath;
import wistron.defectEdx.util.ftpFilePath.SmgFilePath;
import wistron.defectEdx.util.logInfo.DefectEdxLogInformation;
import wistron.defectEdx.util.mail.MailService;
import wistron.smg.tool.ftp.exception.FTPRelatedException;
import wistron.smg.tool.ftp.operator.FTPOperator;

@Component("processEdx")
@Lazy
@Slf4j
public class ProcessEdx {
	@Autowired
	@Qualifier("defectEdxRepository")
	private DefectEdxRepository defectEdxRepository;
	@Autowired
	@Qualifier("DefectEdxPreDataRepository")
	private DefectEdxPreDataRepository defectEdxPreDataRepository;
	@Autowired
	private SmgFilePath smgFilePath;
	@Autowired
	private DefectEdxLogInformation logInfo;
	@Autowired
	private MailService mailService;
	@Autowired
	private EdxPreDataGroup edxPreDataGroup;
	@Autowired
	private PreDataFilterHandler preDataFilterHandler;

	@Value("${spring.datasource.defect-edx.jdbc-url}")
	private String jdbcUrl;
	@Value("${spring.datasource.defect-edx.username}")
	private String dbUsername;
	@Value("${spring.datasource.defect-edx.password}")
	private String dbPassword;
	@Value("${wistron.it.ftp.ip}")
	private String itFtpIp;
	@Value("${wistron.it.ftp.user}")
	private String itFtpUser;
	@Value("${wistron.it.ftp.password}")
	private String itFtpPassword;
	@Value("${wistron.smg.ftp.ip}")
	private String smgFtpIp;
	@Value("${wistron.smg.ftp.user}")
	private String smgFtpUser;
	@Value("${wistron.smg.ftp.password}")
	private String smgFtpPassword;
	@Value("${wistron.address.downloadLocalAddress}")
	private String downloadLocalAddress;
//  filter condition
	@Value("${tool_type.tool_G3G4.tool}")
	private String toolG3G4EquipId;
	@Value("${tool_type.tool_G3G4.edx_pixcel_filter.enable}")
	private Boolean toolG3G4EdxPixcelEnable;
	@Value("${tool_type.tool_G3G4.edx_pixcel_filter.pixcel_width}")
	private Integer toolG3G4EdxPixcelWidth;
	@Value("${tool_type.tool_G3G4.edx_pixcel_filter.pixcel_height}")
	private Integer toolG3G4EdxPixcelHeight;
	@Value("${tool_type.tool_G3G4.edx_size_filter.enable}")
	private Boolean toolG3G4EdxSizeEnable;
	@Value("${tool_type.tool_G3G4.edx_size_filter.size_kb_max}")
	private String toolG3G4EdxSizeKbMax;
	@Value("${tool_type.tool_G3G4.edx_size_filter.size_kb_min}")
	private String toolG3G4EdxSizeKbMin;

	@Value("${tool_type.tool_7110.tool}")
	private String tool7110EquipId;
	@Value("${tool_type.tool_7110.edx_pixcel_filter.enable}")
	private Boolean tool7110EdxPixcelEnable;
	@Value("${tool_type.tool_7110.edx_pixcel_filter.pixcel_width}")
	private Integer tool7110EdxPixcelWidth;
	@Value("${tool_type.tool_7110.edx_pixcel_filter.pixcel_height}")
	private Integer tool7110EdxPixcelHeight;
	@Value("${tool_type.tool_7110.edx_size_filter.enable}")
	private Boolean tool7110EdxSizeEnable;

	@Value("${tool_type.tool_7280.tool}")
	private String tool7280EquipId;
	@Value("${tool_type.tool_7280.edx_pixcel_filter.enable}")
	private Boolean tool7280EdxPixcelEnable;
	@Value("${tool_type.tool_7280.edx_pixcel_filter.pixcel_width}")
	private Integer tool7280EdxPixcelWidth;
	@Value("${tool_type.tool_7280.edx_pixcel_filter.pixcel_height}")
	private Integer tool7280EdxPixcelHeight;
	@Value("${tool_type.tool_7280.edx_size_filter.size_kb_min}")
	private Integer tool7280EdxSizeKbMin;
	@Value("${tool_type.tool_7280.edx_size_filter.size_kb_max}")
	private Integer tool7280EdxSizeKbMax;
	@Value("${tool_type.tool_7280.edx_size_filter.enable}")
	private Boolean tool7280EdxSizeEnable;

	@Value("${tool_type.tool_7380.tool}")
	private String tool7380EquipId;
	@Value("${tool_type.tool_7380.edx_pixcel_filter.enable}")
	private Boolean tool7380EdxPixcelEnable;
	@Value("${tool_type.tool_7380.edx_pixcel_filter.pixcel_width}")
	private Integer tool7380EdxPixcelWidth;
	@Value("${tool_type.tool_7380.edx_pixcel_filter.pixcel_height}")
	private Integer tool7380EdxPixcelHeight;
	@Value("${tool_type.tool_7380.edx_size_filter.enable}")
	private Boolean tool7380EdxSizeEnable;


	private static final String LOADERNAME = "DefectEdxLoader";

	private File f;
	private LoaderControl lc;

	@PostConstruct
	private void postConstruct() {
		log.info("Local download address: {}", downloadLocalAddress);
		createLocalDownloadFolder();
		log.info("Defect EDX Loader Start!!");
		getLoaderInfo();
	}

	@PreDestroy
	private void preDestroy() {
		 cleanLocalDownloadFolder();
		 updateLoaderInfo();
		log.info("Defect EDX Loader END!!");
		 mailService.sendMail(MailType.INFO, new Date().toString() + " Loader End!", "This is the loader end complete!");
	}

	public void process() throws ParseException {
		Date startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(lc.getLastTime());
		Date endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(lc.getInsertlasttime());
		log.info("Loader data get from \"{}\" to \"{}\"", lc.getLastTime(), lc.getInsertlasttime());
		Date reduceInsertlasttime = reduceLoaderTime(lc.getInsertlasttime());
		
		try (FTPOperator itFtpOperator = new FTPOperator(itFtpIp, itFtpUser, itFtpPassword);){
			List<DefectEdxPreData> preDataList = defectEdxPreDataRepository
					.findAllBytableCreateTimeBetweenAndEdxImageIsNull(startDate, endDate);
			
			edxPreLoaderConditionConfirmation(preDataList, itFtpOperator,reduceInsertlasttime);
			log.info("Defect EDX Pre Loader End");
			itFtpOperator.close();
		} catch (Exception e) {
			e.printStackTrace();
			 mailService.sendMail(MailType.ERROR, "This is the Pre Data error mail", e);
		}

		try (FTPOperator itFtpOperator = new FTPOperator(itFtpIp, itFtpUser, itFtpPassword);
				FTPOperator smgFtpOperator = new FTPOperator(smgFtpIp, smgFtpUser, smgFtpPassword)) {

			List<DefectEdxData> list = defectEdxRepository.findEdxFilespec(lc.getLastTime(), lc.getInsertlasttime());

			for (int i = 0; i < list.size(); i++) {

				try {
					log.info("EDX Image Path is: {}", list.get(i).getDefectEdxId().getImageFileSpec());
					if (isImageExistInFtp(itFtpOperator, list.get(i))) {
						downloadAndUploadImageAndUpdateDb(itFtpOperator, list.get(i), smgFtpOperator);
					} else {
						updateLoaderTimeOnly(list.get(i));
						logInfo.logWarn("Image Path is NOT Exist!! ", list.get(i));
					}
				} catch (Exception e) {
					e.printStackTrace();
					 mailService.sendMail(MailType.ERROR, "This is the error mail", e);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			 mailService.sendMail(MailType.ERROR, "This is the error mail", e);
		}
		log.info("Defect EDX Loader End");
	}

	public LoaderControl initDefect() {
		LoaderControl lc = null;
		log.info("Connection DB");

		try {
			lc = new LoaderControl(LOADERNAME, DbUtils.getDbLinkName(jdbcUrl, dbUsername, dbPassword));
		} catch (umcLoaderControlException | umcLoaderControlNotRunException e) {
			log.error("ERROR", e);
		}
		return lc;
	}

	private void downloadAndUploadImageAndUpdateDb(FTPOperator itFtpOperator, DefectEdxData defectEdxData,
			FTPOperator smgFtpOperator) {
		downloadFromItFtp(itFtpOperator, defectEdxData);
		uploadFtpAndUpdateDb(defectEdxData, smgFtpOperator);
	}

	private void downloadFromItFtp(FTPOperator itFtpOperator, DefectEdxData defectEdxData) {
		try {
			itFtpOperator.downloadFile(defectEdxData.getDefectEdxId().getImageFileSpec(),
					absoluteFilePath(getFileName(defectEdxData), downloadLocalAddress));
			log.info("Wafer Image Download OK");
		} catch (Exception e) {
			e.printStackTrace();
			logInfo.logError("Error! Download Image from IT FTP FAILD. ", defectEdxData);
			mailService.sendMail(MailType.ERROR, "This is the IT FTP FAILD error mail", e);
		}
	}

	private void uploadFtpAndUpdateDb(DefectEdxData defectEdxData, FTPOperator smgFtpOperator) {
		try {
			smgFtpOperator.makeAndChangeToRemoteDirs(smgFilePath.getSmgFtpDirectoryPath(defectEdxData));
			smgFtpOperator.uploadFile(absoluteFilePath(getFileName(defectEdxData), downloadLocalAddress),
					absoluteFilePath(getFileName(defectEdxData), smgFilePath.getSmgFtpDirectoryPath(defectEdxData)));
			updateSmgImagePath(defectEdxData);
			logInfo.logInfo("Upload FTP and update DB OK. ", defectEdxData);
		} catch (Exception e) {
			e.printStackTrace();
			logInfo.logError("Error! Upload FTP and Update DB FAIL. ", defectEdxData);
			 mailService.sendMail(MailType.ERROR, "This is the SMG FTP OR DB error mail", e);
		}
	}

	private boolean isImageExistInFtp(FTPOperator itFtpOperator, DefectEdxData defectEdxData)
			throws FTPRelatedException {
		return itFtpOperator.isFileExists(defectEdxData.getDefectEdxId().getImageFileSpec());

	}

	private void updateSmgImagePath(DefectEdxData defectEdxData) throws SQLException {
		defectEdxRepository.updateImageFileFtpPath(
				absoluteFilePath(getFileName(defectEdxData), smgFilePath.getSmgFtpDirectoryPath(defectEdxData)),
				defectEdxData.getDefectEdxId().getInspectionTime(), defectEdxData.getDefectEdxId().getWaferKey(),
				defectEdxData.getDefectEdxId().getDefectId(), defectEdxData.getDefectEdxId().getImageFileSpec());
	}

	private void updateLoaderTimeOnly(DefectEdxData defectEdxData) {
		defectEdxRepository.updateLoaderUpdateTime(defectEdxData.getDefectEdxId().getInspectionTime(),
				defectEdxData.getDefectEdxId().getWaferKey(), defectEdxData.getDefectEdxId().getDefectId(),
				defectEdxData.getDefectEdxId().getImageFileSpec());
	}

	private void createLocalDownloadFolder() {
		f = new File(downloadLocalAddress);
		try {
			if (f.exists()) {
				FileUtils.forceDelete(f);
			}
		} catch (IOException e) {
			log.error("ERROR", e);
		}
		f.mkdir();
	}

	private void getLoaderInfo() {
		lc = initDefect();
	}

	private void cleanLocalDownloadFolder() {
		try {
			FileUtils.forceDelete(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void updateLoaderInfo() {
		try {
			lc.update();
		} catch (umcLoaderControlException e) {
			e.printStackTrace();
		}
	}

	private String absoluteFilePath(String fileName, String directory) {
		return String.format("%s/%s", directory, fileName);
	}

	private String getFileName(DefectEdxData defectEdxData) {
		return ItFilePath.getFileName(defectEdxData.getDefectEdxId().getImageFileSpec());
	}

	private void edxPreLoaderConditionConfirmation(List<DefectEdxPreData> defectEdxPreDataList,
			FTPOperator itFtpOperator,Date reduceInsertlasttime) {
		Map<String, String[]> toolEquipIdMap = new HashMap<>();
		toolEquipIdMap.put("G3G4", toolG3G4EquipId.split(","));
		toolEquipIdMap.put("7110", tool7110EquipId.split(","));
		toolEquipIdMap.put("7280", tool7280EquipId.split(","));
		toolEquipIdMap.put("7380", tool7380EquipId.split(","));
		defectEdxPreDataList.forEach((row) -> {
			for (String key : toolEquipIdMap.keySet()) {
				if (Arrays.stream(toolEquipIdMap.get(key)).anyMatch(row.getReviewEquipId()::equals)) {
					try {
						edxPreLoaderSizeOfImage(row, key, itFtpOperator,reduceInsertlasttime);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	private void edxPreLoaderSizeOfImage(DefectEdxPreData defectEdxPreData, String key, FTPOperator itFtpOperator,Date reduceInsertlasttime)
			throws IOException {
		Integer width = 0;
		Integer height = 0;
		Integer sizeMax = 0;
		Integer sizeMin = 0;
		Boolean pixelEnable = false;
		Boolean sizeEnable = false;
		switch (key) {
		case "G3G4":
			width = toolG3G4EdxPixcelWidth;
			height = toolG3G4EdxPixcelHeight;
			pixelEnable = toolG3G4EdxPixcelEnable;
			sizeEnable = toolG3G4EdxSizeEnable;
			break;
		case "7110":
			width = tool7110EdxPixcelWidth;
			height = tool7110EdxPixcelHeight;
			pixelEnable = tool7110EdxPixcelEnable;
			sizeEnable = tool7110EdxSizeEnable;
			break;
		case "7280":
			width = tool7280EdxPixcelWidth;
			height = tool7280EdxPixcelHeight;
			pixelEnable = tool7280EdxPixcelEnable;
			sizeEnable = tool7280EdxSizeEnable;
			sizeMax = tool7280EdxSizeKbMax;
			sizeMin = tool7280EdxSizeKbMin;
			break;
		case "7380":
			width = tool7380EdxPixcelWidth;
			height = tool7380EdxPixcelHeight;
			pixelEnable = tool7380EdxPixcelEnable;
			sizeEnable = tool7380EdxSizeEnable;
			break;
		default:
			break;
		}

		if (pixelEnable & sizeEnable) {
			Boolean pixelSizePass = preDataFilterHandler.pixelFilter(itFtpOperator, defectEdxPreData.getImageFileSpec(),
					height, width)
					&& preDataFilterHandler.sizeFilter(itFtpOperator, defectEdxPreData.getImageFileSpec(), sizeMax,
							sizeMin);
			preDataFilterHandler.edxImagePassOrNot(pixelSizePass, key, defectEdxPreData, itFtpOperator, height, width,reduceInsertlasttime);

		} else if (pixelEnable) {
			Boolean pixelPass = preDataFilterHandler.pixelFilter(itFtpOperator, defectEdxPreData.getImageFileSpec(),
					height, width);
			preDataFilterHandler.edxImagePassOrNot(pixelPass, key, defectEdxPreData, itFtpOperator, height, width,reduceInsertlasttime);

		} else if (sizeEnable) {
			Boolean sizePass = preDataFilterHandler.sizeFilter(itFtpOperator, defectEdxPreData.getImageFileSpec(),
					sizeMax, sizeMin);
			preDataFilterHandler.edxImagePassOrNot(sizePass, key, defectEdxPreData, itFtpOperator, height, width,reduceInsertlasttime);
		}
	}
	
	private Date reduceLoaderTime(String lcLastTime) throws ParseException{
		Calendar lcTime = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			lcTime.setTime(sdf.parse(lcLastTime));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Calendar newTime = Calendar.getInstance();
		newTime.setTimeInMillis(lcTime.getTimeInMillis());
		newTime.add(Calendar.MINUTE, -1);
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String newTimeString = dateFormatter.format(newTime.getTime());
		
		Date returnDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(newTimeString);
		return returnDate; 
		
	}
}
