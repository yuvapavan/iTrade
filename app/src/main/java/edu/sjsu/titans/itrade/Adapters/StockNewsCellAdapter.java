package edu.sjsu.titans.itrade.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import edu.sjsu.titans.itrade.R;

/**
 * Created by pavan on 5/5/2017.
 */

public class StockNewsCellAdapter extends BaseAdapter {
    private ArrayList<String> mStockNewsTitles;
    private ArrayList<String> mStockNewsContents;
    private ArrayList<String> mStockNewsPublishers;
    private ArrayList<String> mStockNewsDates;
//    private ArrayList<String> mStockNewsUrls;

    private static LayoutInflater inflater;

    public StockNewsCellAdapter(Context context) {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mStockNewsTitles = new ArrayList<>();
        this.mStockNewsContents = new ArrayList<>();
        this.mStockNewsPublishers = new ArrayList<>();
        this.mStockNewsDates = new ArrayList<>();
//        this.mStockNewsUrls = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mStockNewsTitles.size();
    }

    @Override
    public Object getItem(int position) {
        return mStockNewsTitles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class StockNewsCellHolder {
        TextView stockNewsCellTitle;
        TextView stockNewsCellContent;
        TextView stockNewsCellPublisher;
        TextView stockNewsCellDate;
    }

    @Override
    public View getView (final int position, View convertView, ViewGroup parent) {
        StockNewsCellHolder holder;

        if (convertView == null) {
            holder = new StockNewsCellHolder();
            convertView = inflater.inflate(R.layout.list_cell_stock_news, parent, false);
            holder.stockNewsCellTitle = (TextView) convertView.findViewById(R.id.stockNewsCellTitle);
            holder.stockNewsCellContent = (TextView) convertView.findViewById(R.id.stockNewsCellContent);
            holder.stockNewsCellPublisher = (TextView) convertView.findViewById(R.id.stockNewsCellPublisher);
            holder.stockNewsCellDate = (TextView) convertView.findViewById(R.id.stockNewsCellDate);
            convertView.setTag(holder);
        } else {
            holder = (StockNewsCellHolder) convertView.getTag();
        }

        holder.stockNewsCellTitle.setText(mStockNewsTitles.get(position));
        holder.stockNewsCellContent.setText(mStockNewsContents.get(position));
        holder.stockNewsCellPublisher.setText(mStockNewsPublishers.get(position));
        holder.stockNewsCellDate.setText(mStockNewsDates.get(position));

        return convertView;
    }

    public void refreshData (ArrayList<String> newTitles,
                             ArrayList<String> newContents,
                             ArrayList<String> newPublishers,
                             ArrayList<String> newDates) {
        this.mStockNewsTitles = newTitles;
        this.mStockNewsContents = newContents;
        this.mStockNewsPublishers = newPublishers;
        this.mStockNewsDates = newDates;
//        this.mStockNewsUrls = newUrls;
        notifyDataSetChanged();
    }
}
