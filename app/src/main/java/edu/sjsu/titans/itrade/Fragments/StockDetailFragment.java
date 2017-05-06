package edu.sjsu.titans.itrade.Fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import edu.sjsu.titans.itrade.Activities.StockContentActivity;
import edu.sjsu.titans.itrade.Adapters.StockDetailCellAdapter;
import edu.sjsu.titans.itrade.R;
import edu.sjsu.titans.itrade.Utils.NonScrollableListView;

/**
 * Created by pavan on 5/5/2017.
 */

public class StockDetailFragment  extends android.support.v4.app.Fragment {
    public String symbol;

    private StockDetailCellAdapter mAdapter;
    private ListView stockDetailListView;
    private ImageView stockDetailChart;
    private String[] mStockDetailContents = {
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    public StockDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StockContentActivity parentActivity = (StockContentActivity) getActivity();
        this.symbol = parentActivity.symbol;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentRootView = inflater.inflate(R.layout.fragment_stock_detail, container, false);
        stockDetailListView = (NonScrollableListView) fragmentRootView.findViewById(R.id.stockDetailListView);
        stockDetailChart = (ImageView) fragmentRootView.findViewById(R.id.stockDetailChartImageView);

        mAdapter = new StockDetailCellAdapter(this.getContext(), mStockDetailContents);
        stockDetailListView.setAdapter(mAdapter);
        return fragmentRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getStockDetail();
        getStockDetailChart();
    }

    private void getStockDetail() {
        if (symbol != null && !symbol.equals("")) {
            AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
            asyncHttpClient.get(getString(R.string.url_get_stock_detail) + symbol, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                    JSONObject result;
                    try {
                        result = new JSONObject(new String(bytes));
                        mStockDetailContents[0] = result.getString("name");
                        mStockDetailContents[1] = result.getString("symbol");
                        mStockDetailContents[2] = result.getString("lastPrice");
                        mStockDetailContents[3] = result.getString("change")
                                + " (" + result.getString("changePercent") + ")";
                        mStockDetailContents[4] = result.getString("timeAndDate");
                        mStockDetailContents[5] = result.getString("marketCap");
                        mStockDetailContents[6] = result.getString("volume");
                        mStockDetailContents[7] = result.getString("changeYTD")
                                + " (" + result.getString("changePercentYTD") + ")";
                        mStockDetailContents[8] = result.getString("highPrice");
                        mStockDetailContents[9] = result.getString("lowPrice");
                        mStockDetailContents[10] = result.getString("openingPrice");

                        mAdapter.refreshData(mStockDetailContents);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return;
                    }
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

    private void getStockDetailChart() {
        int imageViewWidth = getContext().getResources().getDisplayMetrics().widthPixels;
        int imageViewHeight = imageViewWidth / 3 * 2;
        ViewGroup.LayoutParams layoutParams = stockDetailChart.getLayoutParams();
        layoutParams.height = imageViewHeight;
        layoutParams.width = imageViewWidth;
        stockDetailChart.setLayoutParams(layoutParams);

        if (symbol != null && !symbol.equals("")) {
            String url = getString(R.string.url_get_stock_detail_chart_prefix) + symbol
                    + getString(R.string.url_get_stock_detail_chart_suffix);
            AsyncHttpClient client= new AsyncHttpClient();
            client.get(url, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    if (statusCode == 200) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(responseBody, 0, responseBody.length);
                        stockDetailChart.setImageBitmap(bitmap);
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers,
                                      byte[] responseBody, Throwable error) {
                    try {
                        Toast.makeText(getContext(), "Cannot load stock chart image.", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    error.printStackTrace();
                }
            });
        }
    }
}
