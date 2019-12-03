package com.backendutils;

import java.util.ArrayList;

public class ArrayUtils {
    public Object[][] append(Object[][] a, Object[][] b) {
        Object[][] result = new Object[a.length + b.length][];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }

    public boolean areListsOfObjectsEqual(ArrayList<Object> a, ArrayList<Object> b){
        if (a == null && b == null) return true;
        if ((a == null && b!= null) || (a != null && b== null) || (a.size() != b.size()))
        {
            return false;
        }

        ArrayList<Boolean> compareResults = new ArrayList<Boolean>();

        for (int i = 0; i < a.size(); i++){
            String aItem = String.valueOf(a.get(i));
            String bItem = String.valueOf(b.get(i));;

            compareResults.add(aItem.equals(bItem));
        }
        if (compareResults.contains(false)){
            return false;
        }
        else return true;
    }
}
