package io.jandy.util.sql;

import io.jandy.domain.data.ClassObject;
import io.jandy.util.sql.conditional.Where;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author JCooky
 * @since 2016-08-04
 */
public class SelectQueryBuilder {
  private String[] tableNames;
  private String[] columns;
  private Where where;

  public SelectQueryBuilder from(String... tableNames) {
    this.tableNames = tableNames;
    return this;
  }

  public SelectQueryBuilder columns(String... columns) {
    this.columns = columns;
    return this;
  }

  public SelectQueryBuilder where(Where where) {
    this.where = where;
    return this;
  }

  public String toSql() {
    StringBuilder sql = new StringBuilder(1024);
    sql.append("select ")
        .append(StringUtils.join(columns, ','))
        .append(" from ")
        .append(StringUtils.join(tableNames, ','))
        .append(" where ");

    if (where != null)
        sql.append(where.toSql());

    return sql.toString();
  }
}
