package com.example.android.vehicletax;

/**
 * Created by Shaakya on 12/5/2017.
 */


import android.content.Context;
import android.content.Intent;

import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.MyViewHolder> implements Filterable{

    private Context mContext;
    private List<Vehicle> vehicleList;
    private List<Vehicle> vehicleListFiltered;
    public static String getCC, getType, getCategory, getDate, getNumber;
    private VehicleAdapterListener listener;

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString =  charSequence.toString();
                if(charString.isEmpty()){
                    vehicleListFiltered = vehicleList;
                }else{
                     List<Vehicle> filteredList = new ArrayList<>();
                     for(Vehicle v : vehicleList){
                         if(v.getName().toLowerCase().contains(charString.toLowerCase()) || v.getNumber().contains(charString)){
                             filteredList.add(v);
                         }
                     }
                     vehicleListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = vehicleListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                vehicleListFiltered = (ArrayList<Vehicle>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface VehicleAdapterListener {
        void onContactSelected(Vehicle vehicle);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, number;
        public ImageView overflow;


        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            number = view.findViewById(R.id.number);
            //thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            overflow = view.findViewById(R.id.overflow);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onContactSelected(vehicleListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }


    public VehicleAdapter(Context mContext, List<Vehicle> vehicleList, VehicleAdapterListener listener) {
        this.mContext = mContext;
        this.vehicleList = vehicleList;
        this.listener = listener;
        this.vehicleListFiltered = vehicleList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vehicle_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Vehicle vehicle = vehicleListFiltered.get(position);

        holder.name.setText(vehicle.getName());
        holder.number.setText(vehicle.getNumber());

        // loading album cover using Glide library
        //Glide.with(mContext).load(vehicle.getThumbnail()).into(holder.thumbnail);

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow, position);
            }
        });
    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view, int position) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_vehicle, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener(position));
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        int position;

        public MyMenuItemClickListener(int position) {
            this.position = position;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_view_details:
                    Vehicle vehicle = vehicleList.get(position);
                    String theNumber = vehicle.getNumber();
                    Intent details = new Intent(mContext, VehicleInformation.class);
                    details.putExtra("number", theNumber);
                    mContext.startActivity(details);
                    return true;

                case R.id.action_calculate:
                    Vehicle vehicle1 = vehicleList.get(position);
                    getCC = vehicle1.getCc();
                    getType = vehicle1.getType();
                    getCategory = vehicle1.getCategory();
                    getDate = vehicle1.getDate();
                   Interface i = (Interface)mContext;
                   i.getDialog();
                    return true;

                case R.id.action_delete:
                    Vehicle vehicle2 = vehicleList.get(position);
                    getNumber = vehicle2.getNumber();
                    Interface iDelete = (Interface)mContext;
                    iDelete.getDeleteCheckDialog();
                    return true;

                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return vehicleListFiltered.size();
    }

}