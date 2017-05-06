package edu.sjsu.titans.itrade.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import edu.sjsu.titans.itrade.R;

/**
 * Created by pavan on 5/5/2017.
 */

public class StockDetailCellAdapter extends BaseAdapter {

    private String[] mStockDetailTitles = {
            "Name",
            "Symbol",
            "Last Price",
            "Change",
            "Time and Date",
            "Market Cap",
            "Volume",
            "Change YTD",
            "High Price",
            "Low Price",
            "Opening Price"
    };
    private String[] mStockDetailContents;
    private static LayoutInflater inflater;

    public StockDetailCellAdapter(Context context, String[] stockDetailContents) {
        this.mStockDetailContents = stockDetailContents;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mStockDetailTitles.length;
    }

    @Override
    public Object getItem(int position) {
        return mStockDetailContents[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class StockDetailCellHolder {
        TextView stockDetailCellTitle;
        TextView stockDetailCellContent;
        ImageView stockDetailCellImageView;
    }

    @Override
    public View getView (final int position, View convertView, ViewGroup parent) {
        StockDetailCellHolder holder;

        if (convertView == null) {
            holder = new StockDetailCellHolder();
            convertView = inflater.inflate(R.layout.list_cell_stock_detail, parent, false);
            holder.stockDetailCellTitle = (TextView) convertView.findViewById(R.id.stockDetailCellTitle);
            holder.stockDetailCellContent = (TextView) convertView.findViewById(R.id.stockDetailCellContent);
            holder.stockDetailCellImageView = (ImageView) convertView.findViewById(R.id.stockDetailCellImageView);
            convertView.setTag(holder);
        } else {
            holder = (StockDetailCellHolder) convertView.getTag();
        }

        holder.stockDetailCellTitle.setText(mStockDetailTitles[position]);
        holder.stockDetailCellContent.setText(mStockDetailContents[position]);

        return convertView;
    }

    public void refreshData (String[] newContentList) {
        this.mStockDetailContents = newContentList;
        notifyDataSetChanged();
    }
}
