package net.signagewidgets.serial.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lenoirzamboni on 8/20/15.
 */
public class RemoteControl implements Serializable{

        String name;
        String date;
        long idControl;
        List<Long> idButtons;

    public RemoteControl(String name, String date, long idControl, List<Long> idButtons) {
        this.name = name;
        this.date = date;
        this.idControl = idControl;
        this.idButtons = idButtons;
    }

    private List<RemoteControl> remoteControls;

    /**
     * Get the name of the remote control
     * @return Return a string that is the name of the remote control
     */
    public String getName(){
        return name;
    }

    /**
     * Get the date when the remote control was registered
     * @return Return a string that is the date when the remote control was registered
     */
    public String getDate(){
        return date;
    }

    /**
     * Get the ID of remote control
     * @return Return an int that is ID of remote control
     */
    public long getIdControl(){
        return idControl;
    }

    /**
     * Get the ID of each button of remote control
     * @return Return a List of Integer that each one is the ID of each button of remote control
     */
    public List<Long> getIdButtons(){
        return idButtons;
    }

    /**
     * Set the name of the remote control
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * Set the date when the remote control was added
     */
    public void setDate(String date){
        this.date = date;
    }

    /**
     * Set the ID remote control
     */
    public void setIdControl(int idControl){
        this.idControl = idControl;
    }

    /**
     * Set the ID for each button of the remote control
     */
    public void setIdButtons(List<Long> idButtons){
        this.idButtons = idButtons;
    }
}
