Data model:

Task - Class with attributes:

- id: int
- title: string
- description: string
- priority: int
- doeDate: string
- done: boolean


Functions:

- addTask(list, task) - returns a new list with a new task added.
- toggleDone(list, id) - returns a new list with the done status of the task with the given id toggled.
- filterByDone(tasks, showDone) - filters the list depending on status of showDone.
    If showDone is null, returns original list, if true returns only done tasks, if false returns undone tasks.
- dateKey(dueDate) - helper function that changes string date to numbers so it can be sorted.
- sortByDueDate(tasks, ascending) - sorts tasks by dateKey. Order is reversed if ascending is false.


Showcase:

https://www.youtube.com/watch?v=jBWmuzu_au0