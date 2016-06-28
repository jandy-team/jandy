package io.jandy.util.sql;

import io.jandy.util.sql.conditional.Where;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author JCooky
 * @since 2016-08-04
 */
public class BatchUpdateQueryBuilder<ID> {

  private String tableName;
  private Map<String, Map<ID, Object>> set = new HashMap<>();

  public BatchUpdateQueryBuilder(String tableName) {
    this.tableName = tableName;
  }

  public BatchUpdateQueryBuilder<ID> set(ID id, String column, Object value) {
    if (!"id".equalsIgnoreCase(column)) {

      Map<ID, Object> v = set.get(column);
      if (v == null) {
        v = new HashMap<>();
        set.put(column, v);
      }
      v.put(id, value);

    }
    return this;
  }

  public int execute(JdbcTemplate jdbc) {
    return jdbc.update(toSql());
  }

  public String toSql() {
    assert !set.isEmpty();

    return "UPDATE " + tableName +
        " SET " +
        set.keySet().stream().map(column -> column + " = " + toValue(set.get(column))).reduce((s1, s2) -> s1 + ", " + s2).get() +
        " WHERE id IN " +
        "(" + set.get(set.keySet().stream().findFirst().get()).keySet().stream().map(SqlUtils::toValue).reduce((s1, s2) -> s1 + ',' + s2).get() + ")";
  }

  private String toValue(Map<ID, Object> values) {
    return "CASE id " +
        values.keySet().stream().map(id -> "WHEN " + SqlUtils.toValue(id) + " THEN " + SqlUtils.toValue(values.get(id))).reduce((s1, s2) -> s1 + " " + s2).get()
        + " END";
  }
}
