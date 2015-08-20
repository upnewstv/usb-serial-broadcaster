package net.signagewidgets.serial.model;

import net.signagewidgets.serial.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by lenoirzamboni on 8/20/15.
 */
public class RemoteControl {

        String name;
        String date;
        int typeIcon;

    public RemoteControl(String name, String date, int typeIcon) {
            this.name = name;
            this.date = date;
            this.typeIcon = typeIcon;
    }

    private List<RemoteControl> remoteControls;

    public String getName(){
        return name;
    }

    public String getDate(){
        return date;
    }

    public int getTypeIcon(){
        return typeIcon;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setDate(String date){
        this.date = date;
    }

    public void setTypeIcon(int typeIcon){
        this.typeIcon = typeIcon;
    }

    private void initializeData(){
        remoteControls = new ArrayList<>();
        remoteControls.add(new RemoteControl("Control A", new SimpleDateFormat("dd-MM-yyyy").format(new Date()), R.drawable.remote_example));
        remoteControls.add(new RemoteControl("Control B", new SimpleDateFormat("dd-MM-yyyy").format(new Date()), R.drawable.remote_example));
        remoteControls.add(new RemoteControl("Control C", new SimpleDateFormat("dd-MM-yyyy").format(new Date()), R.drawable.remote_example));
    }
}
