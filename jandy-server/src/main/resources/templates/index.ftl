<#include "include/layouts.ftl">
<@layoutFully>
<div class="container-fluid" style="text-align: center">
  <form action="${root}/signin/github" method="POST" style="display: inline;">
    <button type="submit" class="navbar-btn btn btn-default">Sign in with GitHub</button>
    <input type="hidden" name="scope" value="${github_scopes}" />
  </form>
</div>
</@layoutFully>