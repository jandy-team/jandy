traverse = (node, fn, level) ->
  level = level||0

  if (node.root == false)
    fn(node, level)

  traverse(child, fn, level+1) for child in node.children

findById = (root, nodeId) ->
  node = null
  traverse root, (n) ->
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

class Benchmark
  constructor: (@templates, @$menus) ->

  drawTraceTrees: (root) ->
    @$menus.html('');
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

    # set description of rect for drawing
    traverse root, (node, level) ->
      node.r = {}
      node.r.left = (node.startTime - mm.min) * mm.aspects * width
      node.r.right = (node.startTime + node.elapsedTime - mm.min) * mm.aspects * width
      node.r.top = h * level

    traverse root, (node) =>
      makeRect = (left, top, width, height) =>
        rect = paper.rect(left, top, width, height)
                    .attr('fill', '#99cc00')
                    .attr('stroke', '#000')
                    .attr('title', node.method.owner.packageName+'.'+node.method.owner.name+'.'+node.method.name+node.method.descriptor)
                    .data('toggle-stroke', '#ff0')

        rect.dblclick () =>
          window.history.pushState(root, null, '#'+node.id)
          @drawTraceTrees(node)

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

      left = node.r.left
      for child in node.children
        if (child.r.right - child.r.left > 2)
          makeRect(left, node.r.top, child.r.left - left, h)
          left = child.r.right
      if (node.r.right - left > 2)
        makeRect(left, node.r.top, node.r.right - left, h)

  start: (profId) ->
    $.get(ROOT_URL+"/rest/prof/"+profId).done (prof) =>
      this.draw(prof.root)
      $(window).on 'popstate', () =>
        this.draw(prof.root)
  draw: (root) ->
    if (window.location.hash == null or window.location.hash == '')
      this.drawTraceTrees(root)
    else
      this.drawTraceTrees(findById(root, window.location.hash.replace('#', '')))

greem.Benchmark = Benchmark


