package tangshiAnalyze.analyze.dao.implement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tangshiAnalyze.analyze.dao.AnalyzeDao;
import tangshiAnalyze.analyze.entity.Poetry;
import tangshiAnalyze.analyze.model.AuthorCount;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Author:zhaofan
 * Created:2019/4/19
 */
public class AnalyzeDaoImplement implements AnalyzeDao {
    private final Logger logger = LoggerFactory.getLogger(AnalyzeDaoImplement.class);

    private final DataSource dataSource;

    public AnalyzeDaoImplement(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<AuthorCount> analyzeAuthorCount() {
        List<AuthorCount> dates = new ArrayList<>();

        //try()自动关闭
        String sql = "select count(*) as count,author from poetryinfo group by author";
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                AuthorCount authorCount = new AuthorCount();
                authorCount.setAuthor(resultSet.getString("author"));
                authorCount.setCount(resultSet.getInt("count"));
                dates.add(authorCount);
            }
        }catch(SQLException e){
            logger.error("Database query occur exception {}.",e.getMessage());
        }
        return dates;
    }

    @Override
    public List<Poetry> queryAllPoetryInfo() {

        List<Poetry> dates = new ArrayList<>();
        String sql = "select title,author,dynasty,content from poetryinfo";
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                Poetry poetry = new Poetry();
                poetry.setAuthor(resultSet.getString("author"));
                poetry.setTitle(resultSet.getString("title"));
                poetry.setDynasty(resultSet.getString("dynasty"));
                poetry.setContent(resultSet.getString("content"));
                dates.add(poetry);
                //ORM框架-》对象关系映射 将java对象于关系型数据库对应起来 eg：mybatis,Spring-Data-JDBC hibernate JOOQ TopLink dbutils(算是一个工具类)
                //通过大量的反射实现
            }
        }catch(SQLException e){
            logger.error("Database query occur exception {}.",e.getMessage());
        }
        return dates;
    }
}
