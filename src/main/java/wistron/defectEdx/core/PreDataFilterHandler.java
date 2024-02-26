package wistron.defectEdx.core;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.Date;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import wistron.defectEdx.db.defectEdxDataTable.DefectEdxId;
import wistron.defectEdx.db.defectEdxDataTable.DefectEdxPreData;
import wistron.defectEdx.repository.defectEdx.DefectEdxPreDataRepository;
import wistron.defectEdx.repository.defectEdx.DefectEdxRepository;
import wistron.smg.tool.ftp.operator.FTPOperator;


@Component
public class PreDataFilterHandler {
	@Autowired
	private EdxPreDataGroup edxPreDataGroup;
	@Autowired
	@Qualifier("DefectEdxPreDataRepository")
	private DefectEdxPreDataRepository defectEdxPreDataRepository;
	@Autowired
	@Qualifier("defectEdxRepository")
	private DefectEdxRepository defectEdxRepository;

	public Boolean pixelFilter(FTPOperator itFtpOperator,String imageFileSpec, Integer Height, Integer Width) {
		try {
			InputStream imageFile = itFtpOperator.getRetrieveFileStream(imageFileSpec);
			BufferedImage image = ImageIO.read(imageFile);
			imageFile.close();
			itFtpOperator.completePendingCommand();
			if((image.getTileWidth() == Width) & (image.getTileHeight() == Height)){
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
		
		
	}
	public Boolean sizeFilter(FTPOperator itFtpOperator,String imageFileSpec,Integer sizeMax,Integer sizeMin) {
		try {
			long bytesOfImage = itFtpOperator.getFileSize(imageFileSpec);
			double sizeOfImage = bytesOfImage/1024;
			if(sizeOfImage > sizeMin && sizeOfImage < sizeMax){
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	public void edxImagePassOrNot(Boolean pass, String key, DefectEdxPreData defectEdxPreData,FTPOperator itFtpOperator, Integer Height, Integer Width,Date reduceInsertlasttime){
		if (pass) {
			combination(key,defectEdxPreData,itFtpOperator,Height,Width,reduceInsertlasttime);
		} else {
			defectEdxPreDataRepository.updatePreDataEdxImage("N",
					defectEdxPreData.getInspectionTime(),
					defectEdxPreData.getWaferKey(),	
					defectEdxPreData.getDefectId(), 
					defectEdxPreData.getImageFileSpec());
		};
	}
	private void combination(String key,DefectEdxPreData defectEdxPreData,FTPOperator itFtpOperator, Integer Height, Integer Width,Date reduceInsertlasttime){
		updatePreDataEdxAndInsertToDataEdx(defectEdxPreData);
		if (key == "G3G4"){
			edxPreDataGroup.groupG3G4Filter(defectEdxPreData,"1C",itFtpOperator,Height,Width,reduceInsertlasttime);
		} else {
			edxPreDataGroup.groupNonG3G4Filter(defectEdxPreData,reduceInsertlasttime);
		}
	}
	private void updatePreDataEdxAndInsertToDataEdx(DefectEdxPreData defectEdxPreData) {
		defectEdxPreDataRepository.updatePreDataEdxImage("Y",
				defectEdxPreData.getInspectionTime(),
				defectEdxPreData.getWaferKey(),	
				defectEdxPreData.getDefectId(), 
				defectEdxPreData.getImageFileSpec());
		 checkExistsAndDelete(defectEdxPreData);
		 defectEdxPreDataRepository.insertFromEdxPreToEdx(defectEdxPreData.getInspectionTime(),
		 		defectEdxPreData.getWaferKey(),	
		 		defectEdxPreData.getDefectId(),
				defectEdxPreData.getImageFileSpec(),
				defectEdxPreData.getRowid()
				);
	}
	public void checkExistsAndDelete(DefectEdxPreData defectEdxPreData){
		Boolean existDefectEdxDataBoolean = defectEdxRepository.existsById(new DefectEdxId(defectEdxPreData.getInspectionTime(),defectEdxPreData.getWaferKey(),defectEdxPreData.getDefectId(),defectEdxPreData.getImageFileSpec()));
		if (existDefectEdxDataBoolean) {
			//刪除已存在的相關資料
			try {
				defectEdxRepository.deleteById(new DefectEdxId(defectEdxPreData.getInspectionTime(),defectEdxPreData.getWaferKey(),defectEdxPreData.getDefectId(),defectEdxPreData.getImageFileSpec()));
			}	catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
}
