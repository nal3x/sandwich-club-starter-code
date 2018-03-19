package com.udacity.sandwichclub.utils;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    private static final String KEY_NAME = "name";
    private static final String KEY_MAIN_NAME = "mainName";
    private static final String KEY_ALSO_KNOWN_AS = "alsoKnownAs";
    private static final String KEY_PLACE_OF_ORIGIN = "placeOfOrigin";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_INGREDIENTS = "ingredients";
    private static final String KEY_NO_JSON_VALUE_FOUND = "VALUE NOT FOUND";


    public static Sandwich parseSandwichJson(String json) throws JSONException {

        JSONObject sandwichJson = new JSONObject(json);
        JSONObject sandwichName = sandwichJson.getJSONObject(KEY_NAME);

        String mainName = sandwichName.optString(KEY_MAIN_NAME, KEY_NO_JSON_VALUE_FOUND);

        List<String> alsoKnownAs = convertJsonArrayToList(sandwichName.getJSONArray(KEY_ALSO_KNOWN_AS));

        String placeOfOrigin = sandwichJson.optString(KEY_PLACE_OF_ORIGIN, KEY_NO_JSON_VALUE_FOUND);

        String description = sandwichJson.optString(KEY_DESCRIPTION, KEY_NO_JSON_VALUE_FOUND);

        String image = sandwichJson.optString(KEY_IMAGE);

        List<String> ingredients = convertJsonArrayToList(sandwichJson.getJSONArray(KEY_INGREDIENTS));

        return new Sandwich(mainName, alsoKnownAs, placeOfOrigin, description, image, ingredients);

    }

    /*helper method which takes a JSONArray and converts it to a list of strings*/

    private static List<String> convertJsonArrayToList(JSONArray jsonArray) {
        List<String> listFromJson = new ArrayList<>();
        if (jsonArray.length() ==0 ) {
            listFromJson.add("");
        }
        else {
            for (int i = 0; i < jsonArray.length(); i++) {
                listFromJson.add(jsonArray.optString(i, KEY_NO_JSON_VALUE_FOUND));
            }
        }
        return listFromJson;
    }
}
