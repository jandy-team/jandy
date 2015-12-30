traverse = (node, fn, level) ->
  level = level||0

  if (node.elapsedTime != 0)
    fn(node, level)

  traverse(child, fn, level+1) for child in node.children

drawTraceTrees = (root) ->
  $("#canvas").html('')
  paper = Raphael("canvas", '100%', 600)
  width = $("#canvas").width()
  h = 25

  #get min and max
  mm = {min: Number.MAX_VALUE, max: Number.MIN_VALUE}
  traverse root, (node) ->
    if (node.startTime < mm.min)
      mm.min = node.startTime
    if (node.startTime + node.elapsedTime > mm.max)
      mm.max = node.startTime + node.elapsedTime

  mm.aspects = 1/(mm.max - mm.min)

  traverse root, (node, level) ->
    node.r = {}
    node.r.left = (node.startTime - mm.min) * mm.aspects * width
    node.r.right = (node.startTime + node.elapsedTime - mm.min) * mm.aspects * width
    node.r.top = h * level

  traverse root, (node) ->
    events = (elem) ->
      elem.click () ->
        drawTraceTrees(node)

    left = node.r.left
    for child in node.children
      if (child.r.right - child.r.left > 2)
        rect = paper.rect(left, node.r.top, child.r.left - left, h)
                    .attr('fill', '#f00')
                    .attr('stroke', '#000')
                    .attr('title', node.method.owner.packageName+'.'+node.method.owner.name+'.'+node.method.name+node.method.descriptor)
        left = child.r.right
        events(rect)
    if (node.r.right - left > 2)
      rect = paper.rect(left, node.r.top, node.r.right - left, h)
      .attr('fill', '#f00')
      .attr('stroke', '#000')
      .attr('title', node.method.owner.packageName+'.'+node.method.owner.name+'.'+node.method.name+node.method.descriptor)
      events(rect)

greem.benchmark = {
  start: (profId) ->
    $.get(ROOT_URL+"/rest/prof/"+profId).done (prof) ->
      drawTraceTrees(prof.root)
}



#    rect = paper.rect(0, 0, 50, 20)

#    rect.attr('fill', '#f00')
#    rect.attr('stroke', '#000')