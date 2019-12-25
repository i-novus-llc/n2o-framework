import React from "react";
import { describe, it } from 'mocha';
import { expect } from 'chai';
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
    expect(add(data.id, data)).to.be.deep.equal({
      meta: {},
      payload: data,
      type: ADD
    });
  });
  it("setCounter", () => {
    expect(setCounter("id", 555)).to.be.deep.equal({
      meta: {},
      payload: {
        counterId: "id",
        value: 555
      },
      type: SET_COUNTER
    });
  });
  it("destroy", () => {
    expect(destroy("id")).to.be.deep.equal({
      meta: {},
      payload: {
        id: "id"
      },
      type: DESTROY
    });
  });
  it("clearAll", () => {
    expect(clearAll()).to.be.deep.equal({
      meta: {},
      payload: {},
      type: CLEAR_ALL
    });
  });
});
