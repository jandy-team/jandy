package io.jandy.util.sql.conditional;

import io.jandy.util.sql.SubQuery;

public class Where {
  private WhereType type;
  private String key;
  private Object value;
  private Where and;

  public static Where eq(String key, Object value) {
    Where w = new Where();
    w.type = WhereType.EQUALS;
    w.key = key;
    w.value = value;

    return w;
  }

  public Where and(Where where) {
    this.and = where;
    return this;
  }

  public String toSql() {
    String q = null;
    switch (type) {
      case EQUALS:
        if (value instanceof String) {
          q = key + " = " + "'" + value.toString() + "'";
        } else if (value instanceof SubQuery) {
          q = key + " = " + ((SubQuery) value).toSql();
        } else if (value instanceof Number) {
          q = key + " = " + value.toString();
        } else if (value == null) {
          q = key + " is null";
        } else {
          throw new IllegalArgumentException();
        }
    }

    if (and != null) {
      q += " and " + and.toSql();
    }

    return q;
  }
}

enum WhereType {
  EQUALS
}