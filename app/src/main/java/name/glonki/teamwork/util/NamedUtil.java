package name.glonki.teamwork.util;

import java.util.List;
import java.util.Map;

import name.glonki.teamwork.interfaces.Identifiable;
import name.glonki.teamwork.interfaces.Named;

/**
 * Created by Glonki on 16.10.2017.
 */

public class NamedUtil {

    public static <A extends Named> String[] getNames(List<A> ns) {
        int l = ns.size();
        String[] result = new String[l];
        for(int i=0; i<l; i++) {
            result[i] = ns.get(i).getName();
        }
        return result;
    }

    public static <A extends Named & Identifiable> String getNamesByIds(Map<Integer, A> nsMap, List<Integer> ids) {
        int l = ids.size();
        StringBuilder builder = new StringBuilder();
        for(int i=0; i<l; i++) {
            String name = nsMap.get(ids.get(i)).getName();
            builder.append(name);
            if(i < l - 1) {
                builder.append(", ");
            }
        }
        return builder.toString();
    }

    public static <A extends Named & Identifiable> String getNamesByIds(Map<Integer, A> nsMap, int[] ids) {
        int l = ids.length;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < l; i++) {
            if(nsMap.containsKey(ids[i])) {
                String name = nsMap.get(ids[i]).getName();
                builder.append(name);
                if (i < l - 1) {
                    builder.append(", ");
                }
            }
        }
        return builder.toString();
    }

}
