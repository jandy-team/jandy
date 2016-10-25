import json


# AST pattern
class Serializer:
    def serialize(self, obj):
        if isinstance(obj, Serializer):
            return self.serialize(obj.toDict())
        elif type(obj) == "dict":
            result = {}
            d = obj
            for k in d.keys():
                result[k] = self.serialize(d[k])
            return result
        elif type(obj) == "list":
            result = []
            l = obj
            for v in l:
                result.append(self.serialize(v))
            return result
        else:
            return obj

    def toJson(self, obj):
        return json.dumps(self.serialize(obj))