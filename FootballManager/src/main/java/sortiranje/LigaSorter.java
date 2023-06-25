package sortiranje;

import entitet.Liga;

import java.util.Comparator;

public class LigaSorter implements Comparator<Liga> {

    @Override
    public int compare(Liga l1, Liga l2) {

        if (l1.getBrojBodova().compareTo(l2.getBrojBodova()) == 0){
            return l1.getBrojBodova().compareTo(l2.getBrojBodova());
        } else if (l1.getBrojBodova().compareTo(l2.getBrojBodova()) > 0){
            return l1.getBrojBodova().compareTo(l2.getBrojBodova());
        } else {
            if ((l1.getZabijenoGolova() - l1.getPrimljenoGolova()) > (l2.getZabijenoGolova() - l2.getPrimljenoGolova())) {
                return 0;
            } else if ((l1.getZabijenoGolova() - l1.getPrimljenoGolova()) < (l2.getZabijenoGolova() - l2.getPrimljenoGolova())) {
                return 1;
            } else {
                if (l1.getZabijenoGolova() > l2.getZabijenoGolova()) {
                    return 0;
                } else if (l1.getZabijenoGolova() < l2.getZabijenoGolova()) {
                    return 1;
                }
            }
        }

        return 0;
    }
}