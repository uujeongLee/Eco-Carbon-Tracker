package business.com.cmm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import commf.dao.CommonMapperImpl;
import common.base.BaseService;
import common.file.FileInfo;
import common.util.CommUtils;
import common.util.FileUtils;
import common.util.properties.ApplicationProperty;

@SuppressWarnings( {"rawtypes", "unchecked"})
@Service
public class FileService extends BaseService {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private CommonMapperImpl dao;

	//  파일 조회
	public List<Map> listFile(Map params) {
		List list = dao.list("File.listFile", params);
		return list;
	}

	// 첨부파일 조회
    public Map viewFile(Map params) {
        Map file = (Map)dao.view("File.viewFile", params);
        return file;
    }

	// 첨부파일 등록
	public int regiFiles(List<HashMap> fileList) {
		int cnt = 0;
		for (int i = 0; i < fileList.size(); i++) {
			HashMap fileInfo = (HashMap)fileList.get(i);
			cnt = (Integer)dao.update("File.regiFile", fileInfo);
		}

		return cnt;
	}

	// 첨부파일 삭제 (선택 파일)
	public int deltFiles(List<HashMap> fileList, String updtId) {
		int cnt = 0;
		for (int i = 0; i < fileList.size(); i++) {
			HashMap fileInfo = (HashMap)fileList.get(i);
			fileInfo.put("updtId", updtId);
			cnt += (Integer)dao.update("File.deltFile", fileInfo);
		}

		return cnt;
	}

	// 첨부파일 삭제 (전체 파일)
	public int deltFiles(String delNo, String updtId) {
		Map fileMap = new HashMap();
		fileMap.put("rootNo", delNo);
		fileMap.put("updtId", updtId);

		// DB  삭제
		int cnt = (Integer)dao.update("File.deltAllFiles", fileMap);
		return cnt;
	}


	//==================================================================================
	//  첨부파일 처리
	//  mode : I 등록, U 수정, D 삭제
	//==================================================================================
	public int fileManagement(Map paramFileMap, List<HashMap> listNewFile) throws Exception {
		String regiId 	= CommUtils.nvlTrim(userInfo.getUserId());							//등록자
		String updtId 	= CommUtils.nvlTrim(userInfo.getUserId());							//수정자
		String ip 		= CommUtils.nvlTrim(userInfo.getIpAddr());							//IP

		String tempPath = ApplicationProperty.get("upload.temp.dir");
		String realPath = ApplicationProperty.get("upload.real.dir");

		String rootNo	= CommUtils.nvlTrim((String)paramFileMap.get("rootNo"));			//Root 번호
		String subDir	= CommUtils.nvlTrim((String)paramFileMap.get("subDir"));			//경로

		String status	= CommUtils.nvlTrim((String)paramFileMap.get("status"));			//처리상태
		String docuType	= CommUtils.nvlTrim((String)paramFileMap.get("docuType"));			//문서 구분

		String[] arrFileNo	= (String[])paramFileMap.get("arrFileNo");						//삭제 체크SEQ

		realPath 	= realPath + subDir + "/" + CommUtils.getToday("");

		int cnt = 0;
		List<HashMap> regFileList = new ArrayList();

		// 기존 파일 삭제 (삭제선택된 기존파일 또는 삭제 상태)
		if ((arrFileNo != null && arrFileNo.length > 0) || status.equalsIgnoreCase("D")) {
			// 삭제파일 조회
			List listDelFile	= dao.list("File.listDelFile", paramFileMap);

			// 삭제 파일이 존재하면 처리...
			if (listDelFile != null) {
				// 파일 삭제(서버)
				for (int i = 0; i < listDelFile.size(); i++) {
					HashMap fileInfo 		= (HashMap)listDelFile.get(i);
					String serverFileNm 	= CommUtils.nvlTrim((String)fileInfo.get("fileSvrNm"));
					String path 			= CommUtils.nvlTrim((String)fileInfo.get("filePath"));

					if(status.equalsIgnoreCase("D"))
						// 서버 파일 삭제
						FileUtils.deleteFile(path + "/" + serverFileNm);
				}

				// 게시물 삭제일경우 첨부 전체 삭제 [파일 삭제(DB)]
				if (status.equalsIgnoreCase("D")) {
					cnt = deltFiles(rootNo,updtId);
				}
				else {
					cnt = deltFiles(listDelFile,updtId);
				}
			}
		}

		// 파일 등록
		if (listNewFile != null && listNewFile.size() > 0) {
			for (int i = 0; i < listNewFile.size(); i++) {
				
				HashMap fileInfo 		= (HashMap)listNewFile.get(i);
				String serverFileNm 	= CommUtils.nvlTrim((String)fileInfo.get("fileSvrNm"));

				//디렉토리 생성
				FileUtils.makeDirectories(realPath);

				//임시폴더에서 리얼폴더로 이동.
				FileUtils.moveFile(tempPath + serverFileNm, realPath + "/" + serverFileNm );

				fileInfo.put("ip",			ip);
				fileInfo.put("regiId", 		regiId);
				if(CommUtils.isEmpty((String)fileInfo.get("rootNo"))){
				    fileInfo.put("rootNo", 		rootNo);
				}
				if(CommUtils.isEmpty((String)fileInfo.get("docuType"))){
					fileInfo.put("docuType", 	docuType);
				}

				fileInfo.put("filePath", 	realPath);

				regFileList.add(fileInfo);
			}

			// DB 저장
			if (regFileList.size() > 0)
				cnt = regiFiles(regFileList);
		}

		return cnt;
	}
	
