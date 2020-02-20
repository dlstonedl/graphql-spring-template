query 接口
```
query {
  bookById(id: "book-1") {
    id
    name
    pageCount
    author {
      firstName
      lastName
    }
  }
}
```

mutation 接口
```
mutation ($bookInput: BookInput!) {
  updateBook(bookInput: $bookInput) {
    id
    name
    pageCount
    author {
      firstName
      lastName
    }
  }
}

{
  "bookInput": {
    "id": "book-1",
    "name": "name",
    "pageCount": 111,
    "authorId": "author-2"
  }
}
```
