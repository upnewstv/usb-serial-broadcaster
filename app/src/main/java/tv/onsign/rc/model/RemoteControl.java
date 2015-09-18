package tv.onsign.rc.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lenoirzamboni on 8/20/15.
 */
public class RemoteControl implements Serializable{

    private String mName;
    private String mDate;
    private Integer mIDControl;
    private List<Integer> mIDButtons;

    public RemoteControl(String name, String date, Integer idControl, List<Integer> idButtons) {
        mName = name;
        mDate = date;
        mIDControl = idControl;
        mIDButtons = idButtons;
    }

    private List<RemoteControl> remoteControls;

    /**
     * Get the name of the remote control
     * @return Return a string that is the mName of the remote control
     */
    public String getName() {
        return mName;
    }

    /**
     * Get the nate when the remote control was registered
     * @return Return a string that is the mDate when the remote control was registered
     */
    public String getDate() {
        return mDate;
    }

    /**
     * Get the ID of remote control
     * @return Return an int that is ID of remote control
     */
    public long getIDControl() {
        return mIDControl;
    }

    /**
     * Get the ID of each button of remote control
     * @return Return a List of Integer that each one is the ID of each button of remote control
     */
    public List<Integer> getIdButtons() {
        return mIDButtons;
    }

    public int getIdButton(int id) {
        for (char i = 'A'; i < 'A' + mIDButtons.size(); i++) {
            if (mIDButtons.get(i - 'A') == id) {
                return i;
            }
        }
        return 0;
    }

    /**
     * Set the name of the remote control
     */
    public void setName(String name) {
        mName = name;
    }

    /**
     * Set the mDate when the remote control was added
     */
    public void setDate(String date) {
        mDate = date;
    }

    /**
     * Set the ID remote control
     */
    public void setIDControl(int idControl) {
        mIDControl = idControl;
    }

    /**
     * Set the ID for each button of the remote control
     */
    public void setIdButtons(List<Integer> idButtons) {
        this.mIDButtons = idButtons;
    }
}
