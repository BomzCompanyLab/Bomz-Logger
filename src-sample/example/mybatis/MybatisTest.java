package example.mybatis;

import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import kr.co.bomz.logger.Logger;

/*
 * 
 * MyBatis 로그 연동
 * 모든 SQL 쿼리 출력 시 level 값을 debug 로 해준다
 * 예)	<logger name="org.apache.ibatis" type="console" level="debug" /> 
 * 
 *
 */
public class MybatisTest {

	public static void main(String[] args) throws Exception{
		Logger.setLogConfigFile("./conf/mybatis/logger.xml");
		Logger logger = Logger.getLogger("mybatisTest");
		
		SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsReader("example/mybatis/config.xml"));
		SqlSession session = factory.openSession();
		List<PeopleVO> list = session.selectList("people.selectPeople");
		
		logger.info("select start...");
		for(PeopleVO vo : list ){
			logger.debug(vo.getName(), ":", vo.getAge(), ":", vo.getAddr());
		}
		logger.info("select end...");
	}

}
