<#include "include/layouts.ftl">
<@layoutFully>
<link rel="stylesheet" href="${root}/css/index.css">
<header class="jumbotron">
  <div class="container-fluid">
    <p>
      <img src="${root}/img/logo.png">
    </p>
    <div style="color: #fff;">
      <h2>Performance Analysis Tool ZANDI</h2>
      <p style="font-size: 17px;">
        ZANDI Engine is performance analysis tool based on web services.<br>
        This tool analyzes your application performance and provide hint of the performance improvement<br>
        for open-source developer using at Github.<br>
      </p>
    </div>
    <p>
      <a class="btn btn-default btn-lg" href="#" role="button">
        Getting Started
      </a>
    </p>
  </div>
  <img class="color-short-line" src="${root}/img/index/color_shortline.gif">
</header>
<div id="features" class="container-fluid">
  <section class="row">
    <div class="col-lg-3 features-item">
      <img src="${root}/img/index/feature01.gif">
      <h5>
        To integrate with Github and Travis-CI
      </h5>
    </div>
    <div class="col-lg-3 features-item">
      <img src="${root}/img/index/feature02.png">
      <h5>
        Automatically performance analysis
      </h5>
    </div>
    <div class="col-lg-3 features-item">
      <img src="${root}/img/index/feature03.png">
      <h5>
        Support for ruby, python, java and another languages
      </h5>
    </div>
    <div class="col-lg-3 features-item">
      <img src="${root}/img/index/feature04.png">
      <h5>
        Efficient, Intuitive and Intelligent data visualization
      </h5>
    </div>
  </section>
  <article class="row">
    <div class="col-lg-12">
      <div class="row">
        <div class="col-lg-offset-1 col-lg-6 desc">
          <h2 class="desc-title">
            Integrates with Github and Travis-CI
          </h2>
          <br>
          <ul class="desc-content">
            <li>can insert the badge which represented application performance comparison</li>
            <li>provides the RESTful API and the OAuth provider</li>
            <li>is made by open source and access of public</li>
          </ul>
        </div>
        <div class="col-lg-5 desc-aside">
          <img src="${root}/img/index/big-feature01.png">
        </div>
      </div>
    </div>
  </article>
  <article class="row">
    <div class="col-lg-12">
      <div class="row">
        <div class="col-lg-offset-1 col-lg-6 desc">
          <h2 class="desc-title">
            Automatically analyze performance and tunning key point with improvement for application when commit to repository
          </h2>
          <br>
          <ul class="desc-content">
            <li>offers a variety of report(E-Mail, Twitter, Badge, ...)</li>
            <li>can decide a report type</li>
            <li>provide a report that consider for a specific language</li>
          </ul>
        </div>
        <div class="col-lg-5 desc-aside">
          <img src="${root}/img/index/big-feature02.png">
        </div>
      </div>
    </div>
  </article>
  <article class="row">
    <div class="col-lg-12">
      <div class="row">
        <div class="col-lg-offset-1 col-lg-6 desc ">
          <h2 class="desc-title">
            Will be supported to ruby, python, javascript(node.js, or phantom.js), java, and another languages
          </h2>
          <br>
          <ul class="desc-content">
            <li>automatically changes which in nature of languages</li>
          </ul>
        </div>
        <div class="col-lg-5 desc-aside">
          <img src="${root}/img/index/big-feature03.png">
        </div>
      </div>
    </div>
  </article>
  <article class="row">
    <div class="col-lg-12">
      <div class="row">
        <div class="col-lg-offset-1 col-lg-6 desc">
          <h2 class="desc-title">
            Shows the result to analyzing performance improvement within each functions at runtime
          </h2>
          <br>
        </div>
        <div class="col-lg-5 desc-aside">
          <img src="${root}/img/index/big-feature04.png">
        </div>
      </div>
    </div>
  </article>
</div>
</@layoutFully>