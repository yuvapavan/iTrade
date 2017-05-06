package edu.sjsu.titans.itrade.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import edu.sjsu.titans.itrade.Activities.StockContentActivity;
import edu.sjsu.titans.itrade.Adapters.StockNewsCellAdapter;
import edu.sjsu.titans.itrade.R;

/**
 * Created by pavan on 5/5/2017.
 */

public class StockNewsFragment extends android.support.v4.app.Fragment {
    public String symbol;
    private ListView stockNewsListView;
    private StockNewsCellAdapter mAdapter;

    private ArrayList<String> mStockNewsTitles;
    private ArrayList<String> mStockNewsContents;
    private ArrayList<String> mStockNewsPublishers;
    private ArrayList<String> mStockNewsDates;
    private ArrayList<String> mStockNewsUrls;

    public StockNewsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StockContentActivity parentActivity = (StockContentActivity) getActivity();
        this.symbol = parentActivity.symbol;

        this.mStockNewsTitles = new ArrayList<>();
        this.mStockNewsContents = new ArrayList<>();
        this.mStockNewsPublishers = new ArrayList<>();
        this.mStockNewsDates = new ArrayList<>();
        this.mStockNewsUrls = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_stock_news, container, false);

        this.stockNewsListView = (ListView) rootView.findViewById(R.id.stockNewsListView);
        mAdapter = new StockNewsCellAdapter(this.getContext());
        stockNewsListView.setAdapter(mAdapter);
        stockNewsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mStockNewsUrls.get(position)));
                startActivity(browserIntent);
            }
        });
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getStockNews();
    }

    private void getStockNews() {
        if (symbol != null && !symbol.equals("")) {
            AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
            asyncHttpClient.get(getString(R.string.url_get_stock_news) + symbol, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                    mStockNewsTitles.clear();
                    mStockNewsContents.clear();
                    mStockNewsPublishers.clear();
                    mStockNewsDates.clear();
                    mStockNewsUrls.clear();
                    JSONObject result;
                    try {
                        result = new JSONObject(new String(bytes));
                        System.out.println(result);
                        try {
                            JSONArray newsList = result.getJSONArray("results");
                            System.out.println(newsList);
                            for (int j = 0; j < newsList.length(); j++) {
                                try {
                                    JSONObject news = newsList.getJSONObject(j);
                                    mStockNewsTitles.add(news.getString("title"));
                                    mStockNewsContents.add(news.getString("description"));
                                    mStockNewsPublishers.add(news.getString("publisher"));
                                    mStockNewsDates.add(news.getString("date"));
                                    mStockNewsUrls.add(news.getString("url"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (JSONException e) {
                            try {
                                JSONObject newsObject = result.getJSONObject("results");
                                mStockNewsTitles.add(newsObject.getString("title"));
                                mStockNewsContents.add(newsObject.getString("description"));
                                mStockNewsPublishers.add(newsObject.getString("publisher"));
                                mStockNewsDates.add(newsObject.getString("date"));
                                mStockNewsUrls.add(newsObject.getString("url"));
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                            e.printStackTrace();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return;
                    }
                    mAdapter.refreshData(mStockNewsTitles, mStockNewsContents, mStockNewsPublishers, mStockNewsDates);
                }

                @Override
                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                    try {
                        Toast.makeText(getContext(), "Stock detail not available.", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
