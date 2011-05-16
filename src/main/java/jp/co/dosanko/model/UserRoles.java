package jp.co.dosanko.model;

import jp.co.dosanko.model.auto._UserRoles;

public class UserRoles extends _UserRoles {

    private static final long serialVersionUID =1L;
    
    public Integer getId() {
        return (getObjectId() != null && !getObjectId().isTemporary()) ? (Integer)getObjectId().getIdSnapshot().get(ID_PK_COLUMN): null;
  }
}
