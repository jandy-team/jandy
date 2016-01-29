traverse = (finalLevel, node, fn, level) ->
  level = level||0

  fn(node, level)

  if (finalLevel == level)
    return;

  traverse(finalLevel, child, fn, level+1) for child in node.children

findById = (root, nodeId) ->
  node = null
  traverse Number.MAX_VALUE, root, (n) ->
    if (n.id == parseInt(nodeId))
      node = n
  return node

toggle = (elem, name) ->
  oldValue = elem.attr(name);
  check = elem.data('toggle-check-'+name);
  if (check == undefined)
    check = true;
  elem.attr(name, elem.data('toggle-'+name));
  elem.data('toggle-'+name, oldValue);
  elem.data('toggle-check-'+name, !check);

  return check; # true is to set new value

manips = {
  fullname: (node) ->
    return node.method.owner.packageName+'.'+node.method.owner.name+'.'+node.method.name+node.method.descriptor;

  duration: (node) ->
    return (node.elapsedTime/1000000).toFixed(3)
}

class Benchmark
  constructor: (@templates, @$menus, @$summary) ->

  drawTraceTrees: (root) ->
    @$menus.html('');
    $("#canvas").html('')
    paper = Raphael("canvas", '100%', 600)
    width = $("#canvas").width()
    h = 25

    #get min and max
    mm = {min: Number.MAX_VALUE, max: Number.MIN_VALUE}
    traverse Number.MAX_VALUE, root, (node) ->
      if (node.startTime < mm.min)
        mm.min = node.startTime
      if (node.startTime + node.elapsedTime > mm.max)
        mm.max = node.startTime + node.elapsedTime

    mm.aspects = 1/(mm.max - mm.min)

    # set description of rect for drawing
    traverse Number.MAX_VALUE, root, (node, level) ->
      node.r = {}
      node.r.left = (node.startTime - mm.min) * mm.aspects * width
      node.r.right = (node.startTime + node.elapsedTime - mm.min) * mm.aspects * width
      node.r.top = h * level

    traverse Number.MAX_VALUE, root, (node) =>
      makeRect = (left, top, width, height) =>
        rect = paper.rect(left, top, width, height)
                    .attr('fill', '#99cc00')
                    .attr('stroke', '#000')
                    .attr('title', manips.fullname(node))
                    .data('toggle-stroke', '#ff0')

        rect.dblclick () =>
          window.history.pushState(root, null, '#'+node.id)
          @draw(node)

        rect.click () =>
          if (toggle(rect, 'stroke'))
            @$menus.append(@templates.menus({
              id: node.id,
              package: node.method.owner.packageName,
              className: node.method.owner.name,
              method: node.method.name,
              parameter: node.method.descriptor,
              duration: manips.duration(node)
            }))
          else
            @$menus.find('#info-'+node.id).remove();

        return rect

      left = node.r.left
#      for child in node.children
#        if (child.r.right - child.r.left > 2)
#          makeRect(left, node.r.top, child.r.left - left, h)
#          left = child.r.right
      if (node.r.right - left > 2)
        makeRect(left, node.r.top, node.r.right - left, h)

  start: (profId) ->
    $.get(ROOT_URL+"/rest/prof/"+profId+"/root").done (root) =>
      @root = root;
      this.draw(root.children[0])
      $(window).on 'popstate', () =>
        this.draw(root.children[0])

  draw: (root) ->
    if (window.location.hash == null or window.location.hash == '')
      this.drawTraceTrees(root)
      this.drawSummary(root)
    else
      this.drawTraceTrees(findById(root, window.location.hash.replace('#', '')))
      this.drawSummary(findById(root, window.location.hash.replace('#', '')))

  drawSummary: (root) ->
    @$summary.html('');
    parent = findById(@root.children[0], root.parentId);
    @$summary.append(@templates.summary({
      func: manips.fullname(root)
      duration: manips.duration(root)
      parentId: if parent == null then "#" else root.parentId
      parentName: if parent == null then "" else manips.fullname(parent)
    }))
    $("#summary-canvas").html('')
    paper = Raphael("summary-canvas", '100%', 100)
    width = $("#summary-canvas").width()
    h = 25

    colors = ['green', 'lime']

    #get min and max
    mm = {min: Number.MAX_VALUE, max: Number.MIN_VALUE}
    traverse 1, root, (node) ->
      if (node.startTime < mm.min)
        mm.min = node.startTime
      if (node.startTime + node.elapsedTime > mm.max)
        mm.max = node.startTime + node.elapsedTime

    mm.aspects = 1/(mm.max - mm.min)

    # set description of rect for drawing
    traverse 1, root, (node, level) ->
      node.r = {}
      node.r.left = (node.startTime - mm.min) * mm.aspects * width
      node.r.right = (node.startTime + node.elapsedTime - mm.min) * mm.aspects * width
      node.r.top = 0
      node.r.color = colors[level]

    traverse 1, root, (node) =>
      makeRect = (left, top, width, height, color) =>
        rect = paper.rect(left, top, width, height)
        rect.attr('fill', color)
        rect.attr('stroke', '#000')
        rect.attr('title', node.method.owner.packageName+'.'+node.method.owner.name+'.'+node.method.name+node.method.descriptor)
        rect.data('toggle-stroke', '#ff0')

        rect.dblclick () =>
          window.history.pushState(root, null, '#'+node.id)
          @draw(node)

        rect.click () =>
          if (toggle(rect, 'stroke'))
            @$menus.append(@templates.menus({
              id: node.id,
              package: node.method.owner.packageName,
              className: node.method.owner.name,
              method: node.method.name,
              parameter: node.method.descriptor,
              duration: (node.elapsedTime/1000000000).toFixed(3)
            }))
          else
            @$menus.find('#info-'+node.id).remove();

        return rect

      makeRect(node.r.left, node.r.top, node.r.right - node.r.left, h, node.r.color)

jandy.Benchmark = Benchmark


