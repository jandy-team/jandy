<#include "include/layouts.ftl">
<@layoutFully>
<section id="repos" class="container">
  <#foreach repo in repos>
    <div class="row">
      <a href="${root}/repos/${repo.account}/${repo.name}"><h1>${repo.name}</h1></a>
    </div>
  </#foreach>
</section>
<script>

</script>
</@layoutFully>