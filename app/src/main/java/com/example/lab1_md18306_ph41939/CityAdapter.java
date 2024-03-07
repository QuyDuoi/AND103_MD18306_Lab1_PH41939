package com.example.lab1_md18306_ph41939;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder> {
    private List<City> cities;
    private Context context;

    public CityAdapter(Context context, List<City> cities) {
        this.context = context;
        this.cities = cities;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_city, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        City city = cities.get(position);
        holder.name.setText(city.getName());
        holder.state.setText(city.getState()==null?"N/a":""+city.getState());
        holder.country.setText(city.getCountry());
        holder.capital.setText(city.isCapital()?"(Capital)":"");
        holder.population.setText(String.valueOf(city.getPopulation()));
        String regionsStr = "";
        List<String> regions = city.getRegions();
        for (int i = 0; i < regions.size(); i++) {
            regionsStr += regions.get(i);
            if (i < regions.size() - 1) {
                regionsStr += ", ";
            }
        }
        holder.regions.setText(regionsStr);
    }

    @Override
    public int getItemCount() {
        return cities.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, state, country, capital, population, regions;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nameCity);
            state = itemView.findViewById(R.id.stateCity);
            capital = itemView.findViewById(R.id.capital);
            country = itemView.findViewById(R.id.countryCity);
            population = itemView.findViewById(R.id.population);
            regions = itemView.findViewById(R.id.regions);
        }
    }
}
