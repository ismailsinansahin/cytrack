package com.cydeo.enums;

public enum Country {

    USA(1,"USA"), UK(2,"UK"), GERMANY(3,"Germany"), SPAIN(4,"Spain"), TURKEY(5,"Turkey"),
    FRANCE(6,"France"), NETHERLAND(7,"Netherland"), ITALY(8,"Italy"), GREECE(9,"Greece");

    private int id;
    private String value;

    Country(int id, String value) {
        this.id = id;
        this.value = value;
    }

    public String getValue(){
        return value;
    }

    public int getId() {
        return id;
    }

    public static Country getWithId(int id){
        switch (id){
            case 1: return Country.USA;
            case 2: return Country.UK;
            case 3: return Country.GERMANY;
            case 4: return Country.SPAIN;
            case 5: return Country.TURKEY;
            case 6: return Country.FRANCE;
            case 7: return Country.NETHERLAND;
            case 8: return Country.ITALY;
            case 9: return Country.GREECE;
            default: throw new IllegalArgumentException("Id[" + id +"] not supported.");
        }
    }

}
