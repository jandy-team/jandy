<#macro drawProjectList shownProjectId>
<div class="list-group">
  <#assign projects = user.projects>
  <#foreach project in projects>
    <#assign elapsedDuration = (project.currentBuild.profContextDump.elapsedDuration)!0>
    <#assign color = (project.currentBuild.color.cssValue)!"#9f9f9f">
    <a href="${root}/repos/${project.account}/${project.name}" class="list-group-item ${(project.id == shownProjectId)?then("disabled", "")}" style="border-left: 5px ${color} solid;">
      <h4>${project.account}/${project.name}</h4>
    </a>
  </#foreach>
</div>
</#macro>