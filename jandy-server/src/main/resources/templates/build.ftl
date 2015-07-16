<#include "include/layouts.ftl">
<@layoutFully>
<section class="container">
  <#if build??>
    <#if build.language == 'java'>
      <#macro traverseTreeNodes treeNode>
        <#nested treeNode>
        <#foreach childNode in treeNode.children>
          <@traverseTreeNodes childNode; resultTreeNode>
            <#nested resultTreeNode>
          </@traverseTreeNodes>
        </#foreach>
      </#macro>
      <#assign offset = 0, siblingDepth = 0>
      <@traverseTreeNodes build.javaProfilingDump.root; treeNode>
        <div class="row">
          <div class="col-md-6" style="overflow: hidden;">
            <#if treeNode.javaMethod??>
              <span title="${treeNode.javaMethod.javaClass.packageName}.${treeNode.javaMethod.javaClass.className}.${treeNode.javaMethod.methodName!}">
                <#list 0..treeNode.depth as d>|&nbsp;&nbsp;</#list>${treeNode.javaMethod.javaClass.packageName}.${treeNode.javaMethod.javaClass.className}.${treeNode.javaMethod.methodName!}
              </span>
            <#else>
              Root
            </#if>
          </div>
          <div class="col-md-6" style="vertical-align: middle;">
            <div style="position:relative; left: ${treeNode.offset*100.0}%; width: ${treeNode.width*100.0}%; background-color: hotpink;">
              ${treeNode.totalDuration}ms
            </div>
          </div>
        </div>
      </@traverseTreeNodes>
    </#if>
  </#if>
</section>
</@layoutFully>