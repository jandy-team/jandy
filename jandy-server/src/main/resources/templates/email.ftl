<!DOCTYPE HTML>
<html>
<meta charset="utf-8">
<body data-spy="scroll">
<#assign elapsedDuration = (build.profContextDump.elapsedDuration)!0>
<#assign color = (elapsedDuration <= 0)?then("green", "red")>
<#assign after = (elapsedDuration <= 0)?then(" faster than", " slower than")>
<div style="background-color: rgb(255, 255, 255);
border-bottom-color: rgb(221, 221, 221);
border-bottom-left-radius: 4px;
border-bottom-right-radius: 4px;
border-bottom-style: solid;
border-bottom-width: 1px;
border-image-outset: 0px;
border-image-repeat: stretch;
border-image-slice: 100%;
border-image-source: none;
border-image-width: 1;
border-left-color: rgb(0, 128, 0);
border-left-style: solid;
border-left-width: 10px;
border-right-color: rgb(221, 221, 221);
border-right-style: solid;
border-right-width: 1px;
border-top-color: rgb(221, 221, 221);
border-top-left-radius: 4px;
border-top-right-radius: 4px;
border-top-style: solid;
border-top-width: 1px;
box-shadow: rgba(0, 0, 0, 0.0470588) 0px 1px 1px 0px;
box-sizing: border-box;
color: rgb(51, 51, 51);
display: block;
font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;
font-size: 14px;
height: 164px;
line-height: 20px;
margin-bottom: 20px;
">
  <div style="background-color: rgb(245, 245, 245);
border-bottom-color: rgb(221, 221, 221);
border-bottom-style: solid;
border-bottom-width: 1px;
border-left-color: rgb(221, 221, 221);
border-right-color: rgb(221, 221, 221);
border-top-color: rgb(221, 221, 221);
border-top-left-radius: 3px;
border-top-right-radius: 3px;
box-sizing: border-box;
color: rgb(51, 51, 51);
display: block;
font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;
font-size: 14px;
height: 82px;
line-height: 20px;
padding-bottom: 10px;
padding-left: 15px;
padding-right: 15px;
padding-top: 10px;">
    <div style="box-sizing: border-box;
color: rgb(51, 51, 51);
display: block;
font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;
font-size: 16px;
height: 51px;
line-height: 22.8571px;
margin-bottom: 0px;
margin-top: 0px;">
      <h4 style="box-sizing: border-box
color: rgb(51, 51, 51)
display: block
font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif
font-size: 18px
font-weight: 500
height: 19px
line-height: 19.8px
margin-bottom: 10px
margin-top: 10px">
        <span style="padding-right: 5px;"><a
            href="https://travis-ci.org/${project.account}/${project.name}/builds/${build.travisBuildId}">#${build.number}</a></span>
        <span style="padding-left: 5px; color: ${color};">${(elapsedDuration/1000000)?abs}ms ${after} before</span>
        <span style="box-sizing: border-box;
color: rgb(51, 51, 51);
display: block;
float: right;
font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;
font-size: 18px;
font-weight: 500;
height: 30px;
line-height: 19.8px;
position: relative;
vertical-align: middle;">
          <a href="https://github.com/${project.account}/${project.name}/commit/${(build.commit.sha)!"25a362115243352598617072f435c606658f14f1"}"
             style="background-color: rgb(51, 122, 183);
background-image: none;
border-bottom-color: rgb(46, 109, 164);
border-bottom-left-radius: 3px;
border-bottom-right-radius: 0px;
border-bottom-style: solid;
border-bottom-width: 1px;
border-image-outset: 0px;
border-image-repeat: stretch;
border-image-slice: 100%;
border-image-source: none;
border-image-width: 1;
border-left-color: rgb(46, 109, 164);
border-left-style: solid;
border-left-width: 1px;
border-right-color: rgb(46, 109, 164);
border-right-style: solid;
border-right-width: 1px;
border-top-color: rgb(46, 109, 164);
border-top-left-radius: 3px;
border-top-right-radius: 0px;
border-top-style: solid;
border-top-width: 1px;
box-sizing: border-box;
color: rgb(255, 255, 255);
cursor: pointer;
display: block;
float: left;
font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;
font-size: 12px;
font-weight: normal;
height: 30px;
line-height: 18px;
margin-bottom: 0px;
margin-left: 0px;
padding-bottom: 5px;
padding-left: 10px;
padding-right: 10px;
padding-top: 5px;
position: relative;
text-align: center;
text-decoration: none;
touch-action: manipulation;
vertical-align: middle;
white-space: nowrap;
width: 70px;
-webkit-user-select: none;"
             role="button">${(build.commit.sha?substring(0, 7))!"c90978e"}</a>
          <a href="http://jandy.io/builds/${build.id}" style="background-color: rgb(51, 122, 183);
background-image: none;
border-bottom-color: rgb(46, 109, 164);
border-bottom-left-radius: 0px;
border-bottom-right-radius: 3px;
border-bottom-style: solid;
border-bottom-width: 1px;
border-image-outset: 0px;
border-image-repeat: stretch;
border-image-slice: 100%;
border-image-source: none;
border-image-width: 1;
border-left-color: rgb(46, 109, 164);
border-left-style: solid;
border-left-width: 1px;
border-right-color: rgb(46, 109, 164);
border-right-style: solid;
border-right-width: 1px;
border-top-color: rgb(46, 109, 164);
border-top-left-radius: 0px;
border-top-right-radius: 3px;
border-top-style: solid;
border-top-width: 1px;
box-sizing: border-box;
color: rgb(255, 255, 255);
cursor: pointer;
display: block;
float: left;
font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;
font-size: 12px;
font-weight: normal;
height: 30px;
line-height: 18px;
margin-bottom: 0px;
margin-left: -1px;
padding-bottom: 5px;
padding-left: 10px;
padding-right: 10px;
padding-top: 5px;
position: relative;
text-align: center;
text-decoration: none;
touch-action: manipulation;
vertical-align: middle;
white-space: nowrap;
width: 50px;
-webkit-user-select: none;" role="button">More</a>
        </span>
        <img src="${(committerAvatarUrl)!"https://avatars.githubusercontent.com/u/1345314?v=3"}"
             style="float: right; vertical-align: middle; border: 0; box-sizing: border-box; margin-right: 5px; display: inline-block; background-size: cover; border-radius: 5px;"
             width="30px" height="30px">
      </h4>
    </div>
    <div style="box-sizing: border-box
color: rgb(51, 51, 51)
display: block
font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif
font-size: 14px
height: 80px
line-height: 20px
padding-bottom: 15px
padding-left: 15px
padding-right: 15px
padding-top: 15px">
      <p>
        Max Total Duration: ${(build.profContextDump.maxTotalDuration/1000000)!"9999"}ms<br>
        Commit Message: ${(build.commit.message)!"test"}<br>
      </p>
    </div>
  </div>
</div>
</body>
</html>