class TimelineGraph
  constructor: () ->

  start: (projectId) ->
    h = 140
    paper = Raphael("timeline", '100%', h)
    height = 110
    w = $("#timeline").width()
    width = $("#timeline").width() - 10

    $.get ROOT_URL+"/rest/projects/"+projectId+"/builds", (builds) =>
      dx = width/(builds.length - 1);

      mm = {min: Number.MAX_VALUE, max: Number.MIN_VALUE};
      for build in builds
        if (build.profContextDump.maxTotalDuration < mm.min)
          mm.min = build.profContextDump.maxTotalDuration
        if (build.profContextDump.maxTotalDuration > mm.max)
          mm.max = build.profContextDump.maxTotalDuration

      i = 0;
      for build in builds
        build.num = i++;

      for build in builds
        duration = build.profContextDump.maxTotalDuration
        y = 5 + height - (((duration - mm.min) / (mm.max - mm.min)) * height)
        x = 5 + build.num * dx
        build.r = {}
        build.r.x = x
        build.r.y = y
        build.r.color = if (build.profContextDump.elapsedDuration <= 0) then "green" else "red"

#        https://github.com/syjsmk/recursivesample/commit/25a362115243352598617072f435c606658f14f1
#        btn-group pull-right
#        "###########"+build.number
        $('.btn btn-primary btn-sm').href
        console.log($('.btn btn-primary btn-sm').href)
        paper.text(x, h - 5, $('.btn btn-primary btn-sm').href).attr('href', ROOT_URL)
        paper.path("M"+x+","+(h - 19)+"L"+x+","+(h - 11)+"Z")

      prev = null
      for build in builds
        if (prev != null)
          p = paper.path("M"+prev.r.x+","+prev.r.y+"L"+build.r.x+","+build.r.y+"Z").attr("stroke-width", 2).attr("stroke", "#d3d3d3")
        prev = build

      for build in builds
        paper.circle(build.r.x, build.r.y, 4)
          .attr({'fill': build.r.color})

      paper.path("M0,"+(h - 15)+"L"+w+","+(h - 15)+"Z")
#      p1 = paper.path("M10,100L120,120Z")
#      p1.attr({"stroke-width": 4});
#      pathString = ""
#      for build in builds
#        pathString = ""

jandy.TimelineGraph = TimelineGraph;