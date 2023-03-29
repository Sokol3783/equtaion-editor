package org.example.dao;

public interface DAO {

  long create(String value);

  long getIdByValue(String value);

  boolean saveRoot(Long equationId, Long rootId);


}
