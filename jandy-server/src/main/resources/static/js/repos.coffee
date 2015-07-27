class this.ReposGraph

  constructor: (options) ->
    @margin = options.margin
    @width = options.width - @margin.left - @margin.right
    @height = options.height - @margin.top - @margin.bottom

    @$builds = options.el.builds
    @$methods = options.el.methods

  createBuilds: (branchId) ->

    x = d3.scale.ordinal()
                .rangeRoundBands([0, @width], .1)

    y = d3.scale.linear()
                .range([@height, 0])

    xAxis = d3.svg.axis()
                  .scale(x)
                  .orient("bottom")

    yAxis = d3.svg.axis()
                  .scale(y)
                  .orient("left")
                  .ticks(20)
                  .tickFormat((d) -> return d + "ms")

    svg = d3.select(@$builds)
            .attr("width", @width + @margin.left + @margin.right)
            .attr("height", @height + @margin.top + @margin.bottom)
            .append("g")
            .attr("transform", "translate(" + @margin.left + "," + @margin.top + ")")

    d3.json(ROOT_URL+'/rest/branches/' + branchId + '/builds', (error, data) =>
      throw error if error

      console.debug(data)

      x.domain(_.map(data, (d) -> return d.number))
      y.domain([0, d3.max(data, (d) -> return d.javaProfilingDump.maxTotalDuration)])

      svg.append("g")
          .attr("class", "x axis")
          .attr("transform", "translate(0," + @height + ")")
          .call(xAxis)

      svg.append("g")
          .attr("class", "y axis")
          .call(yAxis)
          .append("text")
          .attr("transform", "rotate(-90)")
          .attr("y", 6)
          .attr("dy", ".71em")
          .style("text-anchor", "end")
          .text("Max Duration")

      svg.selectAll(".bar")
          .data(data)
          .enter().append("rect")
          .attr("class", "bar")
          .attr("x", (d) -> return x(d.number))
          .attr("width", x.rangeBand())
          .attr("y", (d) -> return y(d.javaProfilingDump.maxTotalDuration))
          .attr("height", (d) => return @height - y(d.javaProfilingDump.maxTotalDuration))
          .on('click', (d) => @createMethods(d.id))
    )

  createMethods: (buildId) ->
    x = d3.scale.linear().range([@width, 0])

    y = d3.scale.ordinal().rangeRoundBands([@height, 0])

    xAxis = d3.svg.axis().scale(x).orient('top')

    yAxis = d3.svg.axis().scale(y).orient('left')

    $(@$methods).html('');
    svg = d3.select(@$methods)
            .attr('width', @width + @margin.left + @margin.right)
            .attr('height', @height + @margin.top + @margin.bottom)
            .append('g')
            .attr('transform', 'translate(' + @margin.left + ', ' + @margin.top + ')')

    d3.json ROOT_URL+'/rest/builds/'+buildId+'/java/nodes', (error, data) =>

      x.domain([0, d3.max(data, (d) -> return d.duration)])
      y.domain(_.map(data, (d) -> return d.javaMethod.methodName))

      svg.append('g')
          .attr('class', 'x axis')
          .attr('transform', 'translate(0, 0)')
          .call(xAxis)
          .attr('x', 6)
          .attr('dx', '.71em')
          .text('Duration')

      svg.append('g')
          .attr('class', 'y axis')
          .call(yAxis)
          .attr('tarnsform', 'rotate(-90)')

#      svg.selectAll('.bar')
#          .data(data)
#          .enter().append('rect')
#          .attr('x', (d) -> return x(d.duration))
#          .attr('width', (d) => return @width - x(d.duration))
#          .attr('y', (d) -> return y(d.javaMethod.methodName))
#          .attr('height', y.rangeBand())
