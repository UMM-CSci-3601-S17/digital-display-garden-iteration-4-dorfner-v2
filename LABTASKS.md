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
- When displaying the ToDos in your Angular front-end, make thoughtful decisions
about whether work like filtering should be done in Angular or via database queries.
It would be reasonable, for example, to have the database filter out all the ToDos
belonging to a single user, but let Angular filter by category or status.

### Summary Information About ToDos

To see an example of using the database and the server to do some useful work
(instead of having everything happen in Angular), implement an API endpoint
`/api/todoSummary` which provides summary information about a group of
ToDos in the following format:

````
{
  percentToDosComplete: Float,
  categoriesPercentComplete: {
    groceries: Float,
    ...
  }
  ownersPercentComplete: {
    Blanche: Float,
    ...
  }
}
````

So you should add a new endpoint to your Spark routes, and then have that call
some method (possibly in a new class?) that queries the DB for the relevant data
and assembles this JSON response. Note that you can use 
[MongoDB aggregation](http://mongodb.github.io/mongo-java-driver/3.4/driver/tutorials/aggregation/)
to do most of this calculation without having to actually download all the todos,
organize, and count them yourself.

### Make it pretty

- Use the front-end tools you've learned about to build a nice interface for
accessing these APIs:
  - You must use [Glyphicons][glyphicons] somewhere
  - You must use at least two of the following nifty Bootstrap features:
    - [Navs](http://getbootstrap.com/components/#nav)
    - [Pagination](http://getbootstrap.com/components/#pagination)
    - [Progress Bars](http://getbootstrap.com/components/#progress)
    - [Badges](http://getbootstrap.com/components/#badges) or [Labels](http://getbootstrap.com/components/#labels)
    - [ngStyle directive](https://docs.angularjs.org/api/ng/directive/ngStyle)

[glyphicons]: https://getbootstrap.com/components/#glyphicons
