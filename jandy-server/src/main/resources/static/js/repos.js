'use strict';

jandy.ReposGraph = function (options) {
  this.margin = options.margin;
  this.width = options.width - this.margin.left - this.margin.right;
  this.height = options.height - this.margin.top - this.margin.bottom;

  this.$builds = options.el.builds;
  this.$methods = options.el.methods
};

jandy.ReposGraph.prototype = _.create(Object.prototype, {
  constructor: jandy.ReposGraph,
  createBuilds: function (branchId) {
    var _this = this,
        x = d3.scale.ordinal()
            .rangeRoundBands([0, this.width], .1),

        y = d3.scale.linear()
            .range([this.height, 0]),

        xAxis = d3.svg.axis()
            .scale(x)
            .orient("bottom"),

        yAxis = d3.svg.axis()
            .scale(y)
            .orient("left")
            .ticks(20)
            .tickFormat(function (d) {
              return d + "ms";
            }),

        svg = d3.select(this.$builds)
            .attr("width", this.width + this.margin.left + this.margin.right)
            .attr("height", this.height + this.margin.top + this.margin.bottom)
            .append("g")
            .attr("transform", "translate(" + this.margin.left + "," + this.margin.top + ")");

    d3.json(ROOT_URL + '/rest/branches/' + branchId + '/builds', function (error, data) {
      if (error)
        throw error;

      console.debug(data);

      x.domain(_.map(data, function (d) {
        return d.number;
      }));
      y.domain([0, d3.max(data, function (d) {
        return d.profContextDump.maxTotalDuration
      })]);

      svg.append("g")
          .attr("class", "x axis")
          .attr("transform", "translate(0," + _this.height + ")")
          .call(xAxis);

      svg.append("g")
          .attr("class", "y axis")
          .call(yAxis)
          .append("text")
          .attr("transform", "rotate(-90)")
          .attr("y", 6)
          .attr("dy", ".71em")
          .style("text-anchor", "end")
          .text("Max Duration");

      svg.selectAll(".bar")
          .data(data)
          .enter().append("rect")
          .attr("class", "bar")
          .attr("x", function (d) {
            return x(d.number);
          })
          .attr("width", x.rangeBand())
          .attr("y", function (d) {
            return y(d.profContextDump.maxTotalDuration);
          })
          .attr("height", function (d) {
            return _this.height - y(d.profContextDump.maxTotalDuration)
          })
          .on('click', function (d) {
            _this.createMethods(d.id);
          });
    });
  },
  createMethods: function (buildId) {
    $(this.$methods).html('');

    var _this = this,

        toArray = function (obj, parent, arr) {
          var arr = arr || [];
          obj.parent = parent;

          if (obj.method != null) {
            arr.push(obj);
          }

          _.forEach(obj.children, function (o) {
            toArray(o, obj, arr)
          });

          return arr;
        },


        red = d3.scale.linear()
            .range([0xAD, 255]),
        green = d3.scale.linear()
            .range([0xD8, 255]),
        blue = d3.scale.linear()
            .range([0xE6, 255]),

        angle = d3.scale.linear()
            .range([0, 2.0 * Math.PI]),

        radius = d3.scale.linear()
            .range([0, Math.min(_this.width, _this.height) * 0.5]),

        arc = d3.svg.arc()
            .startAngle(function (node) {
              return angle(node.startTime);
            })
            .endAngle(function (node) {
              return angle(node.startTime + node.elapsedTime);
            })
            .innerRadius(function (d) {
              return 10 + radius(d.depth);
            })
            .outerRadius(function (d) {
              return 10 + radius(d.depth + 1);
            }),

        vis = d3.select(_this.$methods)
            .attr('width', _this.width)
            .attr('height', _this.height)
            .append('g')
            .attr('transform', 'translate(' + _this.width / 2 + ',' + _this.height / 2 + ')');

    d3.json(ROOT_URL + '/rest/build/' + buildId + "/prof", function (prof) {

      var nodes = toArray(prof.root),
          maxDepth = d3.max(nodes, function (n) {
            return n.depth;
          });

      angle.domain([d3.min(nodes, function (n) {
        return n.startTime;
      }), d3.max(nodes, function (n) {
        return n.startTime + n.elapsedTime;
      })]);

      radius.domain([0.0, maxDepth + 1]);
      red.domain([0.0, maxDepth]);
      blue.domain([0.0, maxDepth]);
      green.domain([0.0, maxDepth]);

      vis.selectAll('g').data(nodes).enter()
          .append('path')
          .attr('d', arc)
          .attr('stroke', '#fff')
          .attr('fill', function (d) {
            return d3.rgb(red(d.depth), green(d.depth), blue(d.depth)).toString();
          })
          .attr('title', function (d) {
            return d.method.owner.packageName + '.' + d.method.owner.className + '.' + d.method.name;
          })
      ;

      $(_this.$methods).find('[title]').tooltip({
        'container': 'body',
        'placement': 'top'
      });
    });
  }
});

