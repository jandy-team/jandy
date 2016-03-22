namespace java io.jandy.thrift.java
namespace py jandy

typedef string UUID

struct ClassObject {
  1: UUID id,
  2: string packageName = "",
  3: string name = ""
}

struct MethodObject {
  1: UUID id,
  2: string name,
  3: i32 access,
  4: string descriptor,
  5: UUID ownerId
}

struct ExceptionObject {
  1: UUID id,
  2: UUID classId,
  3: optional UUID casedById,
  4: string message,
}

struct Accumulator {
  1: UUID id,
  2: i64 elapsedTime,
  3: i64 startTime,
  4: string concurThreadName,
  5: optional UUID exceptionId,
}

struct TreeNode {
  1: UUID id,
  2: list<UUID> childrenIds,
  3: Accumulator acc,
  4: UUID methodId,
  5: UUID parentId,
  6: bool root
}

struct ProfilingContext {
  1: list<ClassObject> classes,
  2: list<MethodObject> methods,
  3: list<ExceptionObject> exceptions,
  4: list<TreeNode> nodes,
  5: UUID rootId
}