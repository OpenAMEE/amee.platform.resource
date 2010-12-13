package com.amee.platform.resource.dataitem.v_3_2;

import com.amee.base.domain.Since;
import com.amee.base.resource.ResponseHelper;
import com.amee.domain.data.DataItem;
import com.amee.domain.data.ItemDefinition;
import com.amee.domain.data.ItemValue;
import com.amee.platform.resource.dataitem.DataItemResource;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
@Since("3.2.0")
public class DataItemJSONRenderer_3_2_0 implements DataItemResource.Renderer {

    protected DataItem dataItem;
    protected JSONObject rootObj;
    protected JSONObject dataItemObj;
    protected JSONArray valuesArr;

    public void start() {
        rootObj = new JSONObject();
    }

    public void ok() {
        ResponseHelper.put(rootObj, "status", "OK");
    }

    public void newDataItem(DataItem dataItem) {
        this.dataItem = dataItem;
        dataItemObj = new JSONObject();
        if (rootObj != null) {
            ResponseHelper.put(rootObj, "item", dataItemObj);
        }
    }

    public void addBasic() {
        ResponseHelper.put(dataItemObj, "uid", dataItem.getUid());
    }

    public void addName() {
        ResponseHelper.put(dataItemObj, "name", dataItem.getName());
    }

    public void addLabel() {
        ResponseHelper.put(dataItemObj, "label", dataItem.getLabel());
    }

    public void addPath() {
        ResponseHelper.put(dataItemObj, "path", dataItem.getPath());
        ResponseHelper.put(dataItemObj, "fullPath", dataItem.getFullPath());
    }

    public void addParent() {
        ResponseHelper.put(dataItemObj, "categoryUid", dataItem.getDataCategory().getUid());
        ResponseHelper.put(dataItemObj, "categoryWikiName", dataItem.getDataCategory().getWikiName());
    }

    public void addAudit() {
        ResponseHelper.put(dataItemObj, "status", dataItem.getStatus().getName());
        ResponseHelper.put(dataItemObj, "created", DATE_FORMAT.print(dataItem.getCreated().getTime()));
        ResponseHelper.put(dataItemObj, "modified", DATE_FORMAT.print(dataItem.getModified().getTime()));
    }

    public void addWikiDoc() {
        ResponseHelper.put(dataItemObj, "wikiDoc", dataItem.getWikiDoc());
    }

    public void addProvenance() {
        ResponseHelper.put(dataItemObj, "provenance", dataItem.getProvenance());
    }

    public void addItemDefinition(ItemDefinition itemDefinition) {
        JSONObject itemDefinitionObj = new JSONObject();
        ResponseHelper.put(itemDefinitionObj, "uid", itemDefinition.getUid());
        ResponseHelper.put(itemDefinitionObj, "name", itemDefinition.getName());
        ResponseHelper.put(dataItemObj, "itemDefinition", itemDefinitionObj);
    }

    public void startValues() {
        valuesArr = new JSONArray();
        ResponseHelper.put(dataItemObj, "values", valuesArr);
    }

    public void newValue(ItemValue itemValue) {
        JSONObject valueObj = new JSONObject();
        ResponseHelper.put(valueObj, "path", itemValue.getPath());
        ResponseHelper.put(valueObj, "value", itemValue.getValue());
        if (itemValue.hasUnit()) {
            ResponseHelper.put(valueObj, "unit", itemValue.getUnit().toString());
        }
        if (itemValue.hasPerUnit()) {
            ResponseHelper.put(valueObj, "perUnit", itemValue.getPerUnit().toString());
            ResponseHelper.put(valueObj, "compoundUnit", itemValue.getCompoundUnit().toString());
        }
        ResponseHelper.put(valueObj, "history", itemValue.isHistoryAvailable());
        valuesArr.put(valueObj);
    }

    public String getMediaType() {
        return "application/json";
    }

    public Object getObject() {
        return rootObj;
    }
}