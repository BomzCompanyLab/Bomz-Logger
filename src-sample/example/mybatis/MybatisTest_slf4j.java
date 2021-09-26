package example.mybatis;

import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/*
 * 
 * MyBatis 로그를 slf4j logger 를 이용해 연동
 * 모든 SQL 쿼리 출력 시 level 값을 debug 로 해준다
 * 예)	<logger name="org.apache.ibatis" type="console" level="debug" /> 
 * 
 *
 */
public class MybatisTest_slf4j {

	public static void main(String[] args) throws Exception{
		
		kr.co.bomz.logger.Logger.setLogConfigFile("./conf/mybatis/logger.xml");
		Logger logger = LoggerFactory.getLogger("mybatisTest");
		
		SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsReader("example/mybatis/config.xml"));
		SqlSession session = factory.openSession();
		List<PeopleVO> list = session.selectList("people.selectPeople");
		
		logger.info("select start... to slf4j logger");
		for(PeopleVO vo : list ){
			logger.debug("{}, : {}, : {}",vo.getName(), vo.getAge(), vo.getAddr());
		}
		logger.info("select end... to slf4j logger");
	}

}
