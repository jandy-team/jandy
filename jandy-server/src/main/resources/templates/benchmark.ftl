<#include "include/layouts.ftl">
<@layoutFully>
<section class="container" style="padding-top: 10px;">
  <div id="canvas"></div>
</section>
<script src="${root}/js/benchmark.js"></script>
<script>
  $(function () {
    greem.benchmark.start(
        "${prof.id}"
    )
  })
</script>
</@layoutFully>
