package mvc.sevlet;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SimpleModelAndView
 *
 * @author cc
 * @description 存放前端显示的数据
 * @since 2023/5/26 16:32
 */
public class SimpleModelAndView {

    private String view;

    private final Map<String, Object> model;

    public SimpleModelAndView() {
        view = "";
        model = new ConcurrentHashMap<>();
    }

    public SimpleModelAndView setView(String view) {
        this.view = view;
        return this;
    }

    public String getView() {
        return view;
    }

    public Map<String, Object> getModel() {
        return model;
    }

    public void addObj(String attributeName, Object attributeValue) {
        model.put(attributeName, attributeValue);
    }

    public Object getObj(String attributeName) {
        return model.get(attributeName);
    }
}
