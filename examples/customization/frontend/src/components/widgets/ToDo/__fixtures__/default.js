import React from "react";
import ToDo from "../ToDo";
import ToDoWidget from "../ToDoWidget";

const defaultMeta = {
  id: "todoWidget",
  size: 10,
  dataProvider: {
    url: "n2o/data/todoWidget",
    pathMapping: {},
    queryMapping: {}
  }
};

export default [
  {
    component: ToDo,
    name: "Заметки",
    props: {
      data: [
        {
          id: 1,
          text: "Помыть посуду",
          done: true,
          priority: false
        },
        {
          id: 2,
          text: "Вынести мусор",
          done: false,
          priority: false
        },
        {
          id: 3,
          text: "Накормить кота",
          done: false,
          priority: true
        }
      ]
    },
    reduxState: {}
  },
  {
    component: ToDoWidget,
    name: 'Виджет "Заметки"',
    props: defaultMeta,
    reduxState: {},
    fetch: [
      {
        matcher: "n2o/data/todoWidget?page=1&size=10",
        response: {
          list: [
            {
              id: 1,
              text: "Помыть посуду",
              done: true,
              priority: false
            },
            {
              id: 2,
              text: "Вынести мусор",
              done: false,
              priority: false
            },
            {
              id: 3,
              text: "Накормить кота",
              done: false,
              priority: true
            }
          ]
        }
      }
    ]
  }
];
