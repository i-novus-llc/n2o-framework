import React from "react";
import expect from "expect";
import {
  add,
  setCounter,
  destroy,
  clearAll,
  ADD,
  SET_COUNTER,
  DESTROY,
  CLEAR_ALL
} from "../src/actions";

describe("Тест на экшены", () => {
  it("add", () => {
    const data = {
      id: 1,
      text: "test",
      icon: "icon",
      image: "image",
      title: "title",
      date: "date",
      close: false,
      delay: 500
    };
    expect(add(data.id, data)).toEqual({
      meta: {},
      payload: data,
      type: ADD
    });
  });
  it("setCounter", () => {
    expect(setCounter("id", 555)).toEqual({
      meta: {},
      payload: {
        counterId: "id",
        value: 555
      },
      type: SET_COUNTER
    });
  });
  it("destroy", () => {
    expect(destroy("id")).toEqual({
      meta: {},
      payload: {
        id: "id"
      },
      type: DESTROY
    });
  });
  it("clearAll", () => {
    expect(clearAll()).toEqual({
      meta: {},
      payload: {},
      type: CLEAR_ALL
    });
  });
});
