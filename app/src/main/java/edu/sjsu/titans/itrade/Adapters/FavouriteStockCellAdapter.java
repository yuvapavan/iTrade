package edu.sjsu.titans.itrade.Adapters;

import android.content.Context;
import android.graphics.Color;
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

public class FavouriteStockCellAdapter  extends BaseAdapter {
    private ArrayList<String> mFavoriteStockCellSymbols;
    private ArrayList<String> mFavoriteStockCellPrices;
    private ArrayList<String> mFavoriteStockCellChanges;
    private ArrayList<String> mFavoriteStockCellNames;
    private ArrayList<String> mFavoriteStockCellMarketCaps;

    private static LayoutInflater inflater;

    public FavouriteStockCellAdapter(Context context,
                                    ArrayList<String> favoriteListSymbols,
                                    ArrayList<String> favoriteListPrices,
                                    ArrayList<String> favoriteListChanges,
                                    ArrayList<String> favoriteListNames,
                                    ArrayList<String> favoriteListMarketCaps) {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mFavoriteStockCellSymbols = favoriteListSymbols;
        this.mFavoriteStockCellPrices = favoriteListPrices;
        this.mFavoriteStockCellChanges = favoriteListChanges;
        this.mFavoriteStockCellNames = favoriteListNames;
        this.mFavoriteStockCellMarketCaps = favoriteListMarketCaps;
    }

    @Override
    public int getCount() {
        return mFavoriteStockCellSymbols.size();
    }

    @Override
    public Object getItem(int position) {
        return mFavoriteStockCellSymbols.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class StockNewsCellHolder {
        TextView favoriteStockCellSymbol;
        TextView favoriteStockCellPrice;
        TextView favoriteStockCellChange;
        TextView favoriteStockCellName;
        TextView favoriteStockCellMarketCap;
    }

    @Override
    public View getView (final int position, View convertView, ViewGroup parent) {
        StockNewsCellHolder holder;

        if (convertView == null) {
            holder = new StockNewsCellHolder();
            convertView = inflater.inflate(R.layout.list_cell_favorite_stock, parent, false);
            holder.favoriteStockCellSymbol = (TextView) convertView.findViewById(R.id.favoriteStockCellSymbol);
            holder.favoriteStockCellPrice = (TextView) convertView.findViewById(R.id.favoriteStockCellPrice);
            holder.favoriteStockCellChange = (TextView) convertView.findViewById(R.id.favoriteStockCellChange);
            holder.favoriteStockCellName = (TextView) convertView.findViewById(R.id.favoriteStockCellName);
            holder.favoriteStockCellMarketCap = (TextView) convertView.findViewById(R.id.favoriteStockCellMarketCap);
            convertView.setTag(holder);
        } else {
            holder = (StockNewsCellHolder) convertView.getTag();
        }

        holder.favoriteStockCellSymbol.setText(mFavoriteStockCellSymbols.get(position));
        holder.favoriteStockCellPrice.setText(mFavoriteStockCellPrices.get(position));
        String change = mFavoriteStockCellChanges.get(position);
        holder.favoriteStockCellChange.setText(change);
        if (change.startsWith("-")) {
            holder.favoriteStockCellChange.setBackgroundColor(Color.parseColor("#D32F2F"));
            holder.favoriteStockCellChange.setTextColor(Color.WHITE);
        } else if (change.startsWith("0.00 (") || change.startsWith("0 (")){
            holder.favoriteStockCellChange.setBackgroundColor(Color.TRANSPARENT);
            holder.favoriteStockCellChange.setTextColor(Color.BLACK);
        } else {
            holder.favoriteStockCellChange.setBackgroundColor(Color.parseColor("#43A047"));
            holder.favoriteStockCellChange.setTextColor(Color.WHITE);
        }
        holder.favoriteStockCellName.setText(mFavoriteStockCellNames.get(position));
        holder.favoriteStockCellMarketCap.setText(mFavoriteStockCellMarketCaps.get(position));

        return convertView;
    }

    public void refreshData (ArrayList<String> newPrices,
                             ArrayList<String> newChanges,
                             ArrayList<String> newNames,
                             ArrayList<String> newMarketCap) {
        this.mFavoriteStockCellPrices = newPrices;
        this.mFavoriteStockCellChanges = newChanges;
        this.mFavoriteStockCellNames = newNames;
        this.mFavoriteStockCellMarketCaps = newMarketCap;
        notifyDataSetChanged();
    }
}
