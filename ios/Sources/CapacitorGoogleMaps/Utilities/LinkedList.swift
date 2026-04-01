import Foundation

public class Node<T> {
    var value: T
    var next: Node?

    init(value: T) {
        self.value = value
    }
}

public class List<T> {
    fileprivate var head: Node<T>?
    private var tail: Node<T>?

    public var isEmpty: Bool {
        return head == nil
    }
    public var first: Node<T>? {
        return head
    }
    public var last: Node<T>? {
        return tail
    }

    init(elements: [T]) {
        elements.forEach {
            self.append(value: $0)
        }
    }

    public func append(value: T) {
        let newNode = Node<T>(value: value)

        if let tailNode = tail {
            tailNode.next = newNode
        } else {
            head = newNode
        }

        tail = newNode
    }
}
