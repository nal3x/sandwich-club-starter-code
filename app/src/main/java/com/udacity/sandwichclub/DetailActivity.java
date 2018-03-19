package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.CollapsingToolbarLayout;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import org.json.JSONException;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;
    @BindView (R.id.image_iv) ImageView ingredientsIv;
    @BindArray (R.array.sandwich_details) String[] sandwiches;
    @BindView (R.id.origin_tv) TextView originTv;
    @BindView (R.id.description_tv) TextView descriptionTv;
    @BindView (R.id.ingredients_tv) TextView ingredientsTv;
    @BindView (R.id.also_known_tv) TextView alsoKnownTv;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView (R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String json = sandwiches[position];
        Sandwich sandwich;
        try {
            sandwich = JsonUtils.parseSandwichJson(json);
        } catch (JSONException e) {
            e.printStackTrace();
            closeOnError();
            return;
        }
        populateUI(sandwich);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich sandwich) {
        Picasso.with(this).load(sandwich.getImage()).into(ingredientsIv);
        collapsingToolbar.setTitle(sandwich.getMainName());
        originTv.setText(sandwich.getPlaceOfOrigin());
        descriptionTv.setText(sandwich.getDescription());
        /*Constructing a String for ingredients separated by new line*/
        StringBuilder ingredients = new StringBuilder();
        for (String ingredient : sandwich.getIngredients()) {
            ingredients.append(ingredient).append("\n");
        }
        ingredients.deleteCharAt(ingredients.length() - 1);
        ingredientsTv.setText(ingredients.toString());

        /*Using StringBuilder for alternative sandwich names separated by commas*/
        StringBuilder alsoKnownAs = new StringBuilder();
        for (String alternativeName : sandwich.getAlsoKnownAs()) {
            alsoKnownAs.append(alternativeName).append(", ");
        }
        if (alsoKnownAs.length() > 0) { //remove last comma & space if present
            alsoKnownAs.delete(alsoKnownAs.length() - 2, alsoKnownAs.length());
        }
        alsoKnownTv.setText(alsoKnownAs.toString());
    }
}
