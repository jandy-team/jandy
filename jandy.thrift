namespace java io.jandy.thrift.java

struct ClassKey {
  1: string packageName,
  2: string name
}

struct MethodKey {
  1: string name
  2: i32 access,
  3: string descriptor,
  4: ClassKey owner
}

struct ExceptionKey {
  1: ClassKey classKey,
  2: ExceptionKey caused,
  3: string message
}

struct Accumulator {
  1: i64 elapsedTime,
  2: i64 startTime,
  3: string concurThreadName,
  4: ExceptionKey exceptionKey
}

struct TreeNode {
  1: list<TreeNode> children
  2: Accumulator acc,
  3: MethodKey method
}

struct ProfilingMetrics {
  1: TreeNode root
}