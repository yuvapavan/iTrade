package edu.sjsu.titans.itrade.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

import cz.msebera.android.httpclient.Header;
import edu.sjsu.titans.itrade.Adapters.AutoCompleteAdapter;
import edu.sjsu.titans.itrade.Adapters.FavouriteStockCellAdapter;
import edu.sjsu.titans.itrade.R;

public class MainActivity extends AppCompatActivity {
    private ArrayList<String> mFavoriteListSymbols;
    private ArrayList<String> mFavoriteListPrices;
    private ArrayList<String> mFavoriteListChanges;
    private ArrayList<String> mFavoriteListNames;
    private ArrayList<String> mFavoriteListMarketCaps;

    private AutoCompleteTextView autoCompleteTextView;
    private ListView favoriteStocksListView;

    private FavouriteStockCellAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initAutoCompleteTextView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initFavoriteStocksListView();
        refreshFavoriteStocksList();
    }

    private void initAutoCompleteTextView() {
        this.autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);

        AutoCompleteAdapter adapter = new AutoCompleteAdapter(this, android.R.layout.simple_dropdown_item_1line);
        autoCompleteTextView.setThreshold(0);
        autoCompleteTextView.setAdapter(adapter);
    }

    private void initFavoriteStocksListView() {
        this.favoriteStocksListView = (ListView) findViewById(R.id.favoriteStocksListView);
        TreeSet<String> favoritesSet = new TreeSet<>(getSharedPreferences(getString(R.string.default_shared_preferences_key), MODE_PRIVATE)
                .getStringSet(getString(R.string.favorites_key), new TreeSet<String>()));
        this.mFavoriteListSymbols = new ArrayList<>();
        this.mFavoriteListPrices = new ArrayList<>();
        this.mFavoriteListChanges = new ArrayList<>();
        this.mFavoriteListNames = new ArrayList<>();
        this.mFavoriteListMarketCaps = new ArrayList<>();

        Iterator<String> iterator = favoritesSet.iterator();
        while (iterator.hasNext()) {
            mFavoriteListSymbols.add(iterator.next());
        }

        for (int i = 0; i < mFavoriteListSymbols.size(); i++) {
            mFavoriteListPrices.add("");
            mFavoriteListChanges.add("");
            mFavoriteListNames.add("");
            mFavoriteListMarketCaps.add("");
        }

        this.mAdapter = new FavouriteStockCellAdapter(this,
                mFavoriteListSymbols,
                mFavoriteListPrices,
                mFavoriteListChanges,
                mFavoriteListNames,
                mFavoriteListMarketCaps);
        favoriteStocksListView.setAdapter(mAdapter);
        favoriteStocksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getQuote(mFavoriteListSymbols.get(position));
            }
        });
    }

    private void refreshFavoriteStocksList() {
        for (int i = 0; i < mFavoriteListSymbols.size(); i++) {
            AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
            asyncHttpClient.get(getString(R.string.url_get_stock_detail) + mFavoriteListSymbols.get(i), new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                    JSONObject result;
                    try {
                        result = new JSONObject(new String(bytes));
                        int index = mFavoriteListSymbols.indexOf(result.getString("symbol"));
                        if (index >= 0) {
                            mFavoriteListPrices.set(index, result.getString("lastPrice"));
                            mFavoriteListChanges.set(index, result.getString("change")
                                    + " (" + result.getString("changePercent") + ")");
                            mFavoriteListNames.set(index, result.getString("name"));
                            mFavoriteListMarketCaps.set(index, "Market Cap: " + result.getString("marketCap"));
                            mAdapter.refreshData(
                                    mFavoriteListPrices,
                                    mFavoriteListChanges,
                                    mFavoriteListNames,
                                    mFavoriteListMarketCaps);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                    try {
                        Toast.makeText(MainActivity.this, "Refresh favorite list failed.", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public void getQuote(View v) {
        String symbol = autoCompleteTextView.getText().toString().split(" - ")[0];
        if (symbol != null && !symbol.equals("")) {
            Intent intent = new Intent(MainActivity.this, StockContentActivity.class);
            intent.putExtra(getString(R.string.symbol_key), symbol);
            startActivity(intent);
        } else {
            Toast.makeText(MainActivity.this, "Please enter a valid symbol.", Toast.LENGTH_SHORT).show();
        }
    }

    public void getQuote(String symbol) {
        if (symbol != null && !symbol.equals("")) {
            Intent intent = new Intent(MainActivity.this, StockContentActivity.class);
            intent.putExtra(getString(R.string.symbol_key), symbol);
            startActivity(intent);
        } else {
            Toast.makeText(MainActivity.this, "Please enter a valid symbol.", Toast.LENGTH_SHORT).show();
        }
    }

    public void clearInput(View v) {
        autoCompleteTextView.setText("");
    }


    public void refreshButtonClicked(View v) {
        initFavoriteStocksListView();
        refreshFavoriteStocksList();
    }
}
