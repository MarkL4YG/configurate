package de.mlessmann.config;

import de.mlessmann.config.except.RootMustStayHubException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Life4YourGames on 18.07.16.
 */
public class ConfigNode {

    private String key;
    private Object value;
    private ConfigNode parent;

    public ConfigNode() {

        //New empty root node
        value = new HashMap<String, ConfigNode>();

    }

    public ConfigNode(ConfigNode parent, String key) {

        this.parent = parent;
        this.key = key;

    }

    public ConfigNode(ConfigNode parent, String key, Object value) {

        this.parent = parent;
        this.key = key;
        this.value = value;

    }

    private boolean isHub() { return value instanceof Map; }

    public Boolean isVirtual() { return value == null; }

    public Boolean isType(Class<?> cls) { return cls.isInstance(value); }

    public ConfigNode getParent() {
        return parent;
    }

    public ConfigNode getRoot() {
        if (parent!=null)
            return getParent().getRoot();
        return this;
    }

    //------------------------------------------------------------------------------------------------------------------

    public void addNode(ConfigNode node) {

        if (!(value instanceof Map))
            value = new HashMap<String, ConfigNode>();

        Map m = (Map) value;
        m.put(node.getKey(), node);

    }

    public ConfigNode getNode(String... keys) {

        if (keys.length < 1) return this;

        ConfigNode node = getOrCreateNode(keys[0]);

        if (keys.length > 1) {

            String[] newKeys = Arrays.copyOfRange(keys, 1, keys.length);

            return node.getNode(newKeys);

        } else {
            return node;
        }

    }

    private ConfigNode getOrCreateNode(String key) {

        if (isHub()) {
            Map m = (Map) value;

            if (m.containsKey(key)) {
                if (m.get(key) instanceof ConfigNode) {
                    //Do nothing
                } else {
                    m.remove(key);
                    addNode(new ConfigNode(this, key, null));
                }
                return (ConfigNode) m.get(key);
            }

        }

        ConfigNode n = new ConfigNode(this, key, null);
        addNode(n);

        return n;

    }



    //------------------------------------------------------------------------------------------------------------------

    public String getKey() { return key; }

    public Object getValue() { return value; }

    public void setValue(Object value) throws RootMustStayHubException {
        if (key == null)
            throw new RootMustStayHubException();
        else
            this.value = value;
    }

    public String getString() { return (String) value; }
    public String optString(String def) { return value == null || !(value instanceof String) ? def : getString(); }
    public void setString(String s) { value = s; }

    public Integer getInt() { return (Integer) value; }
    public Integer optInt(Integer def) { return value == null || !(value instanceof Integer) ? def : getInt(); }
    public void setInt(Integer i) { value = i; }

    public Long getLong() { return (Long) value; }
    public Long optLong(Long def) { return value == null || !(value instanceof Long) ? def : getLong(); }
    public void setLong(Long l) { value = l; }

    public Double getDouble() { return (Double) value; }
    public Double optDouble(Double def) { return value == null || !(value instanceof Double) ? def : getDouble(); }
    public void setDouble(Double d) { value = d; }

    public Boolean getBoolean() { return (Boolean) value; }
    public Boolean optBoolean(Boolean def) { return value == null || !(value instanceof Boolean) ? def : getBoolean(); }
    public void setBoolean(Boolean b) { value = b; }

    public BigInteger getBigInt() { return (BigInteger) value; }
    public BigInteger optBigInt(BigInteger def) { return value == null || !(value instanceof BigInteger) ? def : getBigInt(); }
    public void setBigInt(BigInteger b) { value = b; }

    public List getList() { return (List) value; }
    public List optList(List def) { return value == null || !(value instanceof List) ? def : getList(); }
    public void setList(List l) { value = l; }


    //------------------------------------------------------------------------------------------------------------------

}