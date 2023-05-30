package mvc.example;

import com.alibaba.fastjson.JSONObject;
import mvc.container.annotation.SimpleController;
import mvc.sevlet.SimpleModelAndView;
import mvc.sevlet.annotation.ResponseBody;
import mvc.sevlet.annotation.RequestMapping;
import mvc.sevlet.annotation.RequestParam;

/**
 * TestController
 *
 * @author cc
 * @description
 * @since 2023/5/29 14:00
 */
@SimpleController
@RequestMapping(value = "/test")
public class TestController {

    @ResponseBody
    @RequestMapping(value = "/retStr")
    public String retStr() {
        return "hello world";
    }

    @RequestMapping(value = "/hello")
    public SimpleModelAndView hello() {
        SimpleModelAndView simpleModelAndView = new SimpleModelAndView();
        simpleModelAndView.setView("/test/say");
        simpleModelAndView.addObj("str", "world");
        return simpleModelAndView;
    }

    @ResponseBody
    @RequestMapping(value = "/say")
    public String say(@RequestParam String str) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("msg", "hello " + str);
        return jsonObject.toJSONString();
    }
}
