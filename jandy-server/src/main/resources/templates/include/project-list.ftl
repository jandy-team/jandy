<#macro drawProjectList projects shownProjectId>
<div class="list-group">
  <#foreach project in projects>
    <#assign elapsedDuration = (project.currentBuild.profContextDump.elapsedDuration)!0>
    <#assign color = (elapsedDuration <= 0)?then("green", "red")>
    <a href="${root}/repos/${project.account}/${project.name}" class="list-group-item ${(project.id == shownProjectId)?then("disabled", "")}" style="border-left: 5px ${color} solid;">
      <h4>${project.account}/${project.name}</h4>
    </a>
  </#foreach>
</div>
</#macro>