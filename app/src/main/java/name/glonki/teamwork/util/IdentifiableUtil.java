package name.glonki.teamwork.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import name.glonki.teamwork.interfaces.Identifiable;

/**
 * Created by Glonki on 16.10.2017.
 */

public class IdentifiableUtil {

    public static <A extends Identifiable> Map<Integer, A> getIdMap(List<A> list) {
        Map<Integer, A> idMap = new HashMap<>();
        for(A a : list) {
            idMap.put(a.getId(), a);
        }
        return idMap;
    }

}
