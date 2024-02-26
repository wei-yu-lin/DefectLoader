package wistron.defectEdx.repository.defectEdx;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import wistron.defectEdx.db.defectEdxDataTable.DefectEdxPreData;

@Repository("DefectEdxPreDataRepository")
public interface DefectEdxPreDataRepository extends JpaRepository<DefectEdxPreData, Integer> {
	
	
	List<DefectEdxPreData> findAllBytableCreateTimeBetweenAndEdxImageIsNull(Date start, Date end);
	List<DefectEdxPreData> findByInspectionTimeAndWaferKeyAndDefectIdAndReviewEquipIdAndImageType(
		Date inspectionTime, Integer WaferKey, Integer defectId, String reviewEquipId, String imageType);
	
	@Query(value = "UPDATE Defect_Edx_Pre_Data SET edx_Image = :edxImage "
	 		+ "WHERE inspection_Time = :inspectionTime and wafer_Key = :waferKey and defect_Id = :defectId "
	 		+ "and image_FileSpec = :imageFileSpec", nativeQuery=true)
	@Modifying
	@Transactional
	public void updatePreDataEdxImage(@Param("edxImage") String edxImage,
 		@Param("inspectionTime") Date inspectionTime,
 		@Param("waferKey") Integer waferKey,
 		@Param("defectId") Integer defectId, 
 		@Param("imageFileSpec") String imageFileSpec);
	
	
	@Query(value = "insert into DEFECT_EDX_DATA "
			+ "select * from Defect_Edx_Pre_Data "
	 		+ "WHERE inspection_Time = :inspectionTime and wafer_Key = :waferKey and defect_Id = :defectId "
	 		+ "and image_FileSpec = :imageFileSpec and rowid = :rowid", nativeQuery=true)
	@Modifying
	@Transactional
	public void insertFromEdxPreToEdx(@Param("inspectionTime") Date inspectionTime,
 		@Param("waferKey") Integer waferKey,
 		@Param("defectId") Integer defectId, 
 		@Param("imageFileSpec") String imageFileSpec,
 		@Param("rowid") String rowid
		 );
	
	@Query(value = "insert into DEFECT_EDX_DATA "
			+ "select INSPECTION_TIME,WAFER_KEY,LAST_UPDATE,FAB_ID,LOT_ID,WAFER_ID,COMPONENT_ID,SLOT_ID,DEVICE,TECHNOLOGY,LAYER_ID,INSPECT_EQUIP_ID,PROCESS_EQUIP_ID,CENTER_X,CENTER_Y,ORIGIN_X,ORIGIN_Y,DEFECT_ID,CLASS_NUMBER,INDEX_X,INDEX_Y,WAFER_X,WAFER_Y,SIZE_X,SIZE_Y,SIZE_D,REVIEW_EQUIP_ID,TOOL_KEY,RESULT_FILETIMESTAMP,IMAGE_TYPE,IMAGE_FILESPEC,IMAGE_FILE_FTP_PATH,:reduceInsertlasttime as TABLE_CREATE_TIME,LOADER_UPDATE_TIME,EDX_IMAGE from Defect_Edx_Pre_Data "
	 		+ "WHERE inspection_Time = :inspectionTime and wafer_Key = :waferKey and defect_Id = :defectId "
	 		+ "and image_FileSpec = :imageFileSpec and rowid = :rowid", nativeQuery=true)
	@Modifying
	@Transactional
	public void insertToEdxTableCreateTime(@Param("inspectionTime") Date inspectionTime,
 		@Param("waferKey") Integer waferKey,
 		@Param("defectId") Integer defectId, 
 		@Param("imageFileSpec") String imageFileSpec,
		@Param("rowid") String rowid,
		@Param("reduceInsertlasttime") Date reduceInsertlasttime 
 		);
}