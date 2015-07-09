<#include "include/layouts.ftl">
<@layoutFully>
<div class="container">
  <#if build??>
    <#if build.language == 'java'>
      <#macro traverseTreeNodes treeNode depth>
        <#nested treeNode depth>
        <#foreach childNode in treeNode.children>
          <@traverseTreeNodes childNode depth+2; resultTreeNode, resultDepth>
            <#nested resultTreeNode resultDepth>
          </@traverseTreeNodes>
        </#foreach>
      </#macro>
      <@traverseTreeNodes build.javaProfilingDump.root 0; treeNode, depth>
        <div class="row">
          <h3>
            <#list 0..depth as d>
              &nbsp;
            </#list>
            <#if treeNode.javaMethod??>
              ${treeNode.javaMethod.javaClass.packageName}.${treeNode.javaMethod.javaClass.className}.${treeNode.javaMethod.methodName!}
            <#else>
              Root
            </#if>
          </h3>
        </div>
      </@traverseTreeNodes>
    </#if>
  </#if>
</div>
</@layoutFully>