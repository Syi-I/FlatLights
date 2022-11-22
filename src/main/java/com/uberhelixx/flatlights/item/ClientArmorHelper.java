package com.uberhelixx.flatlights.item;

//This needs to be it's own class, I don't recommend doing anything else in here!
//Otherwise you risk being incompatible with servers.
public class ClientArmorHelper{

    private final static ArmorBaseModel YOUR_FIRST_ARMOR = new ArmorModelImplementation();
    //private final static ArmorBaseModel YOUR_SECOND_ARMOR = new SomeOtherArmorModelImplementation();

    public static ArmorBaseModel getFirstArmor(){
        return YOUR_FIRST_ARMOR;
    }

    /*public static ArmorBaseModel getSecondArmor(){
        return YOUR_SECOND_ARMOR;
    }*/
}

//Creating an Armor Item now looks similar to this:
//new ArmorBaseItem(material, slotType, properties, () -> ClientArmorHelper::getFirstArmor);
