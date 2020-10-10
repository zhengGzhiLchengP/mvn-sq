import cn.hutool.core.lang.Console;
import com.feiqu.generator.util.StringUtil;

public class Test {

    @org.junit.Test
    public void test(){
        String  s = StringUtil.lineToHump("cpu");
        Console.log(s);
    }
}
