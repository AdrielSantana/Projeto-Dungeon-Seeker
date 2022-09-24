package game;

import java.util.Set;
import java.util.HashMap;

/**
 * Class Game.Room - a room in an adventure game.
 *
 * This class is part of the "World of Zuul" application. 
 * "World of Zuul" is a very simple, text based adventure game.  
 *
 * A "Game.Room" represents one location in the scenery of the game.  It is
 * connected to other rooms via exits.  For each existing exit, the room 
 * stores a reference to the neighboring room.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2016.02.29
 */

public class Room 
{
    private String description;

    private Boolean trap; // se tem armadilha
    private Boolean treasure; // sala com o tesouro
    private String hint; // Dica da sala
    private String otherRoomKey; // chave de outra sala
    private Boolean lockDoor; // se está trancada
    private String neededKey; // chave necessária para abrir a sala
    private HashMap<String, Room> exits;        // stores exits of this room.

    /**
     * Create a room described "description". Initially, it has
     * no exits. "description" is something like "a kitchen" or
     * "an open court yard".
     * @param description The room's description.
     */
    public Room(String description) 
    {
        this.treasure = false;
        this.trap = false;
        this.lockDoor = false;
        this.description = description;
        exits = new HashMap<>();
    }

    // pega e seta o tesouro


    public Boolean getTreasure() {
        return treasure;
    }

    public void setTreasure(Boolean treasure) {
        this.treasure = treasure;
    }

    // pega e seta a trap
    public Boolean getTrap() {
        return trap;
    }

    public void setTrap(Boolean state){
        this.trap = state;
    }

    // pega e seta a dica

    public String getHint() {
        return hint;
    }

    public void setHint(String hint){
        this.hint = hint;
    }

    /**
     * Define an exit from this room.
     * @param direction The direction of the exit.
     * @param neighbor  The room to which the exit leads.
     */
    public void setExit(String direction, Room neighbor) 
    {
        exits.put(direction, neighbor);
    }

    // Pega e seta a chave de outra sala

    public String getOtherRoomKey() {
        return otherRoomKey;
    }

    public void setOtherRoomKey(String otherRoomKey) {
        this.otherRoomKey = otherRoomKey;
    }

    // Pega e seta a chave necessária para acessar a sala

    public void setNeededKey(String neededKey) {
        this.neededKey = neededKey;
    }

    public String getNeededKey() {
        return neededKey;
    }


    // Pega e seta se a porta está trancada

    public void setLockDoor(Boolean state){
        this.lockDoor = state;
    }

    public Boolean getLockDoor() {
        return lockDoor;
    }

    /**
     * @return The short description of the room
     * (the one that was defined in the constructor).
     */
    public String getShortDescription()
    {
        return description;
    }

    /**
     * Return a description of the room in the form:
     *     You are in the kitchen.
     *     Exits: north west
     * @return A long description of this room
     */
    public String getLongDescription()
    {
        return "Voce esta " + description + ".\n" + getExitString();
    }

    /**
     * Return a string describing the room's exits, for example
     * "Exits: north west".
     * @return Details of the room's exits.
     */
    public String getExitString()
    {
        String returnString = "Saidas:";
        Set<String> keys = exits.keySet();
        for(String exit : keys) {
            returnString += " " + exit;
        }
        return returnString;
    }

    /**
     * Return the room that is reached if we go from this room in direction
     * "direction". If there is no room in that direction, return null.
     * @param direction The exit's direction.
     * @return The room in the given direction.
     */
    public Room getExit(String direction) 
    {
        return exits.get(direction);
    }
}