	// 사용자 : 대상차량 첨부파일 등록
	public int regiTargetFiles(List<HashMap> fileList) {
		int cnt = 0;
		for (int i = 0; i < fileList.size(); i++) {
			HashMap fileInfo = (HashMap)fileList.get(i);
			cnt = (Integer)dao.update("Target.uploadCarFile", fileInfo);
		}

		return cnt;
	}
	
	// 사용자 : 대상차량 첨부파일 수정 
	public int modifyTargetFiles(List<HashMap> fileList) {
		int cnt = 0;
		for (int i = 0; i < fileList.size(); i++) {
			HashMap fileInfo = (HashMap)fileList.get(i);
			cnt = (Integer)dao.update("Target.modifyUploadFile", fileInfo);
		}

		return cnt;
	}
	
	// 관리자: 팩스전송파일 저장
		public int faxFiles(List<HashMap> fileList) {
			int cnt = 0;
			for (int i = 0; i < fileList.size(); i++) {
				HashMap fileInfo = (HashMap)fileList.get(i);
				cnt = (Integer)dao.update("Target.saveFaxFile", fileInfo);
			}

			return cnt;
		}
	
	public int targetFileManagement(Map paramFileMap, List<HashMap> listNewFile) throws Exception {
		String regiId 	= CommUtils.nvlTrim(userInfo.getUserId());							//등록자
		String updtId 	= CommUtils.nvlTrim(userInfo.getUserId());							//수정자
		String ip 		= CommUtils.nvlTrim(userInfo.getIpAddr());							//IP

		String tempPath = ApplicationProperty.get("upload.temp.dir");
		String realPath = ApplicationProperty.get("upload.real.dir");

		String rootNo	= CommUtils.nvlTrim((String)paramFileMap.get("rootNo"));			//Root 번호
		String subDir	= CommUtils.nvlTrim((String)paramFileMap.get("subDir"));			//경로
		String rcptNo   = CommUtils.nvlTrim((String)paramFileMap.get("rcptNo"));            //접수번호

		String status	= CommUtils.nvlTrim((String)paramFileMap.get("status"));			//처리상태
		String docuType	= CommUtils.nvlTrim((String)paramFileMap.get("docuType"));			//문서 구분

		String[] arrFileNo	= (String[])paramFileMap.get("arrFileNo");						//삭제 체크SEQ

		realPath 	= realPath + subDir + "/" + CommUtils.getToday("") + "/" + rcptNo;
		System.out.println(realPath);
		int cnt = 0;
		List<HashMap> regFileList = new ArrayList();

		// 기존 파일 삭제 (삭제선택된 기존파일 또는 삭제 상태)
		if ((arrFileNo != null && arrFileNo.length > 0) || status.equalsIgnoreCase("D")) {
			// 삭제파일 조회
			List listDelFile	= dao.list("File.listDelFile", paramFileMap);

			// 삭제 파일이 존재하면 처리...
			if (listDelFile != null) {
				// 파일 삭제(서버)
				for (int i = 0; i < listDelFile.size(); i++) {
					HashMap fileInfo 		= (HashMap)listDelFile.get(i);
					String serverFileNm 	= CommUtils.nvlTrim((String)fileInfo.get("fileSvrNm"));
					String path 			= CommUtils.nvlTrim((String)fileInfo.get("filePath"));

					if(status.equalsIgnoreCase("D"))
						// 서버 파일 삭제
						FileUtils.deleteFile(path + "/" + serverFileNm);
				}

				// 게시물 삭제일경우 첨부 전체 삭제 [파일 삭제(DB)]
				if (status.equalsIgnoreCase("D")) {
					cnt = deltFiles(rootNo,updtId);
				}
				else {
					cnt = deltFiles(listDelFile,updtId);
				}
			}
		}

		// 파일 등록
		if (listNewFile != null && listNewFile.size() > 0) {
			for (int i = 0; i < listNewFile.size(); i++) {
				
				HashMap fileInfo 		= (HashMap)listNewFile.get(i);
				String serverFileNm 	= CommUtils.nvlTrim((String)fileInfo.get("fileSvrNm"));
				
				//디렉토리 생성
				FileUtils.makeDirectories(realPath);

				//임시폴더에서 리얼폴더로 이동.
				FileUtils.moveFile(tempPath + serverFileNm, realPath + "/" + serverFileNm);

				fileInfo.put("ip",			ip);
				fileInfo.put("regiId", 		regiId);
				fileInfo.put("rcptNo", 		rcptNo);
				if(CommUtils.isEmpty((String)fileInfo.get("rootNo"))){
				    fileInfo.put("rootNo", 		rootNo);
				}
				if(CommUtils.isEmpty((String)fileInfo.get("docuType"))){
					fileInfo.put("docuType", 	docuType);
				}

				fileInfo.put("filePath", 	realPath);
				
				regFileList.add(fileInfo);
			}
			String seqNo = (String)regFileList.get(0).get("seq");
			// DB 저장
			if(seqNo != null && regFileList.size() > 0) {
				cnt = modifyTargetFiles(regFileList);
			}else if(seqNo == null && regFileList.size() > 0){
				cnt = regiTargetFiles(regFileList);
			}
			
		}

		return cnt;
	}
	
