package io.jandy.util.sql;

public class SubQuery {

    private SelectQueryBuilder q;

    public SubQuery(SelectQueryBuilder q) {
      this.q = q;
    }

    public String toSql() {
      return "(" + this.q.toSql() + ")";
    }
  }