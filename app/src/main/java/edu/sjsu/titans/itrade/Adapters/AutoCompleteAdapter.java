package edu.sjsu.titans.itrade.Adapters;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by pavan on 5/5/2017.
 */

public class AutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {
    private ArrayList<String> mData;

    public AutoCompleteAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        mData = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public String getItem(int index) {
        return mData.get(index);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if(constraint != null && constraint.toString() != "") {
                    String url = "http://homework-8-1267.appspot.com/getInfoMobile.php?input=" + constraint.toString();
                    // TODO: Get stock list from input...
                    AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
                    asyncHttpClient.get(url, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int i, Header[] headers, byte[] bytes) {
                            JSONObject result;
                            try {
                                result = new JSONObject(new String(bytes));
                                try {
                                    JSONArray resultList = result.getJSONArray("LookupResult");
                                    mData = new ArrayList<>();
                                    for (int j = 0; j < resultList.length(); j++) {
                                        mData.add(resultList.getJSONObject(j).getString("Symbol")
                                                + " - " + resultList.getJSONObject(j).getString("Name")
                                                + " - " + resultList.getJSONObject(j).getString("Exchange"));
                                        System.out.println(resultList.getJSONObject(j).getString("Symbol")
                                                + " - " + resultList.getJSONObject(j).getString("Name")
                                                + " - " + resultList.getJSONObject(j).getString("Exchange"));
                                    }
                                } catch (JSONException e) {
                                    try {
                                        JSONObject resultList = result.getJSONObject("LookupResult");
                                        mData = new ArrayList<>();
                                        mData.add(resultList.getString("Symbol")
                                                + " - " + resultList.getString("Name")
                                                + " - " + resultList.getString("Exchange"));
                                        System.out.println(resultList.getString("Symbol")
                                                + " - " + resultList.getString("Name")
                                                + " - " + resultList.getString("Exchange"));
                                    } catch (JSONException e1) {
                                        e1.printStackTrace();
                                        return;
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                return;
                            }
                        }

                        @Override
                        public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                            System.out.println(new String(bytes));
                        }
                    });
                    filterResults.values = mData;
                    filterResults.count = mData.size();
                    notifyDataSetChanged();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                System.out.println(results.count);
                if(results != null && results.count > 0) {
                    notifyDataSetChanged();
                }
                else {
                    notifyDataSetInvalidated();
                }
            }
        };
    }
}