	public int faxFileManagement(Map paramFileMap, List<HashMap> listNewFile) throws Exception {
		String regiId 	= CommUtils.nvlTrim(userInfo.getUserId());							//등록자
		String updtId 	= CommUtils.nvlTrim(userInfo.getUserId());							//수정자
		String ip 		= CommUtils.nvlTrim(userInfo.getIpAddr());							//IP

		String tempPath = ApplicationProperty.get("upload.temp.dir");
		String realPath = ApplicationProperty.get("upload.fax.dir");

		String rootNo	= CommUtils.nvlTrim((String)paramFileMap.get("rootNo"));			//Root 번호
		String subDir	= CommUtils.nvlTrim((String)paramFileMap.get("subDir"));			//경로
		String rcptNo   = CommUtils.nvlTrim((String)paramFileMap.get("rcptNo"));            //접수번호

		String status	= CommUtils.nvlTrim((String)paramFileMap.get("status"));			//처리상태
		String docuType	= CommUtils.nvlTrim((String)paramFileMap.get("docuType"));			//문서 구분

		String[] arrFileNo	= (String[])paramFileMap.get("arrFileNo");						//삭제 체크SEQ

		realPath 	= realPath + subDir;

		int cnt = 0;
		List<HashMap> regFileList = new ArrayList();

		// 기존 파일 삭제 (삭제선택된 기존파일 또는 삭제 상태)
		if ((arrFileNo != null && arrFileNo.length > 0) || status.equalsIgnoreCase("D")) {
			// 삭제파일 조회
			List listDelFile	= dao.list("File.listDelFile", paramFileMap);

			// 삭제 파일이 존재하면 처리...
			if (listDelFile != null) {
				// 파일 삭제(서버)
				for (int i = 0; i < listDelFile.size(); i++) {
					HashMap fileInfo 		= (HashMap)listDelFile.get(i);
					String serverFileNm 	= CommUtils.nvlTrim((String)fileInfo.get("fileSvrNm"));
					String path 			= CommUtils.nvlTrim((String)fileInfo.get("filePath"));

					if(status.equalsIgnoreCase("D"))
						// 서버 파일 삭제
						FileUtils.deleteFile(path + "/" + serverFileNm);
				}

				// 게시물 삭제일경우 첨부 전체 삭제 [파일 삭제(DB)]
				if (status.equalsIgnoreCase("D")) {
					cnt = deltFiles(rootNo,updtId);
				}
				else {
					cnt = deltFiles(listDelFile,updtId);
				}
			}
		}

		// 파일 등록
		if (listNewFile != null && listNewFile.size() > 0) {
			for (int i = 0; i < listNewFile.size(); i++) {
				
				HashMap fileInfo 		= (HashMap)listNewFile.get(i);
				String serverFileNm 	= CommUtils.nvlTrim((String)fileInfo.get("fileSvrNm"));
				
				//디렉토리 생성
				FileUtils.makeDirectories(realPath);

				//임시폴더에서 리얼폴더로 이동.
				FileUtils.moveFile(tempPath + serverFileNm, realPath + "/" + serverFileNm);

				fileInfo.put("ip",			ip);
				fileInfo.put("regiId", 		regiId);
				fileInfo.put("rcptNo", 		rcptNo);
				if(CommUtils.isEmpty((String)fileInfo.get("rootNo"))){
				    fileInfo.put("rootNo", 		rootNo);
				}
				if(CommUtils.isEmpty((String)fileInfo.get("docuType"))){
					fileInfo.put("docuType", 	docuType);
				}

				fileInfo.put("filePath", 	realPath);
				
				regFileList.add(fileInfo);
			}
			String seqNo = (String)regFileList.get(0).get("seq");
			// DB 저장
			if(seqNo != null && regFileList.size() > 0) {
				cnt = modifyTargetFiles(regFileList);
			}else if(seqNo == null && regFileList.size() > 0){
				cnt = faxFiles(regFileList);
			}
			
		}

		return cnt;
	}
}
