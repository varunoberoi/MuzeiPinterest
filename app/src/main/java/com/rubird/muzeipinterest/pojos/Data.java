
package com.rubird.muzeipinterest.pojos;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;

@Generated("org.jsonschema2pojo")
public class Data {

    @Expose
    private List<Pin> pins = new ArrayList<Pin>();
    @Expose
    private User user;
    @Expose
    private Board board;

    /**
     * 
     * @return
     *     The pins
     */
    public List<Pin> getPins() {
        return pins;
    }

    /**
     * 
     * @param pins
     *     The pins
     */
    public void setPins(List<Pin> pins) {
        this.pins = pins;
    }

    /**
     * 
     * @return
     *     The user
     */
    public User getUser() {
        return user;
    }

    /**
     * 
     * @param user
     *     The user
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * 
     * @return
     *     The board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * 
     * @param board
     *     The board
     */
    public void setBoard(Board board) {
        this.board = board;
    }

}
