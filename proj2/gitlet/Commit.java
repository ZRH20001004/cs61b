package gitlet;

// TODO: any imports you need here


import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Date;

import static gitlet.Utils.*;

/**
 * Represents a gitlet commit object.
 *  does at a high level.
 *
 * @author TODO
 */
public class Commit implements Serializable {
    /**
     * <p>
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */
    private String date;
    /**
     * The message of this Commit.
     */
    private String message;
    private String parentID;
    private String ID;
    private HashMap<String, String> tree;

    public Commit() {
        Date begin = new Date(0);
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        date = ft.format(begin);
        message = "initial commit";
        ID = calID();
    }

    public Commit(String msg, HashMap<String, String> tree, String parentID) {
        Date current = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        date = ft.format(current);
        message = msg;
        this.tree = tree;
        this.parentID = parentID;
        ID = calID();
    }

    private String calID() {
        return sha1(serialize(this));
    }

    public String getMessage() {
        return message;
    }

    public String getDate() {
        return date;
    }

    public HashMap<String, String> getTree() {
        return tree;
    }

    public String getID() {
        return ID;
    }

    public String getParentID() {
        return parentID;
    }

}
