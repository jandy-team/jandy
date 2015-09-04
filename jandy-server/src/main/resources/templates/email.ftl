<#include "include/layouts.ftl">
<@layout>
<div class="panel panel-success">
  <div class="panel-heading">Performance Report</div>
  <div class="panel-body">
    <p>
      Your application performance is slowed.
      We compare build#${number} and build#${prevNumber}.
      The result is ${elapsedTime}.
    </p>
  </div>
</div>
</@layout>