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
    private static Logging sLogging = new Logging(RVAdapter.class);
    private RemoteControl [] remoteControls;

    public RVAdapter(RemoteControl[] remoteControls) {
        this.remoteControls = remoteControls;
    }

    /**
     * Inner class to hold a reference to each item of RecyclerView
     */
    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView controlName;
        public TextView dateAdd;
        public ImageView iconControl;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            controlName = (TextView) itemLayoutView.findViewById(R.id.no_controls);
            dateAdd = (TextView) itemLayoutView.findViewById(R.id.date_add);
            iconControl = (ImageView) itemLayoutView.findViewById(R.id.icon_control);
        }
    }

    /**
     * Called when RecyclerView needs a new RecyclerView.ViewHolder of the given type to represent an item
     * (Create new views (invoked by the layout manager))
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public RVAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // For each item:

        // Inflate item_layout
        // Create a ViewHolder
        // Bind the view “ViewHolder” with data “remoteControls”

        View itemLayoutView;

        switch (viewType) {

            case 1:
                // create a new view with room for 1 button
                itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_control_1_button, null);
                break;

            case 2:
                // create a new view with room for 2 buttons
                itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_control_2_buttons, null);
                break;

            case 3:
                // create a new view with room for 3 buttons
                itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_control_3_buttons, null);
                break;

            default:
                // create a new view with room for 4 buttons
                itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_control_4_buttons, null);
                break;
        }

        // create ViewHolder
        return new ViewHolder(itemLayoutView);
    }

    /**
     * Return the view type of the item at position for the purposes of view recycling
     *
     * @param position Position that contains the remote control on RemoteControl array
     * @return 2 if the remote control has 2 buttons, 4 if contains 4 buttons
     */
    @Override
    public int getItemViewType(int position) {

        int type = 0;

        if(remoteControls.length == 0){
            type = 0;
        }else{
            type = remoteControls[position].getIdButtons().size();
            sLogging.error("getItemViewType", type);
        }
        return type;
    }

    /**
     * Method that replace the contents of a view (invoked by the layout manager)
     *
     * @param viewHolder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        // - get data from your remoteControls at this position
        // - replace the contents of the view with that remoteControls

        viewHolder.controlName.setText(remoteControls[position].getName());
        viewHolder.dateAdd.setText(remoteControls[position].getDate());

        switch (remoteControls[position].getIdButtons().size()){
            case 1:
                viewHolder.iconControl.setImageResource(R.drawable.remote_example_2);
                break;

            case 2:
                viewHolder.iconControl.setImageResource(R.drawable.remote_example_2);
                break;

            case 3:
                viewHolder.iconControl.setImageResource(R.drawable.remote_example_2);
                break;

            case 4:
                viewHolder.iconControl.setImageResource(R.drawable.remote_example_2);
                break;

        }
    }

    // Return the size of your remoteControls (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return remoteControls.length;
    }
}
