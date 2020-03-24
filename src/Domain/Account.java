package Domain;

public class Account {
    /*
        Object that holds information for an Account. An account has an ID, house number, street, an optional additive to
        the street and house number and a place
     */

    private int ID, houseNumber;
    private String name, street, additive, place;

    public Account(int ID, int number, String name, String street, String additive, String place) {
        
        this.ID = ID;
        this.houseNumber = number;
        this.name = name;
        this.street = street;
        this.additive = additive;
        this.place = place;
    }

    public int getID() {
        return ID;
    }

    public int getHouseNumber() {
        return houseNumber;
    }

    public String getName() {
        return name;
    }

    public String getStreet() {
        return street;
    }

    public String getAdditive() {
        return additive;
    }

    public String getPlace() {
        return place;
    }

    public String toString() {
        return name;
    }
}
