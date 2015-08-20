package net.signagewidgets.serial.util;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.signagewidgets.serial.R;

import net.signagewidgets.serial.model.RemoteControl;

/**
 * Created by lenoirzamboni on 8/20/15.
 */

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder> {
        private RemoteControl [] remoteControls;

        public RVAdapter(RemoteControl[] remoteControls) {
            this.remoteControls = remoteControls;
        }

        // For each item

        // Inflate item_layout
        // Create a ViewHolder
        // Bind the view “ViewHolder” with data “remoteControls”

        // Create new views (invoked by the layout manager)
        @Override
        public RVAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            // create a new view (decide which type of view should be created!!!)
            View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_control_2_buttons, null);

            // create ViewHolder
            ViewHolder viewHolder = new ViewHolder(itemLayoutView);
            return viewHolder;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {

            // - get data from your remoteControls at this position
            // - replace the contents of the view with that remoteControls

            viewHolder.controlName.setText(remoteControls[position].getName());
            viewHolder.dateAdd.setText(remoteControls[position].getDate());
            viewHolder.iconControl.setImageResource(remoteControls[position].getTypeIcon());
        }

        // inner class to hold a reference to each item of RecyclerView
        public static class ViewHolder extends RecyclerView.ViewHolder {

            public TextView controlName;
            public TextView dateAdd;
            public ImageView iconControl;

            public ViewHolder(View itemLayoutView) {
                super(itemLayoutView);
                controlName = (TextView) itemLayoutView.findViewById(R.id.name_control);
                dateAdd = (TextView) itemLayoutView.findViewById(R.id.date_add);
                iconControl = (ImageView) itemLayoutView.findViewById(R.id.icon_control);
            }
        }

        // Return the size of your remoteControls (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return remoteControls.length;
        }
    }
