package kr.co.bomz.logger.extention;

/**
 * 외부 프레임워크 로그 연동 모듈
 * 
 * @author Bomz
 * @since 1.0
 * @version 1.0
 * @see	MyBatisLog
 * @see	ApacheCommonsLog
 */
public class Extention {

	// 사용할 수 있는 외부 연동 로그 모듈
	private static final String[] list = new String[]{
			"kr.co.bomz.logger.extention.MyBatisLog",					// MyBatis
			"kr.co.bomz.logger.extention.ApacheCommonsLog"	// Apache Commons Log
	};
	
	/**
	 * 외부 연동 로그를 사용할 수 있게끔 등록 처리
	 */
	public static final void checkExtention(){
		for(String el : list){
			try{
				((ExtentionLogger)Class.forName(el).newInstance()).useLogger();
			}catch(Throwable e){
				// 해당 라이브러리가 추가되지 않았다면 발생한다
			}
		}
	}
	
}
