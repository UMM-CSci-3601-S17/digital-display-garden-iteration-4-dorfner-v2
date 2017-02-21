# Lab Tasks - Lab 4

 - Questions that you need to answer (as a group!) are indicated with question
mark symbols (:question:).
- Tasks that specify work to do without a written response will be bulleted.

Responses to questions should be submitted as specified by your instructor.

If you're ever confused about what you need to do for a given task, ask.

### Exploring the project

The structure of this project should be nearly identical to that of lab #3,
and as such there really isn't much excitement in that department.

#### Exploring the server
The server is, for the most part, the same as it has been in the past
two labs. The difference to look for here is in how the server gets the
data it sends out in reply to requests.

:question: So how _does_ the server get all that data it's sending out?

### More Todos!

- Re-implement the ToDo API, this time pulling data from MongoDB rather
than from a flat JSON file.

### 3 Meta 5 Me

- Implement an API which provides summary information about a group of
ToDos in the following format:

````
{
  percentToDosComplete: Float,
  categoriesPercentComplete: {
    groceries: Float,
    ...
  }
  ownersPercentComplete: {
    ...
  }
}
````

### Make it pretty

- Use the front-end tools you've learned about to build a nice interface for
accessing these APIs:
  - You must use [Glyphicons][glyphicons] somewhere
  - You must use at least two of the following things:
    - Navs
    - Pagination
    - Progress Bars
    - Badges / Labels
    - ngStyle directive

[glyphicons]: https://getbootstrap.com/components/#glyphicons
