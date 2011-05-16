package jp.co.dosanko.model;

import jp.co.dosanko.model.auto._MeiboDomainMap;

public class MeiboDomainMap extends _MeiboDomainMap {

    private static MeiboDomainMap instance;

    private MeiboDomainMap() {}

    public static MeiboDomainMap getInstance() {
        if(instance == null) {
            instance = new MeiboDomainMap();
        }

        return instance;
    }
}
