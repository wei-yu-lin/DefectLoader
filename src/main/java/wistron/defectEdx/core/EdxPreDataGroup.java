package wistron.defectEdx.core;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import wistron.LoaderControl.LoaderControl;
import wistron.defectEdx.db.defectEdxDataTable.DefectEdxData;
import wistron.defectEdx.db.defectEdxDataTable.DefectEdxId;
import wistron.defectEdx.db.defectEdxDataTable.DefectEdxPreData;
import wistron.defectEdx.repository.defectEdx.DefectEdxPreDataRepository;
import wistron.defectEdx.repository.defectEdx.DefectEdxRepository;
import wistron.smg.tool.ftp.operator.FTPOperator;



@Component
@Slf4j
public class EdxPreDataGroup {
	@Autowired
	@Qualifier("DefectEdxPreDataRepository")
	private DefectEdxPreDataRepository defectEdxPreDataRepository;
	@Autowired
	@Qualifier("defectEdxRepository")
	private DefectEdxRepository defectEdxRepository;
	@Autowired
	private PreDataFilterHandler preDataFilterHandler;
	
	
	public void groupG3G4Filter(DefectEdxPreData defectEdxPreData, String edxPreDataImageType,FTPOperator itFtpOperator, Integer Height, Integer Width,Date reduceInsertlasttime) {
		log.info("groupG3G4Filter started ");
			List<DefectEdxPreData> groupEdxPreData = defectEdxPreDataRepository.findByInspectionTimeAndWaferKeyAndDefectIdAndReviewEquipIdAndImageType(
					defectEdxPreData.getInspectionTime(),
					defectEdxPreData.getWaferKey(),
					defectEdxPreData.getDefectId(), 
					defectEdxPreData.getReviewEquipId(),
					edxPreDataImageType
					);
			
			groupEdxPreData.forEach(e->{
				
				Boolean pixelFilterBoolean = preDataFilterHandler.pixelFilter(itFtpOperator, e.getImageFileSpec(), Height, Width);
				if (pixelFilterBoolean) {
					defectEdxPreDataRepository.updatePreDataEdxImage(
							"Y", e.getInspectionTime(), e.getWaferKey(), e.getDefectId(), e.getImageFileSpec()
							);
				} else {
					defectEdxPreDataRepository.updatePreDataEdxImage("N",
							e.getInspectionTime(),
							e.getWaferKey(),	
							e.getDefectId(), 
							e.getImageFileSpec());
				}
				preDataFilterHandler.checkExistsAndDelete(e);
				
				defectEdxPreDataRepository.insertToEdxTableCreateTime(
						e.getInspectionTime(),
						e.getWaferKey(),
						e.getDefectId(),
						e.getImageFileSpec(),
						e.getRowid(),
						reduceInsertlasttime
						);
			});
			log.info("groupG3G4Filter finished ");
	}
	public void groupNonG3G4Filter(DefectEdxPreData defectEdxPreData,Date reduceInsertlasttime) {
		log.info("groupNonG3G4Filter started ");
		List<DefectEdxData> groupNonG3G4InspData = defectEdxRepository.NonG3G4ForDefectEdx(
				defectEdxPreData.getInspectionTime(),
				defectEdxPreData.getWaferKey(),
				defectEdxPreData.getDefectId(),
				defectEdxPreData.getReviewEquipId().replace('-', '_'),
				defectEdxPreData.getResultFileTimeStamp(),
				reduceInsertlasttime
				);
		
		groupNonG3G4InspData.forEach(e->{
			//判斷defect_edx_data是否已存在相關資料
			Boolean existDefectEdxDataBoolean = defectEdxRepository.existsById(e.getDefectEdxId());

			if (existDefectEdxDataBoolean) {
				//刪除已存在的相關資料
				defectEdxRepository.deleteById(e.getDefectEdxId());
			}
			defectEdxRepository.save(e);
		});
		log.info("groupNonG3G4Filter finished ");
	}
}
