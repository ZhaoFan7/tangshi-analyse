package tangshiAnalyze.crawler.common;

import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据集合
 *包装了一个map，存储清洗的数据
 * Author:zhaofan
 * Created:2019/3/17
 */
@ToString
public class DataSet {
    //data把DOM解析，清洗之后存储的数据 eg：标题，作者，正文
    private Map<String,Object> data = new HashMap<>();

    public void putData(String key,Object value){
        this.data.put(key,value);
    }

    public Object getData(String key){
        return this.data.get(key);
    }

    //获取所有信息
    public Map<String,Object> getData(){
        return new HashMap<>(this.data);//不信上家，不信下家，防止信息被改
    }
}
