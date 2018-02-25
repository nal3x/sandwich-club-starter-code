package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.CollapsingToolbarLayout;
import android.util.Log;
import android.view.Menu;
import android.view.View;
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
        Sandwich sandwich = null;
        try {
            sandwich = JsonUtils.parseSandwichJson(json);

            Log.d(DetailActivity.class.getSimpleName(),
                    "\n name" + sandwich.getMainName() +
            "\n Origin: " + sandwich.getPlaceOfOrigin() +
            "\n Description " + sandwich.getDescription() +
            "\n Also Known As: " + sandwich.getAlsoKnownAs().toString() +
            "\n Ingredients: " + sandwich.getIngredients().toString());


        } catch (JSONException e) {
            e.printStackTrace();
            closeOnError();
            return;
        }

        populateUI(sandwich);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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


        /*Constructing a String for ingredients, removing the last comma and ...*/
        String ingredients = "";
        for (String ingredient : sandwich.getIngredients()) {
            ingredients += ingredient + ", ";
        }
        ingredients = ingredients.replaceAll(", $", "");
        ingredientsTv.setText(ingredients);

        String alsoKnownAs = "";
        for (String alternativeName : sandwich.getAlsoKnownAs()) {
            alsoKnownAs += alternativeName + ", ";
        }
        alsoKnownAs = alsoKnownAs.replaceAll(", $", "");

        if (alsoKnownAs.isEmpty())
            alsoKnownTv.setVisibility(View.GONE);

        else
            alsoKnownTv.setText(alsoKnownAs);

    }
}
