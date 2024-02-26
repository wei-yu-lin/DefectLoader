package wistron.defectEdx.repository.defectEdx;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import wistron.defectEdx.db.defectEdxDataTable.DefectEdxData;
import wistron.defectEdx.db.defectEdxDataTable.DefectEdxId;
import wistron.defectEdx.db.defectEdxDataTable.DefectEdxPreData;

@Repository("defectEdxRepository")
public interface DefectEdxRepository extends JpaRepository<DefectEdxData, DefectEdxId> {
	//Find list.
	@Query("SELECT c from DefectEdxData c "
			+ "WHERE tableCreateTime >= to_date(:startTime,'YYYY-MM-DD HH24:MI:SS') "
			+ "and tableCreateTime <= to_date(:endTime,'YYYY-MM-DD HH24:MI:SS') "
			+ "and loaderUpdateTime IS NULL")
	public List<DefectEdxData> findEdxFilespec(@Param("startTime") String startTime,
			@Param("endTime") String endTime);


	//Update ftp image path.
	@Query("UPDATE DefectEdxData SET imageFileFtpPath = :imageFileFtpPath, loaderUpdateTime = CURRENT_TIMESTAMP "
			+ "WHERE defectEdxId.inspectionTime = :inspectionTime and defectEdxId.waferKey = :waferKey and defectEdxId.defectId = :defectId "
			+ "and defectEdxId.imageFileSpec = :imageFileSpec")
	@Modifying
	@Transactional
	public void updateImageFileFtpPath(@Param("imageFileFtpPath") String imageFileFtpPath, @Param("inspectionTime") Date inspectionTime,
			@Param("waferKey") Integer waferKey, @Param("defectId") Integer defectId,
			@Param("imageFileSpec") String imageFileSpec);
	
	//No found image. Update loader time only.
	@Query("UPDATE DefectEdxData SET loaderUpdateTime = CURRENT_TIMESTAMP "
			+ "WHERE defectEdxId.inspectionTime = :inspectionTime and defectEdxId.waferKey = :waferKey and defectEdxId.defectId = :defectId "
			+ "and defectEdxId.imageFileSpec = :imageFileSpec")
	@Modifying
	@Transactional
	public void updateLoaderUpdateTime(@Param("inspectionTime") Date inspectionTime,
			@Param("waferKey") Integer waferKey, @Param("defectId") Integer defectId,
			@Param("imageFileSpec") String imageFilespec);
	

	@Query(value = "select A.INSPECTION_TIME,A.WAFER_KEY,A.LAST_UPDATE,A.FAB_ID,A.LOT_ID,"
		+ "A.WAFER_ID,A.COMPONENT_ID,A.SLOT_ID,A.DEVICE,A.TECHNOLOGY,A.LAYER_ID,A.INSPECT_EQUIP_ID,"
		+ "A.PROCESS_EQUIP_ID,A.CENTER_X,A.CENTER_Y,A.ORIGIN_X,A.ORIGIN_Y,B.DEFECT_ID,B.CLASS_NUMBER,"
		+ "B.INDEX_X,B.INDEX_Y,B.WAFER_X,B.WAFER_Y,B.SIZE_X,B.SIZE_Y,B.SIZE_D,REPLACE(C.LOADERID, '_', '-') REVIEW_EQUIP_ID,"
		+ "C.TOOL_KEY,C.RESULT_FILETIMESTAMP,D.IMAGE_TYPE,D.IMAGE_FILESPEC,NULL AS IMAGE_FILE_FTP_PATH,"
		+ ":reduceInsertlasttime AS TABLE_CREATE_TIME,NULL AS LOADER_UPDATE_TIME,'N' AS EDX_IMAGE "
		+ "FROM KT12A.INSP_WAFER_SUMMARY A,KT12A.INSP_DEFECT B,KT12A.INSP_WAFER_SCAN C,KT12A.INSP_WAFER_IMAGE D "
		+ "WHERE 1=1 AND A.INSPECTION_TIME = B.INSPECTION_TIME AND A.INSPECTION_TIME = C.INSPECTION_TIME AND A.INSPECTION_TIME = D.INSPECTION_TIME "
		+ "AND A.WAFER_KEY = B.WAFER_KEY AND A.WAFER_KEY = C.WAFER_KEY AND A.WAFER_KEY = D.WAFER_KEY AND B.DEFECT_ID = D.DEFECT_ID AND C.SCAN_ID = D.SCAN_ID "
		+ "AND (D.IMAGE_TYPE LIKE '%11' OR D.IMAGE_TYPE LIKE '%12' OR D.IMAGE_TYPE LIKE '%13'or D.IMAGE_TYPE LIKE '%21' OR D.IMAGE_TYPE LIKE '%22' OR D.IMAGE_TYPE LIKE '%23')"
		+ "AND A.WAFER_KEY = :waferKey AND B.DEFECT_ID = :defectId "
		+ "AND A.INSPECTION_TIME = :inspectionTime AND C.LOADERID = :reviewEquipId "
		+ "And C.RESULT_FILETIMESTAMP = :resultFileTimeStamp "
		, nativeQuery=true)
	@Modifying
	@Transactional
	public List<DefectEdxData> NonG3G4ForDefectEdx(
		@Param("inspectionTime") Date inspectionTime,
		@Param("waferKey") Integer waferKey,
		@Param("defectId") Integer defectId,
		@Param("reviewEquipId") String reviewEquipId,
		@Param("resultFileTimeStamp") Date resultFileTimeStamp,
		@Param("reduceInsertlasttime") Date reduceInsertlasttime
	);
	
}

